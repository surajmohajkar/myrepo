package carwash_auth_service.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserProfileResponse {
    private long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String role;
    private String status;

}
