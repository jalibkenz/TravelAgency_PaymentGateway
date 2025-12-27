package in.kenz.travelagency.payment.application.controller;

import in.kenz.travelagency.payment.application.service.PaymentApplicationService;
import in.kenz.travelagency.payment.application.port.dto.PaymentInitiationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentApplicationService paymentApplicationService;

    @PostMapping("/booking/{bookingId}")
    public PaymentInitiationResponse initiatePayment(
            @PathVariable UUID bookingId,
            @RequestParam String provider
    ) {
        return paymentApplicationService.initiatePayment(bookingId, provider);
    }
}