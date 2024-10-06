package com.example.bidMarket.service.impl;

import com.example.bidMarket.Enum.ProductStatus;
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
import com.example.bidMarket.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.ExpressionException;
import org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler;
import org.springframework.stereotype.Service;

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
    public Product createProduct(ProductCreateRequest request) throws Exception {
        Product product = productMapper.productCreateToProduct(request);
        List<ProductImageDto> productImageDtoList = request.getProductImages();
        // Lưu hình ảnh sản phẩm
        if (productImageDtoList != null && !productImageDtoList.isEmpty()) {
            List<ProductImage> productImageList= new ArrayList<>();
            for (ProductImageDto imageDto : productImageDtoList) {
                ProductImage productImage = productMapper.productImageDtoToProductImage(imageDto);
                productImage.setProduct(product);
                productImageList.add(productImage);
            }
            product.setProductImages(productImageList);
        }
        product = productRepository.save(product);
        return product;
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
}
