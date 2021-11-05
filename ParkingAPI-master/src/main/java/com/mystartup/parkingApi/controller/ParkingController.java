package com.mystartup.parkingApi.controller;

import com.mystartup.parkingApi.model.Parking;
import com.mystartup.parkingApi.service.ParkingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
public class ParkingController {

    ParkingService parkingService;

    public ParkingController(final ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    @GetMapping(value = "/nearbySearch", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<?> nearbySearch(@RequestParam(name = "location", required = true) String location) throws IOException {
        if (location.matches("^-[0-9,.]+$")) {
            Map<Parking, Double> parkings = parkingService.nearbySearch(location);
            return new ResponseEntity<>(parkings, HttpStatus.OK);
        }
        return new ResponseEntity<>("No valid format for location. It should contain digits, one comma and dots.", HttpStatus.BAD_REQUEST);

    }
}