package com.auth.service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth.service.dto.LoginResponse;
import com.auth.service.dto.TokenRequest;
import com.auth.service.entity.UserLogin;
import com.auth.service.jwtauth.C_UserDetailsService;
import com.auth.service.jwtauth.JwtTokenUtil;
import com.auth.service.repository.AuthRepository;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoginService {
	private final Logger log = LoggerFactory.getLogger(LoginService.class);

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private C_UserDetailsService userDetailsService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private AuthRepository userLoginRepository;

	public boolean isTokenValid(String token,TokenRequest userDetails) {
		return jwtTokenUtil.checkTokenValid(token,userDetails);
	}

	public LoginResponse loginUser(String username, String password) throws Exception {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

		// Load user details
		final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

		// Generate JWT token
		String jwtToken = jwtTokenUtil.generateToken(userDetails);
		Date expiryDate = jwtTokenUtil.extractExpiration(jwtToken);

		// Fetch the user from the repository
		UserLogin userLogin = userLoginRepository.findByUsername(username)
				.orElseThrow(() -> new Exception("User not found"));

		// Get role names
		List<String> roles = userLogin.getRoles().stream().map(role -> role.getRolename()).collect(Collectors.toList());

		// Create and return the login response
		return new LoginResponse(userLogin.getId(), userLogin.getUsername(), jwtToken, roles, expiryDate);
	}
}
