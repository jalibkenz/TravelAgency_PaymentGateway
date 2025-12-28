package in.kenz.travelagency.auth.application.dto;


import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

    private String accessToken;
    private String tokenType;   // "Bearer"
    private Long expiresIn;     // seconds
    private Set<String> roles;  // ["TRAVELLER", "AGENCY"]
}