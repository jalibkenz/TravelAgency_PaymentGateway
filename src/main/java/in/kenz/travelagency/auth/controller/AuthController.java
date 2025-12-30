package in.kenz.travelagency.auth.controller;

import in.kenz.travelagency.auth.dto.LoginRequest;
import in.kenz.travelagency.auth.dto.LoginResponse;
import in.kenz.travelagency.auth.dto.SignupFormRequestDTO;
import in.kenz.travelagency.auth.service.AuthService;
import in.kenz.travelagency.common.dto.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;



//    @Operation(summary = "signup")
//    @PostMapping("/signup")
//    public ResponseEntity<Void> signup(@RequestBody SignupFormRequestDTO request) {
//        authService.register(request);
//        return CommonResponse.success();
//    }

    @Operation(summary = "Signup")
    @PostMapping("/signup")
    public ResponseEntity<CommonResponse<String>> signup(
            @RequestBody SignupFormRequestDTO request) {

        authService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CommonResponse.success("User created successfully"));
    }





    @Operation(
            summary = "login"
    )
    @PostMapping("/login")
    public CommonResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        return CommonResponse.success(authService.login(request));
    }



}