package in.kenz.travelagency.booking.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class BookingCreateDTO {

    private UUID tourPackageId;
    private LocalDate travelDate;
    private int travelers;
}