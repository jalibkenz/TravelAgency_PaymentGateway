package in.kenz.travelagency.location.controller;

import in.kenz.travelagency.common.dto.CommonResponse;
import in.kenz.travelagency.location.dto.LocationDTO;
import in.kenz.travelagency.location.service.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @Operation(
            summary = "createLocation"
    )
    @PostMapping
    public ResponseEntity<CommonResponse<LocationDTO>> createLocation(
            @RequestBody LocationDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CommonResponse<>(
                        true,
                        "Location created",
                        locationService.create(dto)
                ));
    }

    @Operation(
            summary = "getAllLocations"
    )
    @GetMapping
    public ResponseEntity<CommonResponse<List<LocationDTO>>> getAllLocations() {

        return ResponseEntity.ok(
                new CommonResponse<>(
                        true,
                        "Locations fetched",
                        locationService.getAllLocations()
                ));
    }


    @Operation(
            summary = "getById"
    )
    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<LocationDTO>> getById(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                new CommonResponse<>(
                        true,
                        "Location found",
                        locationService.getById(id)
                )
        );
    }

    @Operation(
            summary = "getByType"
    )
    @GetMapping("/type/{type}")
    public ResponseEntity<CommonResponse<List<LocationDTO>>> getByType(
            @PathVariable String type) {

        return ResponseEntity.ok(
                new CommonResponse<>(
                        true,
                        "Locations fetched",
                        locationService.getByType(type)
                ));
    }


    @Operation(
            summary = "getByParent"
    )
    @GetMapping("/parent/{parentId}")
    public ResponseEntity<CommonResponse<List<LocationDTO>>> getByParent(
            @PathVariable UUID parentId) {

        return ResponseEntity.ok(
                new CommonResponse<>(
                        true,
                        "Locations fetched",
                        locationService.getByParent(parentId)
                ));
    }



    @Operation(
            summary = "setById"
    )
    @PostMapping("/{id}")
    public ResponseEntity<CommonResponse<LocationDTO>> setById(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                new CommonResponse<>(
                        true,
                        "Location found",
                        locationService.setById(id)
                )
        );
    }

}