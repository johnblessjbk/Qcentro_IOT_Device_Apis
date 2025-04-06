package com.auth.service.dto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class LoginResponse {
    private Long userId;
    private String username;
    private String jwtToken;
    private List<String> roles;
    private Date expiryDate;
}
