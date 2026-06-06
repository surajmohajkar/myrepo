package carwash_auth_service.service;

import carwash_auth_service.entity.OtpVerification;
import carwash_auth_service.repository.OtpRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Random;

@Service
public class OtpService {
    private final OtpRepository otpRepository;
    public OtpService(OtpRepository otpRepository){
        this.otpRepository = otpRepository;
    }
    public String generateOtp(String email){
        String otp = String.valueOf(10000+new Random().nextInt(900000));
        OtpVerification otpVerification = OtpVerification.builder().email(email)
                .otp(otp).verified(false).expiryTime(LocalDateTime.now().plusMinutes(5)).build();
        otpRepository.save(otpVerification);
        return otp;

    }
}
