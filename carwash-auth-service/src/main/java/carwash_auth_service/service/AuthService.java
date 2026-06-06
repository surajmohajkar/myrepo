package carwash_auth_service.service;

import carwash_auth_service.entity.OtpVerification;
import carwash_auth_service.entity.User;
import carwash_auth_service.enums.UserStatus;
import carwash_auth_service.exception.BusinessException;
import carwash_auth_service.repository.OtpRepository;
import carwash_auth_service.repository.UserRepository;
import carwash_auth_service.request.LoginRequest;
import carwash_auth_service.request.RegisterRequest;
import carwash_auth_service.request.VerifyOtpRequest;
import carwash_auth_service.response.ApiResponse;
import carwash_auth_service.response.LoginResponse;
import carwash_auth_service.response.UserProfileResponse;
import carwash_auth_service.security.JwtService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final OtpService otpService;
    private final OtpRepository otpRepository;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository, BCryptPasswordEncoder passwordEncoder,OtpService otpService,
            OtpRepository otpRepository, JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.otpService = otpService;
        this.otpRepository = otpRepository;
        this.jwtService = jwtService;
    }

    public ApiResponse register(
            RegisterRequest request) {


        if (userRepository.findByEmail(
                request.getEmail()).isPresent()) {

            throw new BusinessException("Email already registered");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .password(
                        passwordEncoder.encode(
                                request.getPassword()))
                .role(request.getRole())
                .status(
                        UserStatus.PENDING_VERIFICATION)
                .build();

        userRepository.save(user);
        String otp =
                otpService.generateOtp(
                        request.getEmail());

        System.out.println("Generated OTP = " + otp);

        return new ApiResponse("User Registered Successfully");
    }
    public ApiResponse verifyOtp(
            VerifyOtpRequest request) {

        OtpVerification otpVerification =
                otpRepository
                        .findTopByEmailOrderByIdDesc(
                                request.getEmail())
                        .orElseThrow(() ->
                                new BusinessException("OTP Not Found"));

        if (otpVerification.isVerified()) {

            throw new BusinessException("OTP Already Used");
        }

        if (!otpVerification.getOtp()
                .equals(request.getOtp())) {

            throw new BusinessException("Invalid OTP");
        }

        if (LocalDateTime.now()
                .isAfter(
                        otpVerification.getExpiryTime())) {

            throw new BusinessException("OTP Expired");
        }

        User user =
                userRepository
                        .findByEmail(
                                request.getEmail())
                        .orElseThrow(() ->
                                new BusinessException("User Not Found"));

        user.setStatus(UserStatus.ACTIVE);

        userRepository.save(user);

        otpVerification.setVerified(true);

        otpRepository.save(otpVerification);

        return new ApiResponse("OTP Verified Successfully");
    }
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(()->
                        new BusinessException("User not found"));
        if(user.getStatus() != UserStatus.ACTIVE) {
            throw new BusinessException("Account Not Verified");
        }
        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if(!passwordMatches) {
            throw new BusinessException("Invalid credentials");
        }
        String token = jwtService.generateToken(
                user.getEmail(),user.getRole().name());

        return new LoginResponse(token, user.getRole().name(),"Login Successfully");
    }

    public UserProfileResponse getCurrentUser(){
        String email =
                org.springframework.security.core
                        .context.SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new  BusinessException("User not found"));

        return new UserProfileResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRole().name(),
                user.getStatus().name()
        );
    }
}