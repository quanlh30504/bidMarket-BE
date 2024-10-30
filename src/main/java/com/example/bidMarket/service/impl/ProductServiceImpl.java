package com.example.bidMarket.service.impl;

import com.example.bidMarket.AWS.AmazonS3Service;
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
import org.springframework.security.core.parameters.P;
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

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final AmazonS3Service amazonS3Service;

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

        // Save product to get its id
        product = productRepository.save(product);

        if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
            List<ProductImage> productImages = new ArrayList<>();
            for (int i = 0; i < request.getImageUrls().size(); i++) {
                ProductImage productImage = new ProductImage();
                productImage.setImageUrl(request.getImageUrls().get(i));
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

        try {
            product.setStatus(ProductStatus.DELETING);
            productRepository.save(product);

            // !!!need delete product_category table
            productRepository.delete(product);

            for (ProductImage productImage : product.getProductImages()) {
                try {
                    amazonS3Service.deleteFile(productImage.getImageUrl());
                } catch (Exception e) {
                    log.error("Failed to delete image from S3: {}", productImage.getImageUrl(), e);
                }
            }
        } catch (Exception e) {
            compensateDeleteFailure(product);
            throw new AppException(ErrorCode.PRODUCT_DELETION_FAILED);
        }
    }

    private void compensateDeleteFailure(Product product) {
        try {
            product.setStatus(ProductStatus.INACTIVE);
            productRepository.save(product);
        } catch (Exception e) {
            log.error("Failed to compensate for delete failure for product: {}", product.getId(), e);
        }
    }

    @Override
    @Transactional
    public ProductDto updateProduct(UUID id, ProductUpdateRequest request) {
        log.info("Updating product with id: {}", id);

        Product product = findProductById(id);
        validateProductStatus(product);

        updateBasicInfo(product, request);
        updateImages(product, request.getNewImages());
        updateCategories(product, request.getCategories());

        Product updatedProduct = productRepository.save(product);
        log.info("Successfully updated product with id: {}", id);
        return productMapper.productToProductDto(updatedProduct);
    }

    private void updateBasicInfo(Product product, ProductUpdateRequest request) {
        if (request.getName() != null) {
            product.setName(request.getName());
        }
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        // change later
        if (request.getStockQuantity() != 0) {
            product.setStockQuantity(request.getStockQuantity());
        }
    }
    private Product findProductById(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    private void validateProductStatus(Product product) {
        if (product.getStatus() != ProductStatus.INACTIVE){
            throw new AppException(ErrorCode.PRODUCT_UPDATE_FAILED);
        }
    }

    private void updateCategories(Product product, Set<CategoryType> newCategoryTypes) {
        if (newCategoryTypes != null && !newCategoryTypes.isEmpty()) {
            Set<Category> currentCategories = product.getCategories();
            Set<Category> newCategories = newCategoryTypes.stream()
                    .map(categoryType -> categoryRepository.findByCategoryType(categoryType)
                            .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED)))
                    .collect(Collectors.toSet());

            currentCategories.removeIf(existingCategory -> !newCategories.contains(existingCategory));
            currentCategories.addAll(newCategories);
        }
    }
    private void updateImages(Product product, List<String> newImageUrls) {
        if (newImageUrls != null && !newImageUrls.isEmpty()) {
            List<ProductImage> currentImages = product.getProductImages();
            currentImages.clear();

            for (int i = 0; i < newImageUrls.size(); i++) {
                ProductImage newImage = new ProductImage();
                newImage.setImageUrl(newImageUrls.get(i));
                newImage.setPrimary(i == 0);
                newImage.setProduct(product);
                currentImages.add(newImage);
            }
        } else {
            log.info("No new images provided for product ID: {}", product.getId());
        }
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
