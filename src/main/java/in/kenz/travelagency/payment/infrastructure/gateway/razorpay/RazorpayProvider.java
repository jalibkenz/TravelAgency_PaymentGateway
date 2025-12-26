package in.kenz.travelagency.payment.infrastructure.gateway.razorpay;

import in.kenz.travelagency.payment.application.port.PaymentProvider;
import in.kenz.travelagency.payment.domain.entity.Payment;
import org.springframework.stereotype.Component;

@Component
public class RazorpayProvider implements PaymentProvider {

    @Override
    public String getProviderName() {
        return "RAZORPAY";
    }

    @Override
    public PaymentOrderResponse createOrder(Payment payment) {
        // call Razorpay API
        // return gatewayOrderId, redirect info
    }

    @Override
    public void verifyWebhook(WebhookPayload payload) {
        // signature verification
    }

    @Override
    public ProviderPaymentResult fetchPaymentStatus(String gatewayPaymentId) {
        // Razorpay payment fetch API
    }
}