package com.virtualbookstore.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.virtualbookstore.entity.User;
import com.virtualbookstore.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	private UserService userService;

	@PostMapping("/register")
	public ResponseEntity<User> register(@RequestBody User user) {
		return ResponseEntity.ok(userService.createUser(user));
	}

	@PostMapping("/login")
	public ResponseEntity<Map<String, String>> login(@RequestBody User user) {
		String token = userService.authenticateUser(user.getEmail(), user.getPassword());
		Map<String, String> response = new HashMap<>();
		response.put("token", token);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/details")
	public ResponseEntity<?> getUserDetails(HttpServletRequest request) {
		try {
			String token = userService.extractTokenFromRequest(request);
			UserDetails userDetails = userService.getUserDetails(token);
			return ResponseEntity.ok(userDetails);
		} catch (Exception e) {
			return ResponseEntity.status(401).body(e.getMessage());
		}
	}

	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteUser(HttpServletRequest request, @RequestParam("email") String email) {
	    try {
	        String token = userService.extractTokenFromRequest(request);
	        userService.deleteUserByEmail(token, email);
	        return ResponseEntity.ok("User deleted successfully");
	    } catch (Exception e) {
	        return ResponseEntity.status(401).body(e.getMessage());
	    }
	}
	
	@GetMapping("/all")
    public ResponseEntity<?> getAllUsers(HttpServletRequest request) {
        try {
            String token = userService.extractTokenFromRequest(request);
            List<User> users = userService.getAllUsers(token); 
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }
}
