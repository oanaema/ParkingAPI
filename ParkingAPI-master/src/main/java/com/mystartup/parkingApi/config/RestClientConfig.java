package com.mystartup.parkingApi.config;

import org.apache.http.HttpResponse;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultServiceUnavailableRetryStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class RestClientConfig {

  @Value("${http.client.retry.maxCount}")
  private int maxRetryCount;

  @Value("${http.client.retry.intervalSeconds}")
  private int intervalSeconds;

  @Value("${http.client.ssl.trustStorePassword}")
  private String trustStorePassword;

  @Value("${http.client.ssl.trustStore}")
  private String trustStore;

  @Bean
  public RestTemplate restTemplate() {

    System.setProperty("javax.net.ssl.trustStore", trustStore);
    System.setProperty("javax.net.ssl.trustStorePassword", trustStorePassword);
    System.setProperty("javax.net.ssl.keyStore", trustStore);
    System.setProperty("javax.net.ssl.keyStorePassword", trustStorePassword);

    CloseableHttpClient httpClient = HttpClients.custom()
        .setServiceUnavailableRetryStrategy(getServiceUnavailStrategy())
        .build();

    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();

    requestFactory.setHttpClient(httpClient);
    requestFactory.setConnectTimeout(30_000);
    requestFactory.setConnectionRequestTimeout(30_000);
    RestTemplate restTemplate = new RestTemplate(requestFactory);
    restTemplate.getMessageConverters().add(0, mappingJacksonHttpMessageConverter());

    return restTemplate;
  }

  @Bean
  public MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter() {
    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
    converter.setObjectMapper(objectMapper());
    return converter;
  }

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);
    return mapper;
  }

  private DefaultServiceUnavailableRetryStrategy getServiceUnavailStrategy() {
    Args.notNegative(maxRetryCount, "Max retry count");
    Args.notNegative(intervalSeconds, "Interval time in seconds");
    return new DefaultServiceUnavailableRetryStrategy(maxRetryCount, intervalSeconds * 1000) {
      @Override
      public boolean retryRequest(HttpResponse response, int executionCount, HttpContext context) {
        Boolean retry = executionCount <= maxRetryCount &&
            response.getStatusLine().getStatusCode() == HttpStatus.TOO_MANY_REQUESTS.value();
        if (retry) {
          log.info(String.format("Retrying request %s because %s, wait: %s , retryCount: %s",
                                 ((HttpClientContext)context).getRequest().getRequestLine(),
                                 response.getStatusLine().getStatusCode(), getRetryInterval(),
                                 executionCount));
        }
        return retry;
      }
    };
  }
}