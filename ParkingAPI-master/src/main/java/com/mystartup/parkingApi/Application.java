package com.mystartup.parkingApi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mystartup.parkingApi.model.Parking;
import com.mystartup.parkingApi.repositories.ParkingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@SpringBootApplication
@Slf4j
@EnableAsync
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    RestTemplate restTemplate;

    @Value("${parkingVille.api}")
    private String parkingVilleApi;

    @Bean
    CommandLineRunner initialize(ParkingRepository parkingRepository) {
        return args -> {
            ObjectMapper mapper = new ObjectMapper();
            TypeReference<List<Parking>> mapType = new TypeReference<List<Parking>>() {
            };
            InputStream is = TypeReference.class.getResourceAsStream("/parking.json");
            try {
                //Recuper la liste de parkings depuis le URL de ville et persist dans la base de donne H2.
                //List<Parking> parking = restTemplate.getForObject(parkingVilleApi, ArrayList<Parking>.class);
                //TODO: Trouver solution pour mapper dynamically les objets JSON.
                List<Parking> parkingList = mapper.readValue(is, mapType);
                parkingRepository.saveAll(parkingList);
                log.info("Parking list saved successfully");
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } catch (HttpStatusCodeException ex) {
                log.error(String.format(ex.getMessage()));
            } catch (RestClientException ex) {
                log.error("Problem connection " + String.format(ex.getLocalizedMessage()));
            }
        };
    }
}
