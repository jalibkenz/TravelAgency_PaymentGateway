package in.kenz.travelagency.booking.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class BookingResponseDTO {

    private UUID bookingId;
    private UUID tourPackageId;
    private String tourTitle;
    private LocalDate travelDate;
    private int travelers;
    private String status;

    // ✅ NEW — safe, additive
    private BigDecimal amountPayable;
}