package com.mystartup.parkingApi.service;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.mystartup.parkingApi.model.Geometry;
import com.mystartup.parkingApi.model.Parking;
import com.mystartup.parkingApi.repositories.ParkingRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ParkingServiceImplTest {

    @Mock
    ParkingRepository parkingRepository;
    @Mock
    RestTemplate restTemplate;

    ListAppender<ILoggingEvent> loggingAppender = new ListAppender<>();

    @InjectMocks
    private ParkingService parkingService;

    @BeforeAll
    public void setUp() {
        parkingService = new ParkingServiceImpl(parkingRepository, restTemplate);
        Logger logger = (Logger) LoggerFactory.getLogger(ParkingServiceImpl.class);
        // create and start a ListAppender
        loggingAppender.start();
        // add the appender to the logger
        logger.addAppender(loggingAppender);
    }

    @AfterEach
    public void clearLogger() {
        loggingAppender.list.clear();
    }

    @DisplayName("Test search parking when no Parkings returned from the DB")
    @Test
    void noParkingsRetrieved() {
        when(parkingRepository.findAll()).thenReturn(null);
        assertEquals(Collections.emptyMap(), parkingService.nearbySearch("-33.8670522,151.1957362"));
    }

    @DisplayName("Test nominal nearby search")
    @Test
    void nominalUpdate() {
        Parking parking = new Parking();
        parking.setId(2);
        parking.setNom("Hotel");
        parking.setPlacesRestantes(5);
        parking.setCapacite(100);
        Geometry geometry = new Geometry();
        geometry.setId(1);
        geometry.setType("point");
        geometry.setCoordinates(Arrays.asList(-33.8670522,151.1957362));
        parking.setGeometry(geometry);

        when(parkingRepository.findAll()).thenReturn(Collections.singletonList(parking));

        Map<Parking, Double> resultedParkings = parkingService.nearbySearch("-33.8670522,151.1957362");

        assertEquals(1, resultedParkings.size());
    }
}