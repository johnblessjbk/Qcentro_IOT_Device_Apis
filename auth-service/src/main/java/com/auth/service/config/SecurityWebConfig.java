package com.auth.service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth.service.exceptions.CustomAccessDeniedHandler;
import com.auth.service.jwtauth.C_UserDetailsService;
import com.auth.service.jwtauth.JwtAuthenticationEntryPoint;
import com.auth.service.jwtauth.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityWebConfig {
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	@Autowired
	private C_UserDetailsService customUserDetailsService;
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder,
			UserDetailsService userDetailsService) throws Exception {
		return http.getSharedObject(AuthenticationManagerBuilder.class)
				.userDetailsService(customUserDetailsService)
				.passwordEncoder(bCryptPasswordEncoder)
				.and().build();
	}

	@Bean
	public SecurityFilterChain SecurityFilterChain(HttpSecurity http, 
	                                               JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, 
	                                               JwtAuthenticationFilter jwtAuthenticationFilter,
	                                               CustomAccessDeniedHandler customAccessDeniedHandler) throws Exception {

	    http.csrf().disable()
	            .authorizeHttpRequests((requests) -> requests
	                    .requestMatchers("/auth/authenticate/**", "/v3/api-docs/**", "/swagger-ui/**",
	                            "/swagger-resources/**", "/webjars/**", "/webjars/swagger-ui/**")
	                    .permitAll()
	                    .requestMatchers("/auth/admin/**").hasAuthority("ADMIN")  // Use ROLE_ prefix if necessary
	                    .requestMatchers("/api/devices/**").hasAuthority("USER")
	                    .anyRequest().authenticated())
	            .exceptionHandling()
	                .authenticationEntryPoint(jwtAuthenticationEntryPoint)  // Handles 401 Unauthorized
	                .accessDeniedHandler(customAccessDeniedHandler)  // Handles 403 Forbidden
	            .and()
	            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);  // JWT filter

	    return http.build();
	}

}
