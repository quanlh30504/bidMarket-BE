package com.example.bidMarket.service.impl;

import com.example.bidMarket.Enum.CategoryType;
import com.example.bidMarket.Enum.ProductStatus;
import com.example.bidMarket.SearchService.ProductSpecification;
import com.example.bidMarket.dto.ProductDto;
import com.example.bidMarket.dto.ProductImageDto;
import com.example.bidMarket.dto.Request.ProductCreateRequest;
import com.example.bidMarket.dto.Request.ProductUpdateRequest;
import com.example.bidMarket.exception.AppException;
import com.example.bidMarket.exception.ErrorCode;
import com.example.bidMarket.mapper.ProductMapper;
import com.example.bidMarket.model.Category;
import com.example.bidMarket.model.Product;
import com.example.bidMarket.model.ProductImage;
import com.example.bidMarket.repository.CategoryRepository;
import com.example.bidMarket.repository.ProductImageRepository;
import com.example.bidMarket.repository.ProductRepository;
import com.example.bidMarket.service.ImageService;
import com.example.bidMarket.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.expression.ExpressionException;
import org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;
    private final ImageService imageService;

//    private final ProductCategoryRepository productCategoryRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductDto getProduct(UUID id) throws Exception {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new Exception("Not exist product id: " + id));
        return productMapper.productToProductDto(product);
    }

    @Override
    public List<ProductDto> getAllProduct() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(product -> productMapper.productToProductDto(product))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Product createProduct(ProductCreateRequest request, List<MultipartFile> images) throws Exception {
        Product product = productMapper.productCreateToProduct(request);

        // Save product to get its Id
        product = productRepository.save(product);

        // Lưu hình ảnh sản phẩm
        if (images != null && !images.isEmpty()) {
            List<String> imageUrls = imageService.uploadProductImages(product.getId(), images);
            List<ProductImage> productImages = new ArrayList<>();
            for (int i = 0; i < imageUrls.size(); i++) {
                ProductImage productImage = new ProductImage();
                productImage.setImageUrl(imageUrls.get(i));
                productImage.setPrimary(i == 0);
                productImage.setProduct(product);
                productImages.add(productImage);
            }
            product.setProductImages(productImages);
        }
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public void deleteProduct(UUID productId) {
        Product product = productRepository.findById(productId)
                        .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        if (product.getStatus() == ProductStatus.ACTIVE) {
            throw new AppException(ErrorCode.PRODUCT_DELETION_FAILED);
        }
        productRepository.deleteById(productId);
    }

    @Override
    @Transactional
    public ProductDto updateProduct(UUID id, ProductUpdateRequest request) {
        log.info("Start update product");
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        if (product.getStatus() != ProductStatus.INACTIVE){
            throw new AppException(ErrorCode.PRODUCT_UPDATE_FAILED);
        }
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setStockQuantity(request.getStockQuantity());

        // Cập nhật danh sách ảnh sản phẩm
        List<ProductImage> currentImages = product.getProductImages();
        List<ProductImage> newImages = request.getProductImages().stream()
                .map(productMapper::productImageDtoToProductImage)
                .collect(Collectors.toList());

        // Xóa các ảnh không còn tồn tại trong danh sách mới
        currentImages.removeIf(image -> !newImages.contains(image));

        // Thêm các ảnh mới không có trong danh sách hiện tại
        for (ProductImage newImage : newImages) {
            if (!currentImages.contains(newImage)) {
                newImage.setProduct(product);
                currentImages.add(newImage);
            }
        }

        // Gán lại danh sách ảnh mới cho sản phẩm
        product.setProductImages(currentImages);
        // Danh sách category hiện tại
        Set<Category> currentCategories = product.getCategories();

        Set<Category> newCategories = request.getCategories().stream()
                .map(categoryType -> categoryRepository.findByCategoryType(categoryType)
                        .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED)))
                .collect(Collectors.toSet());

        // Xóa các category không còn trong danh sách mới
        currentCategories.removeIf(existingCategory -> !newCategories.contains(existingCategory));

        // Thêm các category mới không có trong danh sách hiện tại
        for (Category newCategory : newCategories) {
            if (!currentCategories.contains(newCategory)) {
                currentCategories.add(newCategory);
            }
        }
        product.setCategories(currentCategories);

        return productMapper.productToProductDto(productRepository.save(product));
    }

    @Override
    @Transactional
    public ProductDto updateStatus(UUID id, ProductStatus status) throws Exception {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new Exception("Not exist product id " + id));
        product.setStatus(status);
        productRepository.save(product);
        return productMapper.productToProductDto(product);
    }

    public Page<Product> searchProducts(String name, CategoryType categoryType, ProductStatus status,
                                        int page, int size, String sortField, Sort.Direction sortDirection) {

        Specification<Product> spec = Specification
                .where(ProductSpecification.hasName(name))
                .and(ProductSpecification.hasCategoryType(categoryType))
                .and(ProductSpecification.hasStatus(status));

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortField));
        return productRepository.findAll(spec, pageable);
    }
}
