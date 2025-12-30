package in.kenz.travelagency.user.repository;

import in.kenz.travelagency.user.dto.UserProfileResponse;
import in.kenz.travelagency.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    List<UserProfileResponse> findAllUsers();

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    boolean existsByEmailAndIdNot(String email, UUID id);
    boolean existsByUsernameAndIdNot(String username, UUID id);

}