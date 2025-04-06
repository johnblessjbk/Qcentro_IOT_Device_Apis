package com.auth.service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth.service.entity.UserLogin;
import com.auth.service.entity.UserRegister;
import com.auth.service.repository.UserRegisterRepo;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class UserRegisterService {
	@Autowired
	private UserRegisterRepo userRegisterRepo;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public UserRegister addUser(UserRegister userRegister) {
		UserLogin userLogin = new UserLogin();
		userLogin.setUsername(userRegister.getUserLogin().getUsername());
		String password = passwordEncoder.encode(userRegister.getUserLogin().getPassword());
		userLogin.setPassword(password);

		userRegister.setUserLogin(userLogin);
		userLogin.setUser(userRegister);

		return userRegisterRepo.save(userRegister);
	}
}
