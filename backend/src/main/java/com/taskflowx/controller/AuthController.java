package com.taskflowx.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskflowx.dto.request.LoginRequest;
import com.taskflowx.dto.response.ApiResponse;
import com.taskflowx.dto.response.AuthResponse;
import com.taskflowx.service.AuthService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthController {
    
    private final AuthService authService;

    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request){
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }
}
