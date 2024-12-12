package doan.doan_v1.repository;

import doan.doan_v1.entity.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Integer> {

    List<Incident> findByComputerDeviceIdAndDelFlagFalse(int deviceId);

    List<Incident> findByComputerSoftwareIdAndDelFlagFalse(int softwareId);

    List<Incident> findByComputerIdAndDelFlagFalse(int softwareId);

    List<Incident> findAllByComputerIdAndDelFlagFalse(int computerId);

    Optional<Incident> findTopByComputerDeviceIdOrderByReportDateDesc(int computerDeviceId);

    Optional<Incident> findTopByComputerSoftwareIdOrderByReportDateDesc(int computerSoftwareId);

    void deleteByComputerId(Integer computerId);

    List<Incident> findByLocationId(Integer locationId);

    List<Incident> findByTechnicianId(Integer technicianId);

    @Query("SELECT i FROM Incident i WHERE i.locationId = :locationId AND i.technicianId = :technicianId")
    List<Incident> findByLocationIdAndTechnicianId(@Param("locationId") Integer locationId, 
                                                  @Param("technicianId") Integer technicianId);

    @Query("SELECT i FROM Incident i WHERE i.technicianId IN " +
           "(SELECT tl.technicianId FROM TechnicianLocation tl WHERE tl.locationId = :locationId)")
    List<Incident> findByTechnicianLocation(@Param("locationId") Integer locationId);
}
