package doan.doan_v1.repository;

import doan.doan_v1.entity.ComputerDevice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComputerDeviceRepository extends JpaRepository<ComputerDevice, Integer> {

    List<ComputerDevice> findByComputerId(int computerId);

}
