package com.example.demo.controllers;

import org.apache.http.HttpException;
import org.apache.http.client.HttpResponseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.sql.SQLException;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

	private static Logger log = LogManager.getLogger(UserController.class);

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
			log.info("Found user with id " + user.get().getId() + " with username " + user.get().getUsername() + ".");
		} else{ log.error("Could not find user with id " + id + "."); }

		return ResponseEntity.of(userRepository.findById(id));
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		if (user == null) { log.error("Could not find user with name: " + username);
		} else { log.info("We found " + user.getUsername()); }
			return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
		}

	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) throws HttpResponseException {
		try {
			User user = new User();
			user.setUsername(createUserRequest.getUsername());
			Cart cart = new Cart();
			cartRepository.save(cart);
			user.setCart(cart);
			if (createUserRequest.getPassword().length() < 7 ||
					!createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())) {
				log.error("Failure");
				return ResponseEntity.badRequest().build();
			}

			user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));

			userRepository.save(user);
			log.info("Success");
			return ResponseEntity.ok(user);
		} catch (Exception e) {
			log.error("We messed up.");
			return ResponseEntity.badRequest().build();
		}
	}
}
