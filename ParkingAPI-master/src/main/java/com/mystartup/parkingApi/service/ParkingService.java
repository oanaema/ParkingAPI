package com.mystartup.parkingApi.service;

import com.mystartup.parkingApi.model.Parking;

import java.io.IOException;
import java.util.Map;

public interface ParkingService {

  Map<Parking, Double> nearbySearch(String location);
}
