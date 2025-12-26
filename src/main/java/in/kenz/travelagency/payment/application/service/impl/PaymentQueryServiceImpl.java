package in.kenz.travelagency.payment.application.service.impl;

import in.kenz.travelagency.booking.entity.Booking;
import in.kenz.travelagency.booking.enums.BookingStatus;
import in.kenz.travelagency.booking.repository.BookingRepository;
import in.kenz.travelagency.payment.application.service.PaymentQueryService;
import in.kenz.travelagency.payment.domain.entity.CollectionRequest;
import in.kenz.travelagency.payment.domain.entity.Payment;
import in.kenz.travelagency.payment.domain.enums.PaymentStatus;
import in.kenz.travelagency.payment.infrastructure.repository.CollectionRequestRepository;
import in.kenz.travelagency.payment.infrastructure.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentQueryServiceImpl implements PaymentQueryService {

    private final BookingRepository bookingRepository;
    private final CollectionRequestRepository collectionRequestRepository;
    private final PaymentRepository paymentRepository;

    /* ================================
       CAN PAYMENT BE MADE?
       ================================ */
    @Override
    public boolean isPaymentAllowed(UUID bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        // Booking must be payable
        if (booking.getStatus() != BookingStatus.CREATED) {
            return false;
        }

        CollectionRequest collectionRequest =
                collectionRequestRepository.findByBookingAndActiveTrue(booking)
                        .orElse(null);

        if (collectionRequest == null) {
            return false;
        }

        // Block payment if any payment is under merchant-side reconciliation
        boolean blocked = paymentRepository
                .existsByCollectionRequestAndProviderAndStatus(
                        collectionRequest,
                        "ANY",
                        PaymentStatus.FAILED_MERCHANT
                );

        if (blocked) {
            return false;
        }

        // Check if amount is still due
        BigDecimal collected = getAmountCollected(collectionRequest);
        return collected.compareTo(collectionRequest.getAmountDue()) < 0;
    }

    /* ================================
       AMOUNT DUE
       ================================ */
    @Override
    public BigDecimal getAmountDue(UUID bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        CollectionRequest collectionRequest =
                collectionRequestRepository.findByBookingAndActiveTrue(booking)
                        .orElseThrow(() -> new IllegalStateException("No active collection request"));

        BigDecimal collected = getAmountCollected(collectionRequest);
        BigDecimal due = collectionRequest.getAmountDue().subtract(collected);

        return due.max(BigDecimal.ZERO);
    }

    /* ================================
       INTERNAL HELPERS
       ================================ */
    private BigDecimal getAmountCollected(CollectionRequest collectionRequest) {

        List<Payment> payments =
                collectionRequest.getPayments();

        if (payments == null || payments.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return payments.stream()
                .filter(p -> p.getStatus() == PaymentStatus.RECEIVED)
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}