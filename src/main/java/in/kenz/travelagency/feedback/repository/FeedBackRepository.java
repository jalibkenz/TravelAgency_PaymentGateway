package in.kenz.travelagency.feedback.repository;

import in.kenz.travelagency.feedback.entity.FeedBack;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedBackRepository  extends JpaRepository<FeedBack, Long> {
}
