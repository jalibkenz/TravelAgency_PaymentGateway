package in.kenz.travelagency.payment.domain.entity;


import in.kenz.travelagency.booking.entity.Booking;
import in.kenz.travelagency.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "collection_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollectionRequest extends BaseEntity {

    /* ========================
       BUSINESS ANCHOR
       ======================== */
    // The booking for which money is owed.
    // A CollectionRequest cannot exist without a Booking.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    /* ========================
       MONEY OWED
       ======================== */
    // Total amount that needs to be collected for this booking.
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amountDue;

    // Currency of the amount owed (INR, USD, etc.)
    @Column(nullable = false, length = 3)
    private String currency;

    /* ========================
       STATE
       ======================== */
    // Indicates whether this collection request is still active.
    // Becomes false once fully collected or cancelled.
    @Column(nullable = false)
    private boolean active;

    /* ========================
       PAYMENTS
       ======================== */

    // All payments (cash, wire, gateway, retries, partials)
    // made under this collection request.
    @OneToMany(mappedBy = "collectionRequest", fetch = FetchType.LAZY)
    private List<Payment> payments;
}