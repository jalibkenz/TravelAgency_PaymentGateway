package in.kenz.travelagency.payment.domain.entity;

import in.kenz.travelagency.payment.domain.enums.PaymentStatus;
import in.kenz.travelagency.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "payments",
        indexes = {
                // Used for fast lookup during webhooks and reconciliation
                @Index(name = "idx_provider_gateway_order", columnList = "provider, gateway_order_id"),
                @Index(name = "idx_provider_gateway_payment", columnList = "provider, gateway_payment_id")
        },
        uniqueConstraints = {
                // Ensures only one active payment per provider per collection request per status
                @UniqueConstraint(
                        columnNames = {"collection_request_id", "provider", "status"}
                ),
                // Gateway order ID must be unique per provider
                @UniqueConstraint(
                        name = "uk_provider_gateway_order",
                        columnNames = {"provider", "gatewayOrderId"}
                ),
                // Gateway payment ID must be unique per provider
                @UniqueConstraint(
                        name = "uk_provider_gateway_payment",
                        columnNames = {"provider", "gatewayPaymentId"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment extends BaseEntity {

    /* ========================
       UMBRELLA (COLLECTION REQUEST)
       ======================== */
    // Every payment must belong to a collection request.
    // Payments cannot exist independently.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "collection_request_id", nullable = false)
    private CollectionRequest collectionRequest;

    /* ========================
       MONEY COLLECTED
       ======================== */
    // Amount collected (or attempted) in this payment.
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    // Currency of this payment.
    @Column(nullable = false, length = 3)
    private String currency;

    /* ========================
       PAYMENT STATE
       ======================== */
    // Current state of this payment attempt.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private PaymentStatus status;

    /*
     CREATED
     RECEIVED
     RETURNED
     FAILED_CUSTOMER
     FAILED_MERCHANT
    */

    /* ========================
       PROVIDER / METHOD
       ======================== */
    // Source or channel through which payment is attempted or collected.
    // Examples: RAZORPAY, STRIPE, BANK, CASH
    @Column(nullable = false, length = 50)
    private String provider;

    // Gateway-specific order/intent identifier.
    @Column(name = "gateway_order_id", length = 100)
    private String gatewayOrderId;

    // Gateway-specific payment/charge identifier.
    @Column(name = "gateway_payment_id", length = 100)
    private String gatewayPaymentId;

    /* ========================
       FAILURE / RECONCILIATION
       ======================== */
    // Human-readable explanation for failure, if any.
    @Column(length = 100)
    private String failureReason;

    // Provider-specific error code.
    @Column(length = 50)
    private String gatewayErrorCode;

    // Provider-specific error message.
    @Column(length = 255)
    private String gatewayErrorMessage;

    /* ========================
       CONTROL FLAGS
       ======================== */
    // Indicates whether another payment attempt is allowed.
    @Column(nullable = false)
    private boolean retryAllowed;

    // Indicates whether reconciliation is required
    // (money may be in limbo or with merchant).
    @Column(nullable = false)
    private boolean reconciliationRequired;

    /* ========================
       CONCURRENCY CONTROL
       ======================== */
    // Optimistic locking to prevent concurrent updates
    // from webhooks or reconciliation jobs.
    @Version
    private Long version;
}