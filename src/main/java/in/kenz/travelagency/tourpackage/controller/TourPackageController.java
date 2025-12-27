package in.kenz.travelagency.tourpackage.controller;

import in.kenz.travelagency.common.dto.CommonResponse;
import in.kenz.travelagency.tourpackage.dto.TourPackageCreateDTO;
import in.kenz.travelagency.tourpackage.dto.TourPackageResponseDTO;
import in.kenz.travelagency.tourpackage.service.TourPackageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tour-packages")
@RequiredArgsConstructor
public class TourPackageController {

    private final TourPackageService tourPackageService;

    @PostMapping
    public ResponseEntity<CommonResponse<TourPackageResponseDTO>> create(
            @RequestBody TourPackageCreateDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CommonResponse<>(
                        true,
                        "Tour package created",
                        tourPackageService.create(dto)
                ));
    }

    @Operation(
    )
    @GetMapping
    public ResponseEntity<CommonResponse<List<TourPackageResponseDTO>>> getAll() {

        return ResponseEntity.ok(
                new CommonResponse<>(
                        true,
                        "Tour packages fetched",
                        tourPackageService.getAll()
                ));
    }

    @Operation(
    )
    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<TourPackageResponseDTO>> getById(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                new CommonResponse<>(
                        true,
                        "Tour package fetched",
                        tourPackageService.getById(id)
                ));
    }


    @Operation(
            summary = "Updated with REDIS"
    )
    @GetMapping("/location/{locationId}")
    public ResponseEntity<CommonResponse<List<TourPackageResponseDTO>>> getByLocation(
            @PathVariable UUID locationId) {

        return ResponseEntity.ok(
                new CommonResponse<>(
                        true,
                        "Tour packages fetched",
                        tourPackageService.getByLocation(locationId)
                ));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<CommonResponse<Void>> updateStatus(
            @PathVariable UUID id,
            @RequestParam boolean active) {

        tourPackageService.updateStatus(id, active);

        return ResponseEntity.ok(
                new CommonResponse<>(
                        true,
                        "Tour package status updated",
                        null
                ));
    }
}