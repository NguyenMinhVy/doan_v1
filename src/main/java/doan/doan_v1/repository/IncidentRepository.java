package doan.doan_v1.repository;

import doan.doan_v1.entity.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
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

}
