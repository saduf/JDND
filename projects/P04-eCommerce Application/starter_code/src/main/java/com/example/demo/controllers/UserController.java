package com.example.demo.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {
	private Logger log = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		Optional<User> user = userRepository.findById(id);
		if (user.isPresent()) {
			log.info(String.format("User with id %d found, the username is %s", id, user.get().getUsername()));
		}  else {
			log.info(String.format("User with id %d not found", id));
		}
		return ResponseEntity.of(userRepository.findById(id));
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		if (user==null) {
			log.info(String.format("User with username %s not found", username));
		} else {
			log.info(String.format("User with username %s found", username));
		}
		return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
	}
	
	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {

		if(createUserRequest.getPassword().length() < 7 ||
				!createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())) {
			log.info("Error with user password. Cannot create user " + createUserRequest.getUsername());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}

		User user = new User();
		user.setUsername(createUserRequest.getUsername());
		Cart cart = new Cart();
		log.info(String.format("Username created with username %s ", createUserRequest.getUsername()));
		cartRepository.save(cart);
		user.setCart(cart);
		user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
		userRepository.save(user);
		return ResponseEntity.ok(user);
	}
	
}
