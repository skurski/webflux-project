package demo.webflux.client;

import demo.webflux.server.entity.Motorcycle;
import demo.webflux.server.entity.Specification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Stream;

/**
 * First start Server APP before running Client APP
 */
@SpringBootApplication
public class ClientApplication {

    private static final Logger logger = LoggerFactory.getLogger(ClientApplication.class);

    private static final String serverUrl = "http://localhost:8081";

    private static WebClient client = WebClient.create(serverUrl);

    private static RestTemplate restTemplate = new RestTemplate();

    static {
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(serverUrl));
    }

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);

//        restTemplateApproach();
        webClientApproach();
    }

    private static void webClientApproach() {
        Instant start = Instant.now();

//        retrieveExample();
//        exchangeExample();
//        nestedRequest();
        streamRequest();

        logTime(start);
    }

    private static void restTemplateApproach() {
        Instant start = Instant.now();

        singleRequestByRestTemplate();
        nestedRequestByRestTemplate();

        logTime(start);
    }

    private static void singleRequestByRestTemplate() {
        Stream.iterate(1, n -> n + 1)
                .limit(4)
                .forEach(n -> restTemplate.getForObject("/motorcycle/{id}", Motorcycle.class, n));
    }

    private static void nestedRequestByRestTemplate() {
        Stream.iterate(1, n -> n + 1)
                .limit(4)
                .forEach(n -> {
                    Motorcycle moto = restTemplate.getForObject("/motorcycle/{id}", Motorcycle.class, n);
                    restTemplate.getForObject("/motorcycle/{id}/specification", Specification.class, moto.getId());
                });
    }


    private static void retrieveExample() {
        Flux.range(1, 4)
                .flatMap(i -> client.get().uri("/motorcycle/{id}", i)
                        .retrieve()
                        .bodyToMono(Motorcycle.class))
                .blockLast();
    }

    private static void exchangeExample() {
        Flux.range(1, 4)
                .flatMap(i ->
                        client.get()
                                .uri("/motorcycle/{id}", i)
                                .exchange()
                                .flatMap(response -> {
                                    if (response.statusCode() == HttpStatus.OK) {
                                        return response.bodyToMono(Motorcycle.class);
                                    } else {
                                        return response.releaseBody();
                                    }
                                }))
                .doOnNext(p -> System.out.println(p))
                .blockLast();
    }

    private static void streamRequest() {
        client.get().uri("/motorcycles/stream")
                .retrieve()
                .bodyToFlux(Motorcycle.class)
                .take(4)
                .blockLast();
    }

    private static void nestedRequest() {
        Flux.range(1, 4)
                .flatMap(i -> client.get().uri("/motorcycle/{id}", i)
                        .retrieve()
                        .bodyToMono(Motorcycle.class)
                        .flatMap(person -> client.get().uri("/motorcycle/{id}/specification", i)
                                .retrieve()
                                .bodyToMono(Specification.class)))
                .blockLast();
    }

    private static void logTime(Instant start) {
        logger.debug("Elapsed time: " + Duration.between(start, Instant.now()).toMillis() + " ms");
    }

}
