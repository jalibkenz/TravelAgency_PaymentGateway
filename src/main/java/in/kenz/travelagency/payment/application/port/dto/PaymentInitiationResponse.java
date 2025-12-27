package in.kenz.travelagency.payment.application.port.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
public class PaymentInitiationResponse {

    /**
     * Internal booking reference
     */
    private UUID bookingId;

    /**
     * Internal collection request reference
     */
    private UUID collectionRequestId;

    /**
     * Payment provider (RAZORPAY, STRIPE, etc.)
     */
    private String provider;

    /**
     * Amount to be paid
     */
    private BigDecimal amount;

    /**
     * Currency code (INR, USD, etc.)
     */
    private String currency;

    /**
     * Gateway order / session ID
     */
    private String gatewayOrderId;

    /**
     * URL where frontend should redirect the user
     */
    private String redirectUrl;
}