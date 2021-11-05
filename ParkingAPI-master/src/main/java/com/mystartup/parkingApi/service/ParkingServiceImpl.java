package com.mystartup.parkingApi.service;

import com.mystartup.parkingApi.model.Parking;
import com.mystartup.parkingApi.repositories.ParkingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@Slf4j
@Transactional
public class ParkingServiceImpl implements ParkingService {

  ParkingRepository parkingRepository;

  RestTemplate restTemplate;

  public ParkingServiceImpl(final ParkingRepository parkingRepository, final RestTemplate restTemplate) {
    this.parkingRepository = parkingRepository;
    this.restTemplate = restTemplate;
  }

  @Override
  public Map<Parking, Double> nearbySearch(String location){
    Double lat = Double.valueOf(location.substring(0, location.indexOf(",")));
    Double longitude = Double.valueOf(location.substring(location.indexOf(",")+1));

    List<Parking> configuredParkings = parkingRepository.findAll();
    if(configuredParkings == null || configuredParkings.isEmpty()){
      return Collections.emptyMap();
    }
    Map<Parking, Double> computedDistances = new HashMap<>();
    for(Parking p : configuredParkings){
      computedDistances.put(p, distance(p.getGeometry().getCoordinates().get(0), p.getGeometry().getCoordinates().get(0), lat, longitude));
    }
    //LinkedHashMap preserve the ordering of elements in which they are inserted
    LinkedHashMap<Parking, Double> sortedParkings = new LinkedHashMap<>();
    computedDistances.entrySet()
            .stream()
            .sorted(Map.Entry.comparingByValue())
            .forEachOrdered(x -> sortedParkings.put(x.getKey(), x.getValue()));
    return sortedParkings;
  }

  /**
   * Calcul distance entre 2 points.
   * @param lat1
   * @param lon1
   * @param lat2
   * @param lon2
   * @return
   */
  private static double distance(double lat1, double lon1, double lat2, double lon2) {
    if ((lat1 == lat2) && (lon1 == lon2)) {
      return 0;
    }
    else {
      double theta = lon1 - lon2;
      double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
      dist = Math.acos(dist);
      dist = Math.toDegrees(dist);
      dist = dist * 60 * 1.1515;
      dist = dist * 1.609344;

      return (dist);
    }
  }
}
