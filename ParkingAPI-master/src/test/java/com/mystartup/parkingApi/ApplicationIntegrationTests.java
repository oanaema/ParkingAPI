package com.mystartup.parkingApi;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(
    locations = "classpath:application-integration.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
class ApplicationIntegrationTests {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MockMvc mvc;

    private MockRestServiceServer mockServer;

    @BeforeAll
    public void init() throws URISyntaxException {
        mockServer = MockRestServiceServer.createServer(restTemplate);

        mockServer.expect(ExpectedCount.once(),
                        requestTo(new URI("https://data.grandpoitiers.fr/api/records/1.0/search/?dataset=mobilite-parkings-grand-poitiers-donnees-metiers&rows=1000&facet=nom_du_parking&facet=zone_tarifaire&facet=statut2&facet=statut3")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON).body(
                                "[\n" +
                                        "    {\n" +
                                        "      \"id\": \"1\",\n" +
                                        "      \"nom\": \"BLOSSAC TISON\",\n" +
                                        "      \"places_restantes\": 454.0,\n" +
                                        "      \"capacite\": 665.0,\n" +
                                        "      \"geometry\": {\n" +
                                        "        \"type\": \"Point\",\n" +
                                        "        \"coordinates\": [\n" +
                                        "          0.337126307916,\n" +
                                        "          46.5750531765\n" +
                                        "        ]\n" +
                                        "      }\n" +
                                        "    },\n" +
                                        "    {\n" +
                                        "      \"id\": \"2\",\n" +
                                        "      \"nom\": \"NOTRE DAME\",\n" +
                                        "      \"places_restantes\": 2.0,\n" +
                                        "      \"capacite\": 65.0,\n" +
                                        "      \"geometry\": {\n" +
                                        "        \"type\": \"Point\",\n" +
                                        "        \"coordinates\": [\n" +
                                        "          0.345002261648,\n" +
                                        "          46.583498748\n" +
                                        "        ]\n" +
                                        "      }\n" +
                                        "    },\n" +
                                        "    {\n" +
                                        "      \"id\": \"3\",\n" +
                                        "      \"nom\": \"HOTEL DE VILLE\",\n" +
                                        "      \"places_restantes\": 254.0,\n" +
                                        "      \"capacite\": 965.0,\n" +
                                        "      \"geometry\":{\n" +
                                        "        \"type\":\"Point\",\n" +
                                        "        \"coordinates\":[\n" +
                                        "          0.338550783802,\n" +
                                        "          46.5793235347\n" +
                                        "        ]\n" +
                                        "      }\n" +
                                        "    }\n" +
                                        "]"));
    }

    @DisplayName("Test businesses endpoint after startup")
    @Test
    public void callNearbySearch() throws Exception {

        mvc.perform(get("/nearbySearch?location=-33.8670522,151.1957362")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
