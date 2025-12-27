package in.kenz.travelagency.payment.infrastructure.repository;


import in.kenz.travelagency.payment.domain.entity.CollectionRequest;
import in.kenz.travelagency.payment.domain.entity.Payment;
import in.kenz.travelagency.payment.domain.enums.PaymentStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    /* ---------------------------------
       CollectionRequest-level guard
       --------------------------------- */

    boolean existsByCollectionRequestAndProviderAndStatus(
            CollectionRequest collectionRequest,
            String provider,
            PaymentStatus status
    );

    boolean existsByProviderAndGatewayOrderId(
            String provider,
            String gatewayOrderId
    );

    boolean existsByProviderAndGatewayPaymentId(
            String provider,
            String gatewayPaymentId
    );

    /* ---------------------------------
       Provider-scoped gateway lookups
       --------------------------------- */

    Optional<Payment> findByProviderAndGatewayPaymentId(
            String provider,
            String gatewayPaymentId
    );

    Optional<Payment> findByProviderAndGatewayOrderId(
            String provider,
            String gatewayOrderId
    );

    /* ---------------------------------
       Concurrency-safe (webhooks)
       --------------------------------- */

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        select p from Payment p
        where p.provider = :provider
          and p.gatewayPaymentId = :gatewayPaymentId
    """)
    Optional<Payment> findForUpdateByProviderAndGatewayPaymentId(
            @Param("provider") String provider,
            @Param("gatewayPaymentId") String gatewayPaymentId
    );
}