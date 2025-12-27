package in.kenz.travelagency.payment.application.port;


import in.kenz.travelagency.payment.application.port.dto.PaymentOrderResponse;
import in.kenz.travelagency.payment.application.port.dto.ProviderPaymentResult;
import in.kenz.travelagency.payment.application.port.dto.WebhookPayload;
import in.kenz.travelagency.payment.domain.entity.Payment;
import in.kenz.travelagency.payment.domain.enums.PaymentProviderType;

public interface PaymentProvider {

    PaymentProviderType getProviderType();

    PaymentOrderResponse createOrder(Payment payment);

    void verifyWebhook(WebhookPayload payload);

    ProviderPaymentResult fetchPaymentStatus(String gatewayPaymentId);
}