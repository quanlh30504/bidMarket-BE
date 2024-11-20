package com.example.bidMarket.controller;

import com.example.bidMarket.Enum.CategoryType;
import com.example.bidMarket.Enum.ProductStatus;
import com.example.bidMarket.SearchService.PaginatedResponse;
import com.example.bidMarket.dto.ProductDto;
import com.example.bidMarket.dto.Request.ProductCreateRequest;
import com.example.bidMarket.dto.Request.ProductUpdateRequest;
import com.example.bidMarket.mapper.ProductMapper;
import com.example.bidMarket.model.Product;
import com.example.bidMarket.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.UUID;


@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ProductMapper productMapper;

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable UUID id) throws Exception {
        return ResponseEntity.ok(productService.getProduct(id));
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts(){
        return ResponseEntity.ok(productService.getAllProduct());
    }

    @GetMapping("/search")
    public PaginatedResponse<ProductDto> searchProducts(
            @RequestParam(required = false) UUID sellerId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) CategoryType categoryType,
            @RequestParam(required = false) ProductStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortField,
            @RequestParam(defaultValue = "ASC") Sort.Direction sortDirection) {

        Page<Product> productPage = productService.searchProducts(sellerId, name, categoryType, status, page, size, sortField, sortDirection);
        List<ProductDto> content = productPage.getContent().stream()
                .map(productMapper::productToProductDto)
                .toList();
        return new PaginatedResponse<>(
                productPage.getNumber(),
                productPage.getSize(),
                productPage.getTotalElements(),
                productPage.getTotalPages(),
                productPage.isLast(),
                productPage.isFirst(),
                content
        );
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductCreateRequest request) throws Exception {
        logger.info("Received request to create product: {}", request);
        Product createdProduct = productService.createProduct(request);
        return ResponseEntity.ok(productMapper.productToProductDto(createdProduct));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable UUID id,
                                                    @Valid @RequestBody ProductUpdateRequest request) throws Exception {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ProductDto> updateStatus (@PathVariable UUID id, @RequestParam ProductStatus newStatus)
            throws Exception {
        return ResponseEntity.ok(productService.updateStatus(id, newStatus));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct (@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Delete successfullt product id " + id);
    }
}
