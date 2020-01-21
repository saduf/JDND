package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    OrderController orderController;

    private OrderRepository orderRepo = mock(OrderRepository.class);

    private UserRepository userRepo = mock(UserRepository.class);

    @Before
    public void Setup() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "orderRepository", orderRepo);
        TestUtils.injectObjects(orderController, "userRepository", userRepo);
    }

    @Test
    public void submit_order_happy_path() {
        User stompedUser = new User();
        Cart cart = new Cart();

        stompedUser.setUsername("user1");
        stompedUser.setPassword("user1Pass");

        List<Item> stompedItemList = getItemsTest();

        cart.setId(0L);
        cart.setUser(stompedUser);
        //cart.setItems(stompedItemList);

        for(Item i : stompedItemList) {
            cart.addItem(i);
        }

        stompedUser.setCart(cart);

        UserOrder order = UserOrder.createFromCart(cart);

        when(userRepo.findByUsername("user1")).thenReturn(stompedUser);

        final ResponseEntity<UserOrder> response = orderController.submit("user1");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        UserOrder o = response.getBody();
        assertNotNull(o);
        assertEquals(order.getId(), o.getId());
        assertEquals(order.getTotal(), o.getTotal());
        assertEquals(order.getUser(), o.getUser());

    }

    private List<Item> getItemsTest() {

        Item stompedItem1 = new Item();
        Item stompedItem2 = new Item();

        stompedItem1.setId(0L);
        stompedItem1.setName("Round Widget");
        stompedItem1.setPrice(BigDecimal.valueOf(2.99));
        stompedItem1.setDescription("A widget that is round");

        stompedItem2.setId(1L);
        stompedItem2.setName("Square Widget");
        stompedItem2.setPrice(BigDecimal.valueOf(1.99));
        stompedItem2.setDescription("A widget that is square");

        List<Item> stompedItemList = new ArrayList<Item>();
        stompedItemList.add(stompedItem1);
        stompedItemList.add(stompedItem2);

        return stompedItemList;

    }

}
