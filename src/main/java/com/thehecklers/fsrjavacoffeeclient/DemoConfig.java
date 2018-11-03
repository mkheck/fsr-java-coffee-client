package com.thehecklers.fsrjavacoffeeclient;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class DemoConfig {
    @Bean
    WebClient client() {
        // Points to Java-based coffee service configured to run on 8080
        return WebClient.create("http://localhost:8080/coffees");
    }

    @Bean
    CommandLineRunner demo(WebClient client) {
        return args -> client.get()
                .retrieve()
                .bodyToFlux(Coffee.class)
                .filter(coffee -> coffee.getName().equalsIgnoreCase("kaldi's coffee"))
                .flatMap(coffee -> client.get()
                        .uri("/{id}/orders", coffee.getId())
                        .retrieve()
                        .bodyToFlux(CoffeeOrder.class))
                .subscribe(System.out::println);
    }
}
