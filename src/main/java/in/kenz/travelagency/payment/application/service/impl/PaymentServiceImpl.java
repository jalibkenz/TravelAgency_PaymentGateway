package in.kenz.travelagency.payment.application.service.impl;

import in.kenz.travelagency.booking.entity.Booking;
import in.kenz.travelagency.booking.enums.BookingStatus;
import in.kenz.travelagency.booking.repository.BookingRepository;
import in.kenz.travelagency.payment.domain.entity.CollectionRequest;
import in.kenz.travelagency.payment.domain.entity.Payment;
import in.kenz.travelagency.payment.domain.enums.PaymentStatus;
import in.kenz.travelagency.payment.infrastructure.repository.CollectionRequestRepository;
import in.kenz.travelagency.payment.infrastructure.repository.PaymentRepository;
import in.kenz.travelagency.payment.application.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final BookingRepository bookingRepository;
    private final CollectionRequestRepository collectionRequestRepository;
    private final PaymentRepository paymentRepository;

    /* ================================
       INITIATE PAYMENT
       ================================ */
    @Override
    public Payment initiatePayment(UUID bookingId, String provider) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        // 🔒 Only CREATED bookings can be paid
        if (booking.getStatus() != BookingStatus.CREATED) {
            throw new IllegalStateException(
                    "Payment not allowed for booking status: " + booking.getStatus()
            );
        }

        // 🔒 Ensure CollectionRequest exists
        CollectionRequest collectionRequest = collectionRequestRepository
                .findByBooking(booking)
                .orElseGet(() ->
                        collectionRequestRepository.save(
                                CollectionRequest.builder()
                                        .booking(booking)
                                        .amountDue(booking.getTourPackage().getPrice())
                                        .currency("INR")
                                        .active(true)
                                        .build()
                        )
                );

        // 🔒 Block retry if merchant may already have money
        boolean blocked = paymentRepository
                .existsByCollectionRequestAndProviderAndStatus(
                        collectionRequest,
                        provider,
                        PaymentStatus.FAILED_MERCHANT
                );

        if (blocked) {
            throw new IllegalStateException(
                    "Payment is under verification. Retry is blocked."
            );
        }

        Payment payment = Payment.builder()
                .collectionRequest(collectionRequest)
                .amount(collectionRequest.getAmountDue())
                .currency(collectionRequest.getCurrency())
                .provider(provider)
                .status(PaymentStatus.CREATED)
                .retryAllowed(true)
                .reconciliationRequired(false)
                .build();

        return paymentRepository.save(payment);
    }

    /* ================================
       PAYMENT RECEIVED
       ================================ */
    @Override
    public void markPaymentReceived(String provider, String gatewayPaymentId) {

        Payment payment = getByGatewayPaymentId(provider, gatewayPaymentId);

        payment.setStatus(PaymentStatus.RECEIVED);
        payment.setRetryAllowed(false);
        payment.setReconciliationRequired(false);

        CollectionRequest collectionRequest = payment.getCollectionRequest();
        collectionRequest.setActive(false);

        Booking booking = collectionRequest.getBooking();
        booking.setStatus(BookingStatus.CONFIRMED);

        paymentRepository.save(payment);
        collectionRequestRepository.save(collectionRequest);
        bookingRepository.save(booking);
    }

    /* ================================
       FAILED - MONEY WITH CUSTOMER
       ================================ */
    @Override
    public void markPaymentFailedCustomer(String provider, String gatewayPaymentId, String reason) {

        Payment payment = getByGatewayPaymentId(provider, gatewayPaymentId);

        payment.setStatus(PaymentStatus.FAILED_CUSTOMER);
        payment.setFailureReason(reason);
        payment.setRetryAllowed(true);
        payment.setReconciliationRequired(false);

        paymentRepository.save(payment);
    }

    /* ================================
       FAILED - MONEY WITH MERCHANT
       ================================ */
    @Override
    public void markPaymentFailedMerchant(String provider, String gatewayPaymentId, String reason) {

        Payment payment = getByGatewayPaymentId(provider, gatewayPaymentId);

        payment.setStatus(PaymentStatus.FAILED_MERCHANT);
        payment.setFailureReason(reason);
        payment.setRetryAllowed(false);
        payment.setReconciliationRequired(true);

        paymentRepository.save(payment);
    }

    /* ================================
       PAYMENT RETURNED (REFUND)
       ================================ */
    @Override
    public void markPaymentReturned(String provider, String gatewayPaymentId) {

        Payment payment = getByGatewayPaymentId(provider, gatewayPaymentId);

        payment.setStatus(PaymentStatus.RETURNED);
        payment.setRetryAllowed(false);
        payment.setReconciliationRequired(false);

        CollectionRequest collectionRequest = payment.getCollectionRequest();
        collectionRequest.setActive(false);

        Booking booking = collectionRequest.getBooking();
        booking.setStatus(BookingStatus.CANCELLED);

        paymentRepository.save(payment);
        collectionRequestRepository.save(collectionRequest);
        bookingRepository.save(booking);
    }

    /* ================================
       HELPER
       ================================ */
    private Payment getByGatewayPaymentId(String provider, String gatewayPaymentId) {
        return paymentRepository.findByProviderAndGatewayPaymentId(provider, gatewayPaymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
    }
}