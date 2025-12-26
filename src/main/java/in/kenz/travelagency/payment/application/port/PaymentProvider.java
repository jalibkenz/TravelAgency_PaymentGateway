package in.kenz.travelagency.payment.application.port;

import in.kenz.travelagency.payment.domain.entity.Payment;

public interface PaymentProvider {

    String getProviderName();

    PaymentOrderResponse createOrder(Payment payment);

    void verifyWebhook(WebhookPayload payload);

    ProviderPaymentResult fetchPaymentStatus(String gatewayPaymentId);
}