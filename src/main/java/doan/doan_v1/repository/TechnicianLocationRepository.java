package doan.doan_v1.repository;

import doan.doan_v1.entity.TechnicianLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TechnicianLocationRepository extends JpaRepository<TechnicianLocation, Integer> {
    List<TechnicianLocation> findByTechnicianId(Integer technicianId);
    List<TechnicianLocation> findByLocationId(Integer locationId);
    void deleteByTechnicianId(Integer technicianId);
    boolean existsByTechnicianIdAndLocationId(Integer technicianId, Integer locationId);
} 