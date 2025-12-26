package in.kenz.travelagency.tourpackage.service;

import in.kenz.travelagency.tourpackage.dto.TourPackageCreateDTO;
import in.kenz.travelagency.tourpackage.dto.TourPackageResponseDTO;

import java.util.List;
import java.util.UUID;

public interface TourPackageService {

    TourPackageResponseDTO create(TourPackageCreateDTO dto);

    List<TourPackageResponseDTO> getAll();

    TourPackageResponseDTO getById(UUID id);

    List<TourPackageResponseDTO> getByLocation(UUID locationId);

    void updateStatus(UUID id, boolean active);
}