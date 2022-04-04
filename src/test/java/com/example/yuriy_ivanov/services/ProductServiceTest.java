package com.example.yuriy_ivanov.services;

import com.example.yuriy_ivanov.dto.enums.Type;
import com.example.yuriy_ivanov.dto.product.ProductRequest;
import com.example.yuriy_ivanov.dto.product.ProductResponse;
import com.example.yuriy_ivanov.entities.Brand;
import com.example.yuriy_ivanov.entities.Product;
import com.example.yuriy_ivanov.repositories.BrandRepository;
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

import java.util.ArrayList;
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

    @Autowired
    BrandRepository brandRepository;

    @Autowired
    BrandService brandService;


    @BeforeEach
    void resetDB() {
        productRepository.deleteAll();
        brandRepository.deleteAll();
    }

    public Product createProduct(Type type, Integer volume, Integer count, Float price) {
        Product product = new Product();
        List<Product> list= new ArrayList<>();
        Brand brand = createBrand();
        list.add(product);
        brand.setProducts(list);
        product.setBrand(brand);
        product.setType(type);
        product.setPrice(price);
        product.setCount(count);
        product.setVolume(volume);

        productRepository.save(product);

        return product;
    }

    public Brand createBrand() {
        return brandService.addNewBrand("THULE");
    }

    public void assertProductEquals(ProductRequest productRequest, ProductResponse productResponse) {
        assertEquals(productRequest.getBrand().getId(), productResponse.getBrand().getId());
        assertEquals(productRequest.getType(), productResponse.getType());
        assertEquals(productRequest.getVolume(), productResponse.getVolume());
        assertEquals(productRequest.getCount(), productResponse.getCount());
    }

    public void assertProductEquals(Product product, ProductResponse productResponse) {
        assertEquals(product.getBrand().getId(), productResponse.getBrand().getId());
        assertEquals(product.getType(), productResponse.getType());
        assertEquals(product.getVolume(), productResponse.getVolume());
        assertEquals(product.getCount(), productResponse.getCount());
    }

    @Transactional
    @Test
    void shouldCreateProduct() {
        ProductRequest newProduct = new ProductRequest(createBrand(), Type.BUSINESS, 15, 5, 4500.90f);
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
        createProduct(Type.BUSINESS, 15, 5, 4500.90f);
        createProduct(Type.SPORTS, 30, 3, 3400.00f);
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "id"));

        List<ProductResponse> list = productService.all(pageable);
        assertEquals(2, list.size());
    }

    @Transactional
    @Test
    void shouldReturnProductById() {
        Product product = createProduct(Type.BUSINESS, 15, 5, 4500.90f);
        ProductResponse productResponse = productService.findById(product.getId());

        assertProductEquals(product, productResponse);
    }

    @Transactional
    @Test
    void shouldUpdateProduct() {
        Product product = createProduct(Type.BUSINESS, 15, 5, 4500.90f);
        ProductRequest newProduct = new ProductRequest(createBrand(), Type.SPORTS, 30, 3, 3400.00f);
        ProductResponse testProduct = productService.update(product.getId(), newProduct);

        assertEquals(product.getId(), testProduct.getId());
        assertProductEquals(product, testProduct);
    }

    @Transactional
    @Test
    void shouldDeleteProduct() {
        Product product = createProduct(Type.BUSINESS, 15, 5, 4500.90f);
        assertNotNull(productRepository.findAll());

        productService.delete(product.getId());
        assertEquals(0, productRepository.findAll().size());
    }
}
