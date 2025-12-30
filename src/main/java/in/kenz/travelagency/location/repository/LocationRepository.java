package in.kenz.travelagency.location.repository;

import in.kenz.travelagency.location.entity.Location;
import in.kenz.travelagency.location.entity.LocationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LocationRepository extends JpaRepository<Location, UUID> {

    boolean existsByNameAndTypeAndParent_Id(String name, LocationType type, UUID parentId);

    List<Location> findByType(LocationType type);

    List<Location> findByParent_Id(UUID parentId);


}