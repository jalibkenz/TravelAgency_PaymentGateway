package in.kenz.travelagency.booking.service;

import in.kenz.travelagency.booking.dto.BookingCreateDTO;
import in.kenz.travelagency.booking.dto.BookingResponseDTO;

import java.util.List;
import java.util.UUID;

public interface BookingService {

    BookingResponseDTO createBooking(UUID userId, BookingCreateDTO dto);

    List<BookingResponseDTO> getBookingsByUser(UUID userId);

    void cancelBooking(UUID bookingId);
}