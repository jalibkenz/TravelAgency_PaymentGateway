package in.kenz.travelagency.common.exception;

import in.kenz.travelagency.common.dto.CommonResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse<Map<String, String>>> handleValidationErrors(
            MethodArgumentNotValidException ex, WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new CommonResponse<>(false, "Validation failed" + request.getDescription(false), errors));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<CommonResponse<Void>> handleBadCredentials() {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new CommonResponse<>(false,
                        "Invalid username or password", null));
    }

    @ExceptionHandler({DisabledException.class, LockedException.class})
    public ResponseEntity<CommonResponse<Void>> handleAccountDisabled() {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new CommonResponse<>(false,
                        "Account is disabled or locked", null));
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<CommonResponse<Void>> handleDuplicateResource(
            DuplicateResourceException ex, WebRequest request) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new CommonResponse<>(false, ex.getMessage()+"|"+request.getDescription(false), null));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CommonResponse<Void>> handleResourceNotFound(
            ResourceNotFoundException ex,  WebRequest request) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new CommonResponse<>(false, ex.getMessage()+"|"+request.getDescription(false), null));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<CommonResponse<Void>> handleInvalidCredentials(
            InvalidCredentialsException ex,  WebRequest request) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new CommonResponse<>(false, ex.getMessage()+"|"+request.getDescription(false), null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse<Void>> handleUnexpectedError(Exception ex,  WebRequest request) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new CommonResponse<>(false,
                        "Internal server error"+"|"+request.getDescription(false), null));
    }
}
