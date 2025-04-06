package com.auth.service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auth.service.entity.UserLogin;
import com.auth.service.entity.UserRole;
import com.auth.service.repository.AuthRepository;
import com.auth.service.repository.UserRoleRepository;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class AuthService {
	@Autowired
	private UserRoleRepository userRoleRepository;
	
	@Autowired
	private AuthRepository authRepository;

	public UserRole addUserRole(UserRole userRole) {
		return userRoleRepository.save(userRole);
	}

	 public void assignRoleToUser(Long userId, Long roleId) {
	        UserLogin user = authRepository.findById(userId)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	        UserRole role = userRoleRepository.findById(roleId)
	            .orElseThrow(() -> new RuntimeException("Role not found"));

	        user.getRoles().add(role);
	        authRepository.save(user);
	    }

	public List<UserRole> getAllRoleList() {

	return 	userRoleRepository.findAll();
	}
}
