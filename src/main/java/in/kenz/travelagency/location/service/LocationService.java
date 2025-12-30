package in.kenz.travelagency.location.service;

import in.kenz.travelagency.location.dto.LocationDTO;

import java.util.List;
import java.util.UUID;

public interface LocationService {

    LocationDTO create(LocationDTO dto);

    List<LocationDTO> getAllLocations();
    List<LocationDTO> getByType(String type);
    List<LocationDTO> getByParent(UUID parentId);

    LocationDTO getById(UUID id);
    LocationDTO setById(UUID id);
}