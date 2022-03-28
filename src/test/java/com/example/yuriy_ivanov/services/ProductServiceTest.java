package com.example.yuriy_ivanov.services;

import com.example.yuriy_ivanov.dto.enums.Brand;
import com.example.yuriy_ivanov.dto.enums.Type;
import com.example.yuriy_ivanov.dto.product.ProductRequest;
import com.example.yuriy_ivanov.dto.product.ProductResponse;
import com.example.yuriy_ivanov.entities.Product;
import com.example.yuriy_ivanov.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
@AutoConfigureMockMvc
class ProductServiceTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductService productService;

    @BeforeEach
    void resetDB() {
        productRepository.deleteAll();
    }

    public Product createProduct(Brand brand, Type type, Integer volume, Integer count, Float price) {
        Product product = new Product();
        product.setBrand(brand);
        product.setType(type);
        product.setPrice(price);
        product.setCount(count);
        product.setVolume(volume);

        productRepository.save(product);

        return product;
    }

    public void assertProductEquals(ProductRequest productRequest, ProductResponse productResponse) {
        assertEquals(productRequest.getBrand(), productResponse.getBrand());
        assertEquals(productRequest.getType(), productResponse.getType());
        assertEquals(productRequest.getVolume(), productResponse.getVolume());
        assertEquals(productRequest.getCount(), productResponse.getCount());
    }

    public void assertProductEquals(Product product, ProductResponse productResponse) {
        assertEquals(product.getBrand(), productResponse.getBrand());
        assertEquals(product.getType(), productResponse.getType());
        assertEquals(product.getVolume(), productResponse.getVolume());
        assertEquals(product.getCount(), productResponse.getCount());
    }

    @Transactional
    @Test
    void shouldCreateProduct() {
        ProductRequest newProduct = new ProductRequest(Brand.THULE, Type.BUSINESS, 15, 5, 4500.90f);
        List<Product> list = productRepository.findAll();
        assertEquals(0, list.size());
        ProductResponse testProduct = productService.create(newProduct);

        assertProductEquals(newProduct, testProduct);

        List<Product> newList = productRepository.findAll();
        int fistSize = list.size() + 1;
        assertEquals(fistSize, newList.size());
    }

    @Transactional
    @Test
    void shouldReturnAllProducts() {
        createProduct(Brand.THULE, Type.BUSINESS, 15, 5, 4500.90f);
        createProduct(Brand.ADIDAS, Type.SPORTS, 30, 3, 3400.00f);
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "id"));

        List<ProductResponse> list = productService.all(pageable);
        assertEquals(2, list.size());
    }

    @Transactional
    @Test
    void shouldReturnProductById() {
        Product product = createProduct(Brand.THULE, Type.BUSINESS, 15, 5, 4500.90f);
        ProductResponse productResponse = productService.findById(product.getId());

        assertProductEquals(product, productResponse);
    }

    @Transactional
    @Test
    void shouldUpdateProduct() {
        Product product = createProduct(Brand.THULE, Type.BUSINESS, 15, 5, 4500.90f);
        ProductRequest newProduct = new ProductRequest(Brand.ADIDAS, Type.SPORTS, 30, 3, 3400.00f);
        ProductResponse testProduct = productService.update(product.getId(), newProduct);

        assertEquals(product.getId(), testProduct.getId());
        assertProductEquals(product, testProduct);
    }

    @Transactional
    @Test
    void shouldDeleteProduct() {
        Product product = createProduct(Brand.THULE, Type.BUSINESS, 15, 5, 4500.90f);
        assertNotNull(productRepository.findAll());

        productService.delete(product.getId());
        assertEquals(0, productRepository.findAll().size());
    }
}
