package carwash_auth_service.controller;

import carwash_auth_service.response.UserProfileResponse;
import carwash_auth_service.service.AuthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientController {
    private final AuthService authService;

    public ClientController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/api/client/me")
    public UserProfileResponse me(){
        return authService.getCurrentUser();
    }

    @GetMapping("/api/client/profile")
    public String Profile(){
        return "Client Profile";
    }
}
