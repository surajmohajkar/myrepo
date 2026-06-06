package carwash_auth_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WasherController {
    @GetMapping("/api/washer/jobs")
    public String jobs(){
        return "Washer Jobs";
    }
}
