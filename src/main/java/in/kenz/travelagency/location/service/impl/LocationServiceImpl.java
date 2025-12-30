package in.kenz.travelagency.location.service.impl;

import in.kenz.travelagency.common.exception.DuplicateResourceException;
import in.kenz.travelagency.location.dto.LocationDTO;
import in.kenz.travelagency.location.entity.Location;
import in.kenz.travelagency.location.entity.LocationType;
import in.kenz.travelagency.location.repository.LocationRepository;
import in.kenz.travelagency.location.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;


    @Override
    public LocationDTO create(LocationDTO dto) {

        LocationType type = LocationType.valueOf(dto.getType());

        if (locationRepository.existsByNameAndTypeAndParent_Id(
                dto.getName(), type, dto.getParentId())) {
            throw new DuplicateResourceException("Location already exists");
        }

        Location parent = null;
        if (dto.getParentId() != null) {
            parent = locationRepository.findById(dto.getParentId())
                    .orElseThrow(() ->
                            new IllegalArgumentException("Parent location not found"));
        }

        Location location = Location.builder()
                .name(dto.getName())
                .type(type)
                .parent(parent)
                .build();

        locationRepository.save(location);

        dto.setId(location.getId());
        return dto;
    }







    @Override
    public List<LocationDTO> getAllLocations() {

        return locationRepository
                .findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }






    @Override
    public List<LocationDTO> getByType(String type) {

        return locationRepository
                .findByType(LocationType.valueOf(type))
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<LocationDTO> getByParent(UUID parentId) {

        return locationRepository
                .findByParent_Id(parentId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private LocationDTO toDTO(Location location) {

        LocationDTO dto = new LocationDTO();
        dto.setId(location.getId());
        dto.setName(location.getName());
        dto.setType(location.getType().name());
        dto.setParentId(
                location.getParent() != null
                        ? location.getParent().getId()
                        : null
        );
        return dto;
    }


    @Override
    public LocationDTO getById(UUID id) {

        Location location = locationRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Location not found"));

        return toDTO(location);
    }

    @Override
    public LocationDTO setById(UUID id) {

        Location location = locationRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Location not found"));
        LocationDTO dto = new LocationDTO();
        dto = toDTO(location);
        return dto;
    }
}