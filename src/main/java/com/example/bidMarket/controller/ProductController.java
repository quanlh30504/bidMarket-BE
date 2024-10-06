package com.example.bidMarket.controller;

import com.example.bidMarket.Enum.ProductStatus;
import com.example.bidMarket.dto.ProductDto;
import com.example.bidMarket.dto.Request.ProductCreateRequest;
import com.example.bidMarket.dto.Request.ProductUpdateRequest;
import com.example.bidMarket.mapper.ProductMapper;
import com.example.bidMarket.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ProductMapper productMapper;

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable UUID id) throws Exception {
        return ResponseEntity.ok(productService.getProduct(id));
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts(){
        return ResponseEntity.ok(productService.getAllProduct());
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductCreateRequest request) throws Exception {
        return ResponseEntity.ok(productMapper.productToProductDto(productService.createProduct(request)));

    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct (@PathVariable UUID id, @RequestBody ProductUpdateRequest request)
            throws Exception {
        return ResponseEntity.ok(productService.updateProduct(id,request));
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
