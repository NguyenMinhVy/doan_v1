package doan.doan_v1.repository;

import doan.doan_v1.entity.Technician;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TechnicianRepository extends JpaRepository<Technician, Integer> {

    List<Technician> findByLocationId(Integer locationId);

    List<Technician> findByDelFlagFalse();

    Technician findByIdAndDelFlagFalse(Integer id);
}
