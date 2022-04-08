package com.example.yuriy_ivanov.services;

import com.example.yuriy_ivanov.dto.enums.Type;
import com.example.yuriy_ivanov.dto.order.OrderRequest;
import com.example.yuriy_ivanov.dto.order.OrderResponse;
import com.example.yuriy_ivanov.entities.*;
import com.example.yuriy_ivanov.repositories.BrandRepository;
import com.example.yuriy_ivanov.repositories.CartRepository;
import com.example.yuriy_ivanov.repositories.ProductRepository;
import com.example.yuriy_ivanov.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
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
class OrderServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    OrderService orderService;

    @Autowired
    BrandService brandService;

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
    void shouldCreateOrder() {
        User user = createUser();
        Product product = createProduct(Type.BUSINESS, 15, 5, 4500.90f);
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
