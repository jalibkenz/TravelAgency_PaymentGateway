package in.kenz.travelagency.payment.application.service;



import in.kenz.travelagency.payment.domain.entity.Payment;

import java.util.UUID;

public interface PaymentService {

    /* ================================
       INITIATE PAYMENT
       ================================ */

    Payment initiatePayment(UUID bookingId, String provider);

    /* ================================
       PAYMENT STATE TRANSITIONS
       ================================ */

    void markPaymentReceived(String provider, String gatewayPaymentId);

    void markPaymentFailedCustomer(
            String provider,
            String gatewayPaymentId,
            String reason
    );

    void markPaymentFailedMerchant(
            String provider,
            String gatewayPaymentId,
            String reason
    );

    void markPaymentReturned(String provider, String gatewayPaymentId);
}