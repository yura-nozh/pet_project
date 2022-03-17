package com.example.yuriy_ivanov.web;

import com.example.yuriy_ivanov.dto.enums.Brand;
import com.example.yuriy_ivanov.dto.enums.Type;
import com.example.yuriy_ivanov.dto.order.OrderRequest;
import com.example.yuriy_ivanov.dto.order.OrderResponse;
import com.example.yuriy_ivanov.entities.Cart;
import com.example.yuriy_ivanov.entities.LineItem;
import com.example.yuriy_ivanov.entities.Product;
import com.example.yuriy_ivanov.entities.User;
import com.example.yuriy_ivanov.repositories.CartRepository;
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
import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    MockMvc mockMvc;

    public Product createProduct() {
        Product product = new Product();
        product.setBrand(Brand.THULE);
        product.setType(Type.BUSINESS);
        product.setVolume(30);
        product.setCount(10);
        product.setPrice(5000f);
        productRepository.save(product);

        return product;
    }

    public User createUser(String email) {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Due");
        user.setEmail(email);
        user.setPassword("qwerty");
        userRepository.save(user);

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
    public void shouldCreateOrder() throws Exception {
        User user = createUser("mail@mail.com");
        Product product = createProduct();
        Cart cart = createCart(user, product);
        OrderRequest orderRequest = new OrderRequest(user.getId());
        int cartSize = cart.getLineItems().size();

        MockHttpServletRequestBuilder requestBuilder = post("/orders/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest));
        ResultActions resultActions = this.mockMvc.perform(requestBuilder);
        String result = resultActions.andReturn().getResponse().getContentAsString();
        OrderResponse orderResponse = objectMapper.readValue(result, OrderResponse.class);

        resultActions.andExpect(status().isOk());
        assertEquals(user.getId(), orderResponse.getUser().getId());
        assertEquals(cartSize, orderResponse.getLineItems().size());
        // TODO: 17.03.2022 check if products in order cant be bought after order created
    }
}
