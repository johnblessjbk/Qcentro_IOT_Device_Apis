package com.auth.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.auth.service.entity.UserRegister;

@Repository
public interface UserRegisterRepo extends JpaRepository<UserRegister, Long> {

}
