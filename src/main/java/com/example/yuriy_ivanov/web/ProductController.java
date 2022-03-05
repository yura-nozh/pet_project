package com.example.yuriy_ivanov.web;

import com.example.yuriy_ivanov.dto.product_dto.ProductRequest;
import com.example.yuriy_ivanov.dto.product_dto.ProductResponse;
import com.example.yuriy_ivanov.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bags")
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> create(@RequestBody ProductRequest productRequest) {
        ProductResponse bag = productService.create(productRequest);

        return ResponseEntity.ok(bag);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> index(@PageableDefault(sort = { "id" }, direction = Sort.Direction.ASC) Pageable pageable) {
        List<ProductResponse> bags = productService.all(pageable);

        return ResponseEntity.ok(bags);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> show(@PathVariable Long id) {
        ProductResponse productResponse = productService.findById(id);

        return ResponseEntity.ok(productResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(@PathVariable Long id, @RequestBody ProductRequest productRequest) {
        ProductResponse productResponse = productService.update(id, productRequest);

        return ResponseEntity.ok(productResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        productService.delete(id);

        return ResponseEntity.ok("Product was successfully deleted.");
    }
}
