package com.example.yuriy_ivanov.web;

import com.example.yuriy_ivanov.dto.enums.Type;
import com.example.yuriy_ivanov.dto.product.ProductRequest;
import com.example.yuriy_ivanov.dto.product.ProductResponse;
import com.example.yuriy_ivanov.entities.Brand;
import com.example.yuriy_ivanov.entities.Product;
import com.example.yuriy_ivanov.repositories.BrandRepository;
import com.example.yuriy_ivanov.repositories.ProductRepository;
import com.example.yuriy_ivanov.services.BrandService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

// TODO: 17.03.2022 add product generator for testing purposes put some ~100 dif products

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    BrandService brandService;

    @Autowired
    BrandRepository brandRepository;

    @BeforeEach
    void resetDB() {
        brandRepository.deleteAll();
        productRepository.deleteAll();
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

    public void assertProductEquals(ProductResponse product1, Product product2) {
        assertEquals(product1.getBrand().getId(), product2.getBrand().getId());
        assertEquals(product1.getType(), product2.getType());
        assertEquals(product1.getVolume(), product2.getVolume());
        assertEquals(product1.getCount(), product2.getCount());
        assertEquals(product1.getPrice(), product2.getPrice());
    }

    @Test
    void shouldReturnUSerList() throws Exception {
        createProduct(Type.SPORTS, 30, 10, 5000f);
        ResultActions resultActions = this.mockMvc.perform(get("/bags"));
        String result = resultActions.andReturn().getResponse().getContentAsString();
        List<Product> testList = objectMapper.readValue(result, new TypeReference<>() {});

        resultActions.andExpect(status().isOk());

        assertEquals(1, testList.size());
    }

    @Test
    void shouldReturnProductById() throws Exception {
        Product product = createProduct(Type.SPORTS, 30, 10, 5000f);
        ResultActions resultActions = this.mockMvc.perform(get("/bags/" + product.getId()));
        String result = resultActions.andReturn().getResponse().getContentAsString();
        ProductResponse testProductResponse = objectMapper.readValue(result, new TypeReference<>() {});

        resultActions.andExpect(status().isOk());
        assertProductEquals(testProductResponse, product);
    }

    @Test
    void shouldCreateProduct() throws Exception{
        List<Product> productsBefore = productRepository.findAll();
        ProductRequest newProduct = new ProductRequest(createBrand(), Type.SPORTS, 30, 10, 5000f);

        assertEquals(0, productsBefore.size());

        MockHttpServletRequestBuilder requestBuilder = post("/bags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newProduct));
        ResultActions resultActions = this.mockMvc.perform(requestBuilder);
        String result = resultActions.andReturn().getResponse().getContentAsString();
        ProductResponse testProduct = objectMapper.readValue(result, new TypeReference<>() {});
        List<Product> productsAfter = productRepository.findAll();

        resultActions.andExpect(status().isOk());
        assertNotNull(result);
        assertEquals(1, productsAfter.size());
        assertProductEquals(testProduct, objectMapper.convertValue(newProduct, Product.class));
    }

    @Test
     void shouldUpdateProduct() throws Exception {
        Product product = createProduct(Type.SPORTS, 30, 10, 5000f);
        ProductRequest newProduct = new ProductRequest(createBrand(), Type.SPORTS, 31, 11, 6000f);
        List<Product> productsBefore = productRepository.findAll();

        MockHttpServletRequestBuilder requestBuilder = put("/bags/" + product.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newProduct));
        ResultActions resultActions = this.mockMvc.perform(requestBuilder);
        String result = resultActions.andReturn().getResponse().getContentAsString();
        ProductResponse updateProduct = objectMapper.readValue(result, new TypeReference<>() {});
        List<Product> productsAfter = productRepository.findAll();

        resultActions.andExpect(status().isOk());
        assertNotNull(result);
        assertEquals(productsBefore.size(), productsAfter.size());
        assertProductEquals(updateProduct, productRepository.findById(product.getId()).orElseThrow());
    }

    @Test
    void deleteProduct() throws Exception {
        Product product = createProduct(Type.SPORTS, 30, 10, 5000f);

        ResultActions resultActions = this.mockMvc.perform(delete("/bags/" + product.getId()));
        String result = resultActions.andReturn().getResponse().getContentAsString();
        List<Product> productAfter = productRepository.findAll();

        resultActions.andExpect(status().isOk());
        assertNotNull(result);
        assertEquals(0, productAfter.size());
        assertEquals("Product was successfully deleted.", result);
    }
}
