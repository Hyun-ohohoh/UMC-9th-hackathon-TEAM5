package com.example.demo.domain.room.repository;


import com.example.demo.domain.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query(value = "SELECT * FROM rooms r WHERE ST_Distance_Sphere(point(r.longitude, r.latitude), point(:lon, :lat)) <= :radius", nativeQuery = true)
    List<Room> findRoomsWithinRadius(@Param("lon") double lon, @Param("lat") double lat, @Param("radius") double radius);
}
