package in.kenz.travelagency.auth.application.service;

import in.kenz.travelagency.auth.application.dto.LoginRequest;
import in.kenz.travelagency.auth.application.dto.LoginResponse;
import in.kenz.travelagency.auth.application.dto.RegisterRequest;

public interface AuthService {

    void register(RegisterRequest request);

    LoginResponse login(LoginRequest request);
}