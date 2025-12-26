package in.kenz.travelagency.payment.application.service;

import in.kenz.travelagency.payment.domain.entity.Payment;

import java.util.UUID;

public interface PaymentService {

    Payment initiatePayment(UUID bookingId);

    void markPaymentReceived(String gatewayPaymentId);

    void markPaymentFailedCustomer(String gatewayPaymentId, String reason);

    void markPaymentFailedMerchant(String gatewayPaymentId, String reason);

    void markPaymentReturned(String gatewayPaymentId);
}