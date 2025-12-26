package in.kenz.travelagency.tourpackage.service.impl;

import in.kenz.travelagency.location.entity.Location;
import in.kenz.travelagency.location.repository.LocationRepository;
import in.kenz.travelagency.tourpackage.dto.TourPackageCreateDTO;
import in.kenz.travelagency.tourpackage.dto.TourPackageResponseDTO;
import in.kenz.travelagency.tourpackage.entity.TourPackage;
import in.kenz.travelagency.tourpackage.repository.TourPackageRepository;
import in.kenz.travelagency.tourpackage.service.TourPackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TourPackageServiceImpl implements TourPackageService {

    private final TourPackageRepository tourPackageRepository;
    private final LocationRepository locationRepository;

    // 🔥 CREATE → evict location cache
    @Override
    @CacheEvict(
            value = "tourPackagesByLocation",
            key = "#dto.locationId"
    )
    public TourPackageResponseDTO create(TourPackageCreateDTO dto) {

        Location location = locationRepository.findById(dto.getLocationId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Location not found"));

        TourPackage tourPackage = TourPackage.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .durationDays(dto.getDurationDays())
                .price(dto.getPrice())
                .location(location)
                .active(true)
                .build();

        tourPackageRepository.save(tourPackage);
        return toDTO(tourPackage);
    }

    @Override
    public List<TourPackageResponseDTO> getAll() {
        return tourPackageRepository.findByActiveTrue()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TourPackageResponseDTO getById(UUID id) {
        TourPackage tourPackage = tourPackageRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Tour package not found"));
        return toDTO(tourPackage);
    }

    // 🔥 READ → cached
    @Override
    @Cacheable(
            value = "tourPackagesByLocation",
            key = "#locationId"
    )
    public List<TourPackageResponseDTO> getByLocation(UUID locationId) {

        System.out.println("🔥 DB HIT for location: " + locationId);

        return tourPackageRepository
                .findByLocation_IdAndActiveTrue(locationId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // 🔥 UPDATE → evict cache
    @Override
    @CacheEvict(
            value = "tourPackagesByLocation",
            allEntries = true
    )
    public void updateStatus(UUID id, boolean active) {
        TourPackage tourPackage = tourPackageRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Tour package not found"));
        tourPackage.setActive(active);
        tourPackageRepository.save(tourPackage);
    }

    // ---------- Mapper ----------
    private TourPackageResponseDTO toDTO(TourPackage tourPackage) {

        TourPackageResponseDTO dto = new TourPackageResponseDTO();
        dto.setId(tourPackage.getId());
        dto.setTitle(tourPackage.getTitle());
        dto.setDescription(tourPackage.getDescription());
        dto.setDurationDays(tourPackage.getDurationDays());
        dto.setPrice(tourPackage.getPrice());
        dto.setLocationId(tourPackage.getLocation().getId());
        dto.setLocationName(tourPackage.getLocation().getName());
        dto.setActive(tourPackage.isActive());

        return dto;
    }
}