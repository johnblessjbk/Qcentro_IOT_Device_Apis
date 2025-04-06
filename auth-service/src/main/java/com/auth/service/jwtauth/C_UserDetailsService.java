package com.auth.service.jwtauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.auth.service.entity.UserLogin;
import com.auth.service.repository.AuthRepository;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class C_UserDetailsService implements UserDetailsService {

    @Autowired
    private AuthRepository authRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserLogin> userLogin = authRepository.findByUsername(username);
        
        if (!userLogin.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }

        return new org.springframework.security.core.userdetails.User(
                userLogin.get().getUsername(),
                userLogin.get().getPassword(),
                userLogin.get().getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getRolename()))
                        .collect(Collectors.toList())
        );
    }
}
