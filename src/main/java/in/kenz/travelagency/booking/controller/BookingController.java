package in.kenz.travelagency.booking.controller;

import in.kenz.travelagency.booking.dto.BookingCreateDTO;
import in.kenz.travelagency.booking.dto.BookingResponseDTO;
import in.kenz.travelagency.booking.service.BookingService;
import in.kenz.travelagency.common.dto.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    // TEMP: userId passed manually (Auth integration later)
    @Operation(
    )
    @PostMapping("/{userId}")
    public ResponseEntity<CommonResponse<BookingResponseDTO>> create(
            @PathVariable UUID userId,
            @RequestBody BookingCreateDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>(
                        true,
                        "Booking created",
                        bookingService.createBooking(userId, dto)
                ));
    }






    @Operation(
    )
    @GetMapping("/user/{userId}")
    public ResponseEntity<CommonResponse<List<BookingResponseDTO>>> getByUser(
            @PathVariable UUID userId) {

        return ResponseEntity.ok(
                new CommonResponse<>(
                        true,
                        "Bookings fetched",
                        bookingService.getBookingsByUser(userId)
                ));
    }






    @Operation(
            tags = { "Traveller Flow"}
    )
    @PatchMapping("/{bookingId}/cancel")
    public ResponseEntity<CommonResponse<Void>> cancel(
            @PathVariable UUID bookingId) {

        bookingService.cancelBooking(bookingId);

        return ResponseEntity.ok(
                new CommonResponse<>(
                        true,
                        "Booking cancelled",
                        null
                ));
    }
}