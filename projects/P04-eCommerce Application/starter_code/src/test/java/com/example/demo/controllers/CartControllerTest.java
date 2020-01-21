package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    CartController cartController;
    private UserRepository userRepo = mock(UserRepository.class);
    private CartRepository cartRepo = mock(CartRepository.class);
    private ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepo);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepo);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepo);
    }

    @Test
    public void add_to_cart_happy_path() throws Exception {
        User stompedUser = new User();
        Cart cart = new Cart();

        stompedUser.setUsername("user1");
        stompedUser.setPassword("user1Pass");

        cart.setUser(stompedUser);
        stompedUser.setCart(cart);

        Item stompedItem = new Item();
        stompedItem.setId(0L);
        stompedItem.setName("Round Widget");
        stompedItem.setPrice(BigDecimal.valueOf(2.99));
        stompedItem.setDescription("A widget that is round");

        when(userRepo.findByUsername("user1")).thenReturn(stompedUser);
        when(itemRepo.findById(0L)).thenReturn(java.util.Optional.of(stompedItem));

        ModifyCartRequest m = new ModifyCartRequest();
        m.setUsername("user1");
        m.setQuantity(3);
        m.setItemId(0L);

        BigDecimal actualTotal = stompedItem.getPrice().multiply(new BigDecimal(m.getQuantity()));

        final ResponseEntity<Cart> response = cartController.addTocart(m);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart c = response.getBody();
        System.out.println(c.getItems());
        assertNotNull(c);
        assertEquals(actualTotal, c.getTotal());

    }


}
