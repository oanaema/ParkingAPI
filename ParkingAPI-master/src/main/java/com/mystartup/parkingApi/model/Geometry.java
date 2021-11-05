package com.mystartup.parkingApi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "geometry")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Geometry {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @JsonProperty
    private String type;
    @JsonProperty
    @ElementCollection(targetClass=Double.class)
    private List<Double> coordinates;

}
