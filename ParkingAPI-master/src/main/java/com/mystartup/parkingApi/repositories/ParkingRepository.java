package com.mystartup.parkingApi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.mystartup.parkingApi.model.Parking;

@RepositoryRestResource(collectionResourceRel = "parkings", path = "parkings")
public interface ParkingRepository extends JpaRepository<Parking, Long> {
}
