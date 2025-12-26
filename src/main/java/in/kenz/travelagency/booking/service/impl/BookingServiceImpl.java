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

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final TourPackageRepository tourPackageRepository;
    private final UserRepository userRepository;

    @Override
    public BookingResponseDTO createBooking(UUID userId, BookingCreateDTO dto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        TourPackage tourPackage = tourPackageRepository.findById(dto.getTourPackageId())
                .orElseThrow(() -> new IllegalArgumentException("Tour package not found"));

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

    @Override
    public List<BookingResponseDTO> getBookingsByUser(UUID userId) {

        return bookingRepository.findByUser_Id(userId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void cancelBooking(UUID bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }

    private BookingResponseDTO toDTO(Booking booking) {

        BookingResponseDTO dto = new BookingResponseDTO();
        dto.setBookingId(booking.getId());
        dto.setTourPackageId(booking.getTourPackage().getId());
        dto.setTourTitle(booking.getTourPackage().getTitle());
        dto.setTravelDate(booking.getTravelDate());
        dto.setTravelers(booking.getTravelers());
        dto.setStatus(booking.getStatus().name());

        return dto;
    }
}