package in.kenz.travelagency.booking.repository;

import in.kenz.travelagency.booking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {

    List<Booking> findByUser_Id(UUID userId);

    List<Booking> findByTourPackage_Id(UUID tourPackageId);
}