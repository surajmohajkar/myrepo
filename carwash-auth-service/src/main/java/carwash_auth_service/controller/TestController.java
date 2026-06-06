package carwash_auth_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/api/test/secure")
    public String secureApi(){
        return "Jwt Authentication Working";
    }
}
