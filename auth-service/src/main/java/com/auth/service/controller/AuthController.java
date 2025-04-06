package com.auth.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.auth.service.dto.ApiResponse;
import com.auth.service.dto.LoginRequest;
import com.auth.service.dto.TokenRequest;
import com.auth.service.entity.UserRegister;
import com.auth.service.entity.UserRole;
import com.auth.service.service.AuthService;
import com.auth.service.service.LoginService;
import com.auth.service.service.UserRegisterService;
import com.auth.service.util.ErrorMessages;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth/authenticate")
public class AuthController {

	@Autowired
	private UserRegisterService userRegisterService;
	
	@Autowired
	private LoginService loginService;

	@PostMapping("/userregister")
	public ResponseEntity<ApiResponse<String>> addUserData(@RequestBody @Valid UserRegister user, BindingResult bindingResult) {
		userRegisterService.addUser(user);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ApiResponse<>("success",ErrorMessages.USER_ADDED, null));
	}

	@PostMapping("/login")
	public ResponseEntity<?> addUserData(@RequestBody LoginRequest user) throws Exception {
		return ResponseEntity.status(HttpStatus.CREATED).body(loginService.loginUser(user.getUsername(), user.getPassword()));
	}
	
	@GetMapping("/isTokenValid")
	public ResponseEntity<String> checkTokenValid(@RequestParam("token") String token,TokenRequest tokenData){
		String response= (loginService.isTokenValid(token,tokenData))?"Valid":"Invalid";
		return ResponseEntity.ok(response);
	}
}
