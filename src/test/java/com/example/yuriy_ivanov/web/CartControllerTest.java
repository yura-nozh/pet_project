package com.example.yuriy_ivanov.web;

import com.example.yuriy_ivanov.dto.cart.CartRequest;
import com.example.yuriy_ivanov.dto.cart.CartResponse;
import com.example.yuriy_ivanov.dto.enums.Type;
import com.example.yuriy_ivanov.dto.line_item.LineItemResponse;
import com.example.yuriy_ivanov.entities.*;
import com.example.yuriy_ivanov.repositories.*;
import com.example.yuriy_ivanov.services.BrandService;
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

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureMockMvc
class CartControllerTest {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    LineItemRepository lineItemRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BrandService brandService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    BrandRepository brandRepository;

    @BeforeEach
    void resetDB() {
        brandRepository.deleteAll();
        userRepository.deleteAll();
        cartRepository.deleteAll();
        productRepository.deleteAll();
    }

    public Product createProduct(Type type, Integer volume, Integer count, Float price) {
        Product product = new Product();
        product.setBrand(createBrand());
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
    public User createUser(String email) {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Due");
        user.setEmail(email);
        user.setPassword("qwerty");

        userRepository.saveAndFlush(user);

        return user;
    }

    public Cart createCart(User user, Product product) {
        Cart cart = new Cart();
        cart.setUser(user);
        LineItem lineItem = new LineItem();
        lineItem.setProduct(product);
        lineItem.setQuantity(1);
        List<LineItem> list = new ArrayList<>();
        list.add(lineItem);
        cart.setLineItems(list);

        cartRepository.save(cart);

        return cart;
    }

    @Test
    @Transactional
    void shouldAddNewItem() throws Exception {
        Product product = createProduct(Type.BUSINESS, 15, 5, 4500.90f);
        User user = createUser("test@mail.tt");
        Integer qty = 1;
        CartRequest cartRequest = new CartRequest(product.getId(), user.getId(), qty);
        MockHttpServletRequestBuilder requestBuilder = post("/cart/addItem")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cartRequest));
        ResultActions resultActions = this.mockMvc.perform(requestBuilder);
        String result = resultActions.andReturn().getResponse().getContentAsString();
        CartResponse cartResponse = objectMapper.readValue(result, CartResponse.class);
        LineItemResponse lineItemResponse = cartResponse.getLineItems().get(0);
        Long id = lineItemResponse.getProduct().getId();
        assertEquals(qty, lineItemResponse.getQuantity());

        MockHttpServletRequestBuilder requestBuilder2 = post("/cart/addItem")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cartRequest));
        ResultActions resultActions2 = this.mockMvc.perform(requestBuilder2);
        String result2 = resultActions2.andReturn().getResponse().getContentAsString();
        CartResponse cartResponse2 = objectMapper.readValue(result2, CartResponse.class);
        LineItemResponse lineItemResponse2 = cartResponse2.getLineItems().get(0);
        Long id2 = lineItemResponse2.getProduct().getId();

        assertEquals(id, id2);
        assertEquals(qty + qty, lineItemResponse2.getQuantity());
    }

    @Test
    @Transactional
    void shouldAddOneMoreItem() throws Exception {
        Product product = createProduct(Type.BUSINESS, 15, 5, 4500.90f);
        User user = createUser("test@mail.ru");
        Cart cart = createCart(user, product);
            int testQuantity = 1;
        CartRequest cartRequest = new CartRequest(product.getId(), user.getId(), testQuantity);

        MockHttpServletRequestBuilder requestBuilder = post("/cart/addItem")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cartRequest));
        ResultActions resultActions = this.mockMvc.perform(requestBuilder);
        String result = resultActions.andReturn().getResponse().getContentAsString();

        CartResponse cartResponse = objectMapper.readValue(result, CartResponse.class);
        List<LineItemResponse> listLineItems = cartResponse.getLineItems();
        Integer newQuantity = 0;
        for(LineItemResponse lineItem : listLineItems) {
            if(lineItem.getProduct().getId().equals(product.getId())) {
                newQuantity = lineItem.getQuantity();
            }
        }
        assertEquals(testQuantity + 1, newQuantity);

    }

    @Test
    @Transactional
    void shouldRemoveItem() throws Exception {
        Product product = createProduct(Type.BUSINESS, 15, 5, 4500.90f);
        User user = createUser("test2@mail.tt");
        Cart cart = createCart(user, product);
        int size = cart.getLineItems().size();
        CartRequest cartRequest = new CartRequest(product.getId(), user.getId(), 100);

        MockHttpServletRequestBuilder requestBuilder = post("/cart/removeItem")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cartRequest));
        ResultActions resultActions = this.mockMvc.perform(requestBuilder);
        String result = resultActions.andReturn().getResponse().getContentAsString();

        // FIXED
        CartResponse cartResponse = objectMapper.readValue(result, CartResponse.class);

        resultActions.andExpect(status().isOk());
        assertEquals(size - 1, cartResponse.getLineItems().size());
    }

    @Test
    @Transactional
    void shouldShowUserCart() throws Exception {
        Product product = createProduct(Type.BUSINESS, 15, 5, 4500.90f);
        User user = createUser("test2@mail.by");
        Cart cart = createCart(user, product);

        ResultActions resultActions = this.mockMvc.perform(get("/cart/showCart/" + user.getId()));
        String result = resultActions.andReturn().getResponse().getContentAsString();
        CartResponse cartResponse = objectMapper.readValue(result, CartResponse.class);

        resultActions.andExpect(status().isOk());
        assertEquals(cart.getId(), cartResponse.getId());
    }

    @Test
    @Transactional
    void shouldRemoveCart() throws Exception {
        Product product = createProduct(Type.BUSINESS, 15, 5, 4500.90f);
        User user = createUser("test2@mail.tt");
        Cart cart = createCart(user, product);

        ResultActions resultActions = this.mockMvc.perform(delete("/cart/" + cart.getId()));
        String result = resultActions.andReturn().getResponse().getContentAsString();
        List<Cart> testList = cartRepository.findAll();

        resultActions.andExpect(status().isOk());
        assertNotNull(result);
        assertEquals("Cart was successfully deleted.", result);
        assertEquals(0, testList.size());
    }
}
