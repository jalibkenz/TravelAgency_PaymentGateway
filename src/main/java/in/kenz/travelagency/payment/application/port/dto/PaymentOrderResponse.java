package in.kenz.travelagency.payment.application.port.dto;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentOrderResponse {

    /**
     * Provider identifier (RAZORPAY, STRIPE, etc.)
     */
    private String provider;

    /**
     * Provider-side order / intent / reference ID
     */
    private String gatewayOrderId;

    /**
     * Amount in minor units if required by provider
     */
    private Long amount;

    /**
     * Currency code (INR, USD, etc.)
     */
    private String currency;

    /**
     * Data required by frontend SDK / redirect
     * Example:
     * - Razorpay key_id, order_id
     * - Stripe client_secret
     */
    private Map<String, String> metadata;

    /**
     * Redirect URL (for hosted checkout flows)
     */
    private String redirectUrl;
}
