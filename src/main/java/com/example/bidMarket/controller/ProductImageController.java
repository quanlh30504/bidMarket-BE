package com.example.bidMarket.controller;

import com.example.bidMarket.dto.ProductImageDto;
import com.example.bidMarket.service.ProductImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/product-images")
public class ProductImageController {

    private final ProductImageService productImageService;

    public ProductImageController(ProductImageService productImageService) {
        this.productImageService = productImageService;
    }

    @PostMapping
    public ResponseEntity<ProductImageDto> createProductImage(@RequestBody ProductImageDto productImageDto) {
        ProductImageDto createdProductImage = productImageService.createProductImage(productImageDto);
        return ResponseEntity.ok(createdProductImage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductImageDto> getProductImageById(@PathVariable UUID id) {
        ProductImageDto productImageDto = productImageService.getProductImageById(id);
        return ResponseEntity.ok(productImageDto);
    }

    @GetMapping
    public ResponseEntity<List<ProductImageDto>> getAllProductImages() {
        List<ProductImageDto> productImages = productImageService.getAllProductImages();
        return ResponseEntity.ok(productImages);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductImageDto> updateProductImage(@PathVariable UUID id, @RequestBody ProductImageDto productImageDto) {
        ProductImageDto updatedProductImage = productImageService.updateProductImage(id, productImageDto);
        return ResponseEntity.ok(updatedProductImage);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProductImage(@PathVariable UUID id) {
        productImageService.deleteProductImage(id);
        return ResponseEntity.ok().build();
    }
}
