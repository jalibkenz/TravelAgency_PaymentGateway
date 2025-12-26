package in.kenz.travelagency.tourpackage.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class TourPackageCreateDTO {

    private String title;
    private String description;
    private int durationDays;
    private BigDecimal price;
    private UUID locationId;
}