package in.kenz.travelagency.auth.application.controller;

import in.kenz.travelagency.auth.application.dto.LoginRequest;
import in.kenz.travelagency.auth.application.dto.LoginResponse;
import in.kenz.travelagency.auth.application.dto.RegisterRequest;
import in.kenz.travelagency.auth.application.service.AuthService;
import in.kenz.travelagency.common.dto.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public CommonResponse<Void> register(@RequestBody RegisterRequest request) {
        authService.register(request);
        return CommonResponse.success();
    }

    @PostMapping("/login")
    public CommonResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        return CommonResponse.success(authService.login(request));
    }
}