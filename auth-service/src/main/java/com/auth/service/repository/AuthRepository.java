package com.auth.service.repository;

import java.lang.StackWalker.Option;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.auth.service.entity.UserLogin;
@Repository
public interface AuthRepository extends JpaRepository<UserLogin, Long>{

	Optional<UserLogin> findByUsername(String username);

}
