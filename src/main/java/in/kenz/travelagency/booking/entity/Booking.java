package in.kenz.travelagency.booking.entity;

import in.kenz.travelagency.booking.enums.BookingStatus;
import in.kenz.travelagency.common.entity.BaseEntity;

import in.kenz.travelagency.tourpackage.entity.TourPackage;
import in.kenz.travelagency.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_package_id", nullable = false)
    private TourPackage tourPackage;

    @Column(nullable = false)
    private LocalDate travelDate;

    @Column(nullable = false)
    private int travelers;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BookingStatus status;

    @Column(nullable = false)
    private boolean active = true;
}