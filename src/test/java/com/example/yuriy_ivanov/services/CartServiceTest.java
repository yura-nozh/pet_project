package com.example.yuriy_ivanov.services;

import com.example.yuriy_ivanov.dto.cart.CartRequest;
import com.example.yuriy_ivanov.dto.cart.CartResponse;
import com.example.yuriy_ivanov.dto.enums.Type;
import com.example.yuriy_ivanov.entities.*;
import com.example.yuriy_ivanov.repositories.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CartServiceTest {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    LineItemRepository lineItemRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CartService cartService;

    @Autowired
    BrandService brandService;

    @Autowired
    BrandRepository brandRepository;

    @AfterEach
    void resetDB() {
        userRepository.deleteAll();
        cartRepository.deleteAll();
        productRepository.deleteAll();
        lineItemRepository.deleteAll();
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


    public User createUser() {
        Random random = new Random();
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Due");
        user.setEmail("mail" + String.valueOf(random.nextInt(100)) + "@mail.ru");
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
    public void shouldCreateCartWithFirstItem () {
        Product product = createProduct(Type.BUSINESS, 15, 5, 4500.90f);
        User user = createUser();
        CartRequest cartRequest = new CartRequest(product.getId(), user.getId(), 100);
        CartResponse cartResponse = cartService.addItem(cartRequest);
        Cart testCart = cartRepository.getById(cartResponse.getId());
        Long userId = testCart.getUser().getId();

        List<LineItem> list = lineItemRepository.findAll();
        Long productId = list.get(0).getProduct().getId();

        assertEquals(user.getId(), userId);
        assertEquals(product.getId(), productId);
    }

    @Transactional
    @Test
    public void shouldIncreaseQuantity() {
        Product product = createProduct(Type.BUSINESS, 15, 5, 4500.90f);
        User user = createUser();
        Cart cart = createCart(user, product);
        LineItem lineItem = lineItemRepository.findLineItemByProductId(product.getId());
        Integer countBefore = lineItem.getQuantity();
        CartRequest cartRequest = new CartRequest(product.getId(), user.getId(), 100);

        CartResponse cartResponse = cartService.addItem(cartRequest);

        LineItem lineItemAfter = lineItemRepository.findLineItemByProductId(product.getId());
        Integer countAfter = lineItemAfter.getQuantity();

        assertEquals(cart.getId(), cartResponse.getId());
        assertEquals(lineItem.getProduct(), lineItemAfter.getProduct());
        assertEquals(countBefore + 100, countAfter);
    }

    @Transactional
    @Test
    public void shouldRemoveItemFromCart() {
        Product product = createProduct(Type.BUSINESS, 15, 5, 4500.90f);
        User user = createUser();
        Cart cart = createCart(user, product);
        Integer qty = 1;
        CartRequest cartRequest = new CartRequest(product.getId(), user.getId(), qty);
        cartService.addItem(cartRequest);
        Integer qtyTest = lineItemRepository.findLineItemByProductId(product.getId()).getQuantity();

        assertEquals(2, qtyTest);

        CartResponse firstCartResponse = cartService.removeItem(cartRequest);
        Integer countAfterFirstRemove = lineItemRepository.findLineItemByProductId(product.getId()).getQuantity();
        assertEquals(qty, countAfterFirstRemove);

        CartResponse secondCartResponse = cartService.removeItem(cartRequest);
        assertEquals(firstCartResponse.getId(), secondCartResponse.getId());
        assertEquals(0, cart.getLineItems().size());
    }

    @Transactional
    @Test
    public void shouldRemoveCart() {
        Product product = createProduct(Type.BUSINESS, 15, 5, 4500.90f);
        User user = createUser();
        Cart cart = createCart(user, product);
        Long cartId = cart.getId();
        List<Cart> listBefore = cartRepository.findAll();

        assertEquals(1, listBefore.size());

        cartService.destroy(cartId);
        List<Cart> listAfter = cartRepository.findAll();

        assertEquals(0, listAfter.size());
    }

    @Transactional
    @Test
    void shouldFindUserCart() {
        Product product = createProduct(Type.BUSINESS, 15, 5, 4500.90f);
        User user = createUser();
        Cart cart = createCart(user, product);

        CartResponse testCart = cartService.showUserCart(user.getId());
        User testUser = cartRepository.findById(testCart.getId()).orElseThrow().getUser();

        assertEquals(user.getId(), testUser.getId());
    }
}
