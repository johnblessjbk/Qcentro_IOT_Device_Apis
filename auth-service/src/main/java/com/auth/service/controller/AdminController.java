package com.auth.service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auth.service.dto.ApiResponse;
import com.auth.service.entity.UserRole;
import com.auth.service.service.AuthService;
import com.auth.service.util.ErrorMessages;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/auth/admin")
public class AdminController {

	@Autowired
	private AuthService authService;

	@PostMapping("/addRole")
	public ResponseEntity<ApiResponse<String>> getUserDetails(@RequestBody @Valid UserRole userRole, BindingResult bindingResult) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ApiResponse<>("success",ErrorMessages.ROLE_ADDED, null));
	}

	@PostMapping("/assign-role")
	public ResponseEntity<ApiResponse<String>> assignRoleToUser(@RequestParam Long userId, @RequestParam Long roleId) {
		authService.assignRoleToUser(userId, roleId);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ApiResponse<>("success", ErrorMessages.ROLE_ASSIGN, null));

	}
	@GetMapping("/role-List")
	public ResponseEntity<List<UserRole>> getAllRoleList(){
		List<UserRole> ListofRole=authService.getAllRoleList();
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(ListofRole);
	}
}
