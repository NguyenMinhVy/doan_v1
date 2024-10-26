package doan.doan_v1.repository;

import doan.doan_v1.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {

    List<Room> findByDelFlagFalse();

    Room findRoomByNameAndDelFlagFalse(String name);
}
