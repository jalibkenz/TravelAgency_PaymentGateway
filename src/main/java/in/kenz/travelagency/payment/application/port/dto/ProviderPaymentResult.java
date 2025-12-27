package in.kenz.travelagency.payment.application.port.dto;

import in.kenz.travelagency.payment.domain.enums.PaymentProviderType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProviderPaymentResult {

    /**
     * Provider identifier
     */
    private PaymentProviderType provider;

    /**
     * Provider payment ID
     */
    private String gatewayPaymentId;

    /**
     * Provider order / intent ID
     */
    private String gatewayOrderId;

    /**
     * Raw provider status (e.g. captured, failed, authorized)
     */
    private String providerStatus;

    /**
     * Raw provider response (JSON string)
     */
    private String rawResponse;
}