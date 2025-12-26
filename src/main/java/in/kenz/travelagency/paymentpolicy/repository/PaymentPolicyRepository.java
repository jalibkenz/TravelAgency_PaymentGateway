package in.kenz.travelagency.paymentpolicy.repository;

import in.kenz.travelagency.paymentpolicy.entity.PaymentPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentPolicyRepository extends JpaRepository<PaymentPolicy, Long> {
}
