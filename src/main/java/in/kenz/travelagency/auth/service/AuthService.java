package in.kenz.travelagency.auth.service;

import in.kenz.travelagency.auth.dto.LoginRequest;
import in.kenz.travelagency.auth.dto.LoginResponse;
import in.kenz.travelagency.auth.dto.SignupFormRequestDTO;

public interface AuthService {

    void register(SignupFormRequestDTO request);

    LoginResponse login(LoginRequest request);
}