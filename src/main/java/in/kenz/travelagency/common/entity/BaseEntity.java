package in.kenz.travelagency.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@MappedSuperclass
@Getter
public abstract class BaseEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(
            columnDefinition = "BINARY(16)",
            updatable = false,
            nullable = false
    )
    private UUID id;
}