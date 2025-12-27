package in.kenz.travelagency.booking.service.impl;

import in.kenz.travelagency.booking.dto.BookingCreateDTO;
import in.kenz.travelagency.booking.dto.BookingResponseDTO;
import in.kenz.travelagency.booking.entity.Booking;
import in.kenz.travelagency.booking.enums.BookingStatus;
import in.kenz.travelagency.booking.repository.BookingRepository;
import in.kenz.travelagency.booking.service.BookingService;
import in.kenz.travelagency.tourpackage.entity.TourPackage;
import in.kenz.travelagency.tourpackage.repository.TourPackageRepository;
import in.kenz.travelagency.user.entity.User;
import in.kenz.travelagency.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final TourPackageRepository tourPackageRepository;
    private final UserRepository userRepository;

    /* ================================
       CREATE BOOKING
       ================================ */
    @Override
    public BookingResponseDTO createBooking(UUID userId, BookingCreateDTO dto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        TourPackage tourPackage = tourPackageRepository
                .findById(dto.getTourPackageId())
                .orElseThrow(() -> new IllegalArgumentException("Tour package not found"));

        if (dto.getTravelers() <= 0) {
            throw new IllegalArgumentException("Number of travelers must be greater than zero");
        }

        Booking booking = Booking.builder()
                .user(user)
                .tourPackage(tourPackage)
                .travelDate(dto.getTravelDate())
                .travelers(dto.getTravelers())
                .status(BookingStatus.CREATED)
                .active(true)
                .build();

        bookingRepository.save(booking);
        return toDTO(booking);
    }

    /* ================================
       GET BOOKINGS BY USER
       ================================ */
    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getBookingsByUser(UUID userId) {

        return bookingRepository.findByUser_Id(userId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /* ================================
       CANCEL BOOKING
       ================================ */
    @Override
    public void cancelBooking(UUID bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            return; // idempotent cancel
        }

        if (booking.getStatus() == BookingStatus.CONFIRMED) {
            throw new IllegalStateException(
                    "Confirmed booking cannot be cancelled directly. Refund flow required."
            );
        }

        booking.setStatus(BookingStatus.CANCELLED);
        booking.setActive(false);

        bookingRepository.save(booking);
    }

    /* ================================
       DTO MAPPING
       ================================ */
    private BookingResponseDTO toDTO(Booking booking) {

        BookingResponseDTO dto = new BookingResponseDTO();
        dto.setBookingId(booking.getId());
        dto.setTourPackageId(booking.getTourPackage().getId());
        dto.setTourTitle(booking.getTourPackage().getTitle());
        dto.setTravelDate(booking.getTravelDate());
        dto.setTravelers(booking.getTravelers());
        dto.setStatus(booking.getStatus().name());
        dto.setAmountPayable(calculateAmount(booking));

        return dto;
    }

    /* ================================
       PRICE CALCULATION (SOURCE OF TRUTH)
       ================================ */
    private BigDecimal calculateAmount(Booking booking) {
        return booking.getTourPackage()
                .getPrice()
                .multiply(BigDecimal.valueOf(booking.getTravelers()));
    }
}