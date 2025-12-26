package in.kenz.travelagency.feedback.entity;

import in.kenz.travelagency.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "feed_back")
@Getter
@Setter
@NoArgsConstructor
public class FeedBack extends BaseEntity {

    @NotBlank
    @Column(nullable = false, length = 500)
    private String message;

    @Min(0)
    @Max(5)
    @Column(nullable = false)
    private int stars;
}