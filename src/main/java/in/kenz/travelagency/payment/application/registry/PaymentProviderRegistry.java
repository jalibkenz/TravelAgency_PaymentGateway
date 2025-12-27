package in.kenz.travelagency.payment.application.registry;

import in.kenz.travelagency.payment.application.port.PaymentProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class PaymentProviderRegistry {

    private final Map<String, PaymentProvider> providers;

    public PaymentProvider get(String providerName) {
        PaymentProvider provider = providers.get(providerName);
        if (provider == null) {
            throw new IllegalArgumentException(
                    "Unsupported payment provider: " + providerName
            );
        }
        return provider;
    }
}