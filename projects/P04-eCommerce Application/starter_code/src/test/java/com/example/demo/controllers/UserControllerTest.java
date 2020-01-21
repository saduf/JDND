package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepo = mock(UserRepository.class);

    private CartRepository cartRepo = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepo);
        TestUtils.injectObjects(userController, "cartRepository", cartRepo);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
        // ToDo complete the setup
    }

    @Test
    public void create_user_happy_path() throws Exception {
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");

        final ResponseEntity<User> response = userController.createUser(r);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("test", u.getUsername());
        assertEquals("thisIsHashed", u.getPassword());
    }

    @Test
    public void find_by_id_happy_path() throws Exception {

        User stompedUser = new User();
        Cart cart = new Cart();

        stompedUser.setUsername("user1");
        stompedUser.setPassword("user1Pass");

        cart.setUser(stompedUser);
        stompedUser.setCart(cart);

        when(userRepo.findById(0L)).thenReturn(java.util.Optional.of(stompedUser));

        final ResponseEntity<User> response = userController.findById(0L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("user1", u.getUsername());
        assertEquals("user1Pass", u.getPassword());
    }

    @Test
    public void find_by_username_happy_path() throws Exception {

        User stompedUser = new User();
        Cart cart = new Cart();

        stompedUser.setUsername("user1");
        stompedUser.setPassword("user1Pass");

        cart.setUser(stompedUser);
        stompedUser.setCart(cart);

        when(userRepo.findByUsername("user1")).thenReturn(stompedUser);

        final ResponseEntity<User> response = userController.findByUserName("user1");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("user1", u.getUsername());
        assertEquals("user1Pass", u.getPassword());
    }

}
