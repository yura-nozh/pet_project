package com.example.yuriy_ivanov.services;

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
import com.sun.xml.bind.v2.TODO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    OrderService orderService;

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

    public User createUser() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Due");
        user.setEmail("mail@mail.com");
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

    @Transactional
    @Test
    public void shouldCreateOrder() {
        User user = createUser();
        Product product = createProduct();
        Cart cart = createCart(user, product);
        OrderRequest orderRequest = new OrderRequest(user.getId());
        Integer productListFromCart = cart.getLineItems().size();
        OrderResponse orderResponse = orderService.createOrder(orderRequest);
        Integer testList = orderResponse.getLineItems().size();
        List<Cart> list = cartRepository.findAll();
//         FIXED
        assertEquals(0, list.size());
        assertEquals(productListFromCart, testList);
        assertEquals(cart.getUser().getId(), orderResponse.getUser().getId());
    }
}
