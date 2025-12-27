package in.kenz.travelagency.payment.application.port.dto;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebhookPayload {

    /**
     * Raw request body (exact string)
     */
    private String payload;

    /**
     * HTTP headers sent by provider
     */
    private Map<String, String> headers;

    /**
     * Provider name (RAZORPAY, STRIPE, etc.)
     */
    private String provider;
}
