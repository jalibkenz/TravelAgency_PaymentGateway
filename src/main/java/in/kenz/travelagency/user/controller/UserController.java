package in.kenz.travelagency.user.controller;

import in.kenz.travelagency.common.dto.CommonResponse;
import in.kenz.travelagency.user.dto.ChangePasswordDTO;
import in.kenz.travelagency.user.dto.UserDTO;
import in.kenz.travelagency.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;






    @Operation(summary = "getProfile")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User profile fetched"),
            @ApiResponse(responseCode = "404", description = "User not found!"),
    })
    @GetMapping("/{username}")
    public ResponseEntity<CommonResponse<UserDTO>> getProfile(
            @PathVariable String username) {

        return ResponseEntity.ok(
                new CommonResponse<>(
                        true,
                        "User profile received from DB",
                        userService.findByUsername(username)
                )
        );
    }






    @Operation(summary = "updateProfile")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User profile updated!"),
            @ApiResponse(responseCode = "404", description = "User not found to update!"),
            @ApiResponse(responseCode = "409", description = "Email already in DB")
    })
    //updateProfile
    @PutMapping("/{username}")
    public ResponseEntity<CommonResponse<Void>> updateProfile(
            @PathVariable String username,
            @RequestBody UserDTO userDTO) {

        userService.updateProfile(username, userDTO);

        return ResponseEntity.ok(
                new CommonResponse<>(
                        true,
                        "Profile updated successfully",
                        null
                )
        );
    }

    @Operation(summary = "changePassword")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User password changed"),
            @ApiResponse(responseCode = "404", description = "User not found to update!"),
            @ApiResponse(responseCode = "400", description = "User didn't provide correct existing saved password!")
    })
    @PutMapping("/{username}/password")
    public ResponseEntity<CommonResponse<Void>> changePassword(
            @PathVariable String username,
            @Valid @RequestBody ChangePasswordDTO dto) {

        userService.changePassword(username, dto);

        return ResponseEntity.ok(
                new CommonResponse<>(
                        true,
                        "Password changed successfully",
                        null
                )
        );
    }
}