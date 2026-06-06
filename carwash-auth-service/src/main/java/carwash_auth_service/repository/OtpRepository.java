package carwash_auth_service.repository;

import carwash_auth_service.entity.OtpVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<OtpVerification, Long> {
    Optional<OtpVerification>findTopByEmailOrderByIdDesc(String email);
}
