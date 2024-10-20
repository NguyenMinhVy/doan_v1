package doan.doan_v1.repository;

import doan.doan_v1.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {

    Location findByIdAndDelFlagFalse (int locationId);

    List<Location> findAllByOrderByNameAsc();
}
