package in.kenz.travelagency.payment.infrastructure.gateway.razorpay;

import in.kenz.travelagency.payment.application.port.PaymentProvider;
import in.kenz.travelagency.payment.application.port.dto.PaymentOrderResponse;
import in.kenz.travelagency.payment.application.port.dto.ProviderPaymentResult;
import in.kenz.travelagency.payment.application.port.dto.WebhookPayload;
import in.kenz.travelagency.payment.domain.entity.Payment;
import in.kenz.travelagency.payment.domain.enums.PaymentProviderType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class RazorpayProvider implements PaymentProvider {

    /* ========================
       INFRA DEPENDENCIES
       ======================== */

    private final RazorpayClient razorpayClient;
    private final RazorpayConfig razorpayConfig;
    private final RazorpayWebhookVerifier webhookVerifier;

    /* ========================
       PROVIDER IDENTITY
       ======================== */

    @Override
    public PaymentProviderType getProviderType() {
        return PaymentProviderType.RAZORPAY;
    }

    /* ========================
       CREATE PAYMENT ATTEMPT
       ======================== */

    @Override
    public PaymentOrderResponse createOrder(Payment payment) {

        /*
         * Razorpay expects amount in minor units (paise).
         * Conversion stays strictly in infra layer.
         */
        long amountInPaise = payment.getAmount()
                .multiply(java.math.BigDecimal.valueOf(100))
                .longValueExact();

        String orderId = razorpayClient.createOrder(
                amountInPaise,
                payment.getCurrency(),
                payment.getId().toString() // receipt / reference
        );

        return PaymentOrderResponse.builder()
                .provider(getProviderType().name())
                .gatewayOrderId(orderId)
                .amount(amountInPaise)
                .currency(payment.getCurrency())
                .metadata(Map.of(
                        "key", razorpayConfig.getKeyId(),
                        "order_id", orderId
                ))
                .build();
    }

    /* ========================
       WEBHOOK VERIFICATION
       ======================== */

    @Override
    public void verifyWebhook(WebhookPayload payload) {

        String signature = payload.getHeaders()
                .get("X-Razorpay-Signature");

        webhookVerifier.verify(
                payload.getPayload(),
                signature,
                razorpayConfig.getWebhookSecret()
        );
    }

    /* ========================
       FETCH PAYMENT STATUS
       ======================== */

    @Override
    public ProviderPaymentResult fetchPaymentStatus(String gatewayPaymentId) {

        String rawResponse = razorpayClient.fetchPayment(gatewayPaymentId);

        /*
         * Infra returns raw provider data only.
         * Interpretation happens in application layer.
         */
        return ProviderPaymentResult.builder()
                .provider(getProviderType())
                .gatewayPaymentId(gatewayPaymentId)
                .providerStatus("UNKNOWN")
                .rawResponse(rawResponse)
                .build();
    }
}