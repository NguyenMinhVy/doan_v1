package doan.doan_v1.repository;

import doan.doan_v1.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Integer> {

//    List<Device> findByComputerIdAndDelFlagFalse(int computerId);

    List<Device> findByDelFlagFalse();

}
