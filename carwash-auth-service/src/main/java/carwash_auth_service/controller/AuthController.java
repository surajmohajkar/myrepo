package carwash_auth_service.controller;

import carwash_auth_service.request.LoginRequest;
import carwash_auth_service.request.VerifyOtpRequest;
import carwash_auth_service.response.ApiResponse;
import carwash_auth_service.request.RegisterRequest;
import carwash_auth_service.response.LoginResponse;
import carwash_auth_service.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ApiResponse register(@Valid @RequestBody
            RegisterRequest request) {

        return authService.register(request);
    }
    @PostMapping("/verify-otp")
    public ApiResponse verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        return authService.verifyOtp(request);
    }
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
}