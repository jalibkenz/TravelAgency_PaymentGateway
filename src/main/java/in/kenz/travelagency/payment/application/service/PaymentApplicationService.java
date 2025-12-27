package in.kenz.travelagency.payment.application.service;

import in.kenz.travelagency.booking.entity.Booking;
import in.kenz.travelagency.booking.repository.BookingRepository;
import in.kenz.travelagency.payment.application.port.PaymentProvider;
import in.kenz.travelagency.payment.application.port.dto.PaymentInitiationResponse;
import in.kenz.travelagency.payment.application.port.dto.PaymentOrderResponse;
import in.kenz.travelagency.payment.application.port.dto.WebhookPayload;
import in.kenz.travelagency.payment.application.registry.PaymentProviderRegistry;
import in.kenz.travelagency.payment.domain.entity.CollectionRequest;
import in.kenz.travelagency.payment.domain.entity.Payment;
import in.kenz.travelagency.payment.domain.enums.PaymentStatus;
import in.kenz.travelagency.payment.infrastructure.repository.CollectionRequestRepository;
import in.kenz.travelagency.payment.infrastructure.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentApplicationService {

    private final BookingRepository bookingRepository;
    private final CollectionRequestRepository collectionRequestRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentProviderRegistry providerRegistry;

    @Transactional
    public PaymentInitiationResponse initiatePayment(UUID bookingId, String provider) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        CollectionRequest collectionRequest = collectionRequestRepository
                .findByBooking(booking)
                .orElseGet(() ->
                        collectionRequestRepository.save(
                                CollectionRequest.builder()
                                        .booking(booking)
                                        .amountDue(
                                                booking.getTourPackage().getPrice()
                                                        .multiply(BigDecimal.valueOf(booking.getTravelers()))
                                        )
                                        .currency("INR")
                                        .active(true)
                                        .build()
                        )
                );

        Payment payment = paymentRepository.save(
                Payment.builder()
                        .collectionRequest(collectionRequest)
                        .amount(collectionRequest.getAmountDue())
                        .currency(collectionRequest.getCurrency())
                        .status(PaymentStatus.CREATED)
                        .provider(provider.toUpperCase())
                        .retryAllowed(true)
                        .reconciliationRequired(false)
                        .build()
        );

        PaymentProvider paymentProvider = providerRegistry.get(payment.getProvider());
        PaymentOrderResponse orderResponse = paymentProvider.createOrder(payment);

        return PaymentInitiationResponse.builder()
                .bookingId(booking.getId())
                .collectionRequestId(collectionRequest.getId())
                .provider(payment.getProvider())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .gatewayOrderId(orderResponse.getGatewayOrderId())
                .redirectUrl(orderResponse.getRedirectUrl())
                .build();
    }

    /**
     * LOW-LEVEL INTERNAL API
     */
    @Transactional
    protected PaymentOrderResponse createPayment(Payment payment) {
        Payment saved = paymentRepository.save(payment);
        PaymentProvider provider = providerRegistry.get(saved.getProvider());
        return provider.createOrder(saved);
    }

    /**
     * WEBHOOK ENTRY
     */
    @Transactional
    public void handleWebhook(WebhookPayload payload) {
        PaymentProvider provider = providerRegistry.get(payload.getProvider());
        provider.verifyWebhook(payload);
        // TODO: update Payment + CollectionRequest
    }
}