package in.kenz.travelagency.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserProfileResponse {
    private String username;
    private String email;
    private String mobileNumber;
}
