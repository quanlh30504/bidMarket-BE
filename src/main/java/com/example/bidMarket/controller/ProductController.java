package com.example.bidMarket.controller;

import com.example.bidMarket.dto.ProductDto;
import com.example.bidMarket.dto.ProductImageDto;
import com.example.bidMarket.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        ProductDto createdProduct = productService.createProduct(productDto);
        return ResponseEntity.ok(createdProduct);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable UUID id) {
        ProductDto productDto = productService.getProductById(id);
        return ResponseEntity.ok(productDto);
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable UUID id, @RequestBody ProductDto productDto) {
        ProductDto updatedProduct = productService.updateProduct(id, productDto);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{productId}/images")
    public ResponseEntity<ProductImageDto> addProductImage(@PathVariable UUID productId, @RequestBody ProductImageDto imageDto) {
        ProductImageDto createdImage = productService.addProductImage(productId, imageDto);
        return ResponseEntity.ok(createdImage);
    }

    @GetMapping("/{productId}/images")
    public ResponseEntity<List<ProductImageDto>> getProductImages(@PathVariable UUID productId) {
        List<ProductImageDto> images = productService.getProductImages(productId);
        return ResponseEntity.ok(images);
    }

    @PutMapping("/{productId}/images/{imageId}")
    public ResponseEntity<ProductImageDto> updateProductImage(@PathVariable UUID productId, @PathVariable UUID imageId, @RequestBody ProductImageDto imageDto) {
        ProductImageDto updatedImage = productService.updateProductImage(productId, imageId, imageDto);
        return ResponseEntity.ok(updatedImage);
    }

    @DeleteMapping("/{productId}/images/{imageId}")
    public ResponseEntity<Void> deleteProductImage(@PathVariable UUID productId, @PathVariable UUID imageId) {
        productService.deleteProductImage(productId, imageId);
        return ResponseEntity.noContent().build();
    }
}
