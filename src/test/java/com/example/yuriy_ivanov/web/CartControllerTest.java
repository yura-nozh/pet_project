package com.example.yuriy_ivanov.web;

import com.example.yuriy_ivanov.dto.cart.CartRequest;
import com.example.yuriy_ivanov.dto.cart.CartResponse;
import com.example.yuriy_ivanov.entities.Cart;
import com.example.yuriy_ivanov.entities.LineItem;
import com.example.yuriy_ivanov.entities.Product;
import com.example.yuriy_ivanov.entities.User;
import com.example.yuriy_ivanov.repositories.CartRepository;
import com.example.yuriy_ivanov.repositories.LineItemRepository;
import com.example.yuriy_ivanov.repositories.ProductRepository;
import com.example.yuriy_ivanov.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureMockMvc
public class CartControllerTest {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    LineItemRepository lineItemRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    public Product createProduct() {
        Product product = new Product();
        product.setBrand("Thule");
        product.setType("business");
        product.setVolume(30);
        product.setCount(10);
        product.setPrice(5000f);

        productRepository.saveAndFlush(product);

        return product;
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
//        lineItem.setCart(cart);
        List<LineItem> list = new ArrayList<>();
        list.add(lineItem);
        cart.setLineItems(list);

        cartRepository.save(cart);

        return cart;
    }

    @Test
    public void shouldAddNewItem() throws Exception {
        Product product = createProduct();
        User user = createUser("test@mail.tt");
        CartRequest cartRequest = new CartRequest(product.getId(), user.getId());
        MockHttpServletRequestBuilder requestBuilder = post("/cart/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cartRequest));
        ResultActions resultActions = this.mockMvc.perform(requestBuilder);
        String result = resultActions.andReturn().getResponse().getContentAsString();

        MockHttpServletRequestBuilder requestBuilder2 = post("/cart/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cartRequest));
        ResultActions resultActions2 = this.mockMvc.perform(requestBuilder2);
        String result2 = resultActions2.andReturn().getResponse().getContentAsString();
    }

    @Test
    @Transactional
    public void shouldRemoveItem() throws Exception {
        Product product = createProduct();
        User user = createUser("test2@mail.tt");
        Cart cart = createCart(user, product);
        int size = cart.getLineItems().size();
        CartRequest cartRequest = new CartRequest(product.getId(), user.getId());

        MockHttpServletRequestBuilder requestBuilder3 = post("/cart/remove")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cartRequest));
        ResultActions resultActions3 = this.mockMvc.perform(requestBuilder3);
        String result3 = resultActions3.andReturn().getResponse().getContentAsString();

        CartResponse cartResponse = objectMapper.readValue(result3, CartResponse.class);

        resultActions3.andExpect(status().isOk());
        assertEquals(size - 1, cartResponse.getLineItems().size());
    }

}
