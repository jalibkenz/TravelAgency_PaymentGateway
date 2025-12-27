package in.kenz.travelagency.payment.infrastructure.web;

import in.kenz.travelagency.payment.application.port.dto.WebhookPayload;
import in.kenz.travelagency.payment.application.service.PaymentApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/webhooks/payments")
@RequiredArgsConstructor
public class PaymentWebhookController {

    private final PaymentApplicationService service;

    @PostMapping("/{provider}")
    public void receiveWebhook(
            @PathVariable String provider,
            @RequestBody String payload,
            @RequestHeader Map<String, String> headers
    ) {
        service.handleWebhook(
                WebhookPayload.builder()
                        .provider(provider.toUpperCase())
                        .payload(payload)
                        .headers(headers)
                        .build()
        );
    }
}