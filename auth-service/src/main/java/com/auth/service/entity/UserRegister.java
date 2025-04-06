package com.auth.service.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
public class UserRegister {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@NotEmpty(message = "Name should not be null")
	@Size(min = 3, max = 15, message = "Name should be minimum 3 and maximum 15 characters")
	private String name;

	@NotEmpty(message = "Email should not be null")
	@Email(message = "Please provide a valid email address")
	private String email;

	@NotEmpty(message = "Phone should not be null")
	@Pattern(regexp = "\\d{10}", message = "Enter a valid 10-digit phone number")
	private String phone;

	@NotEmpty(message = "Password should not be empty")
	@Size(min = 8, message = "Password should be at least 8 characters long")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=]).{8,}$", message = "Password must contain at least one digit, one uppercase letter, one lowercase letter, and one special character (@#$%^&+=)")
	private String password;

	@Transient
	private String confirmPassword;
	
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	private UserLogin userLogin;
}
