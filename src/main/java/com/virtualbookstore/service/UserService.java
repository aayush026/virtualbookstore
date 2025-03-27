package com.virtualbookstore.service;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.virtualbookstore.entity.User;
import com.virtualbookstore.repo.UserRepo;
import com.virtualbookstore.util.JWTUtil;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserService implements UserDetailsService {

	private final UserRepo userRepository;
	private final JWTUtil jwtUtil;
	private final AuthenticationManager authenticationManager;
	private final PasswordEncoder encoder;

	public UserService(UserRepo userRepository, JWTUtil jwtUtil, @Lazy AuthenticationManager authenticationManager, // 🔥
																													// Add
																													// @Lazy
																													// here
			PasswordEncoder encoder) {
		this.userRepository = userRepository;
		this.jwtUtil = jwtUtil;
		this.authenticationManager = authenticationManager;
		this.encoder = encoder;
	}

	public User createUser(User user) {
		user.setPassword(encoder.encode(user.getPassword())); // Encrypt password
		return userRepository.save(user);
	}

	public String authenticateUser(String email, String password) {
		System.out.println("email : " + email + "password : " + password);
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found!"));
		System.out.println("user :  AJI17 : " + user);
//		if (!encoder.matches(password, user.getPassword())) {
//			throw new BadCredentialsException("Invalid credentials");
//		}
		System.out.println("UserService.authenticateUser() :: 1");
		// authenticationManager.authenticate(new
		// UsernamePasswordAuthenticationToken(email, password));
		System.out.println("UserService.authenticateUser() :: 2");
		UserDetails userDetails = loadUserByUsername(email);
		return jwtUtil.generateToken(userDetails.getUsername());
	}

	@Override
	public UserDetails loadUserByUsername(String email) {
		return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
	}

	public String extractTokenFromRequest(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			return authHeader.substring(7);
		}
		throw new RuntimeException("Missing or invalid Authorization header");
	}

	public UserDetails getUserDetails(String token) {
		String username = jwtUtil.extractUserName(token);

		if (username == null || !jwtUtil.isTokenValid(token, username)) {
			throw new RuntimeException("Invalid or expired token");
		}

		return loadUserByUsername(username);
	}

	public void deleteUserByEmail(String token, String email) {
		String usernameFromToken = jwtUtil.extractUserName(token);
		if (usernameFromToken == null || !jwtUtil.isTokenValid(token, usernameFromToken)) {
			throw new RuntimeException("Invalid or expired token");
		}

		User userRequesting = userRepository.findByEmail(usernameFromToken)
				.orElseThrow(() -> new RuntimeException("Requesting user not found"));

		User userToDelete = userRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("User not found with email: " + email));

		if (!userRequesting.getEmail().equals(email) && !userRequesting.getRole().equals("ROLE_ADMIN")) {
			throw new RuntimeException("Unauthorized to delete this user");
		}

		userRepository.delete(userToDelete);
	}

	public List<User> getAllUsers(String token) {
		String email = jwtUtil.extractUserName(token);
		User requestingUser = userRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("User not found"));

		if (!requestingUser.getRole().equals(User.Role.ADMIN)) {
			throw new RuntimeException("Unauthorized! Only admins can access user list.");
		}

		return userRepository.findAll();
	}
}
