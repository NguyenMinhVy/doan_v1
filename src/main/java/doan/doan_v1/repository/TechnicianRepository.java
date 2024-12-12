package doan.doan_v1.repository;

import doan.doan_v1.entity.Technician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TechnicianRepository extends JpaRepository<Technician, Integer> {
//    List<Technician> findByLocationId(Integer locationId);

    List<Technician> findAllByDelFlagFalse();

    Technician findByIdAndDelFlagFalse(Integer id);

    Technician findByUserIdAndDelFlagFalse(Integer userId);

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM Technician t " +
            "WHERE t.technicianCode = :technicianCode AND t.delFlag = false")
    boolean existsByTechnicianCodeAndDelFlagFalse(@Param("technicianCode") String technicianCode);

    @Query("SELECT t FROM Technician t JOIN TechnicianLocation tl ON t.id = tl.technicianId " +
           "WHERE tl.locationId = :locationId")
    List<Technician> findByLocationId(@Param("locationId") Integer locationId);
    
    Technician findByUserId(Integer userId);
    Technician findByTechnicianCode(String technicianCode);
    
    @Query("SELECT DISTINCT t FROM Technician t " +
           "JOIN TechnicianLocation tl ON t.id = tl.technicianId " +
           "WHERE tl.locationId IN :locationIds")
    List<Technician> findByLocationIds(@Param("locationIds") List<Integer> locationIds);
}
