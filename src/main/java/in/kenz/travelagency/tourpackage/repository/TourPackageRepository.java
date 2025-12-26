package in.kenz.travelagency.tourpackage.repository;

import in.kenz.travelagency.tourpackage.entity.TourPackage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TourPackageRepository extends JpaRepository<TourPackage, UUID> {

    List<TourPackage> findByActiveTrue();

    List<TourPackage> findByLocation_IdAndActiveTrue(UUID locationId);
}