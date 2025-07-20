package com.miapp;

import com.miapp.controller.SetupController;
import com.miapp.repository.ClienteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"com.miapp.model"})
@EnableJpaRepositories(basePackages = {"com.miapp.repository"})
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public CommandLineRunner runSetup(SetupController setupController, ClienteRepository clienteRepository) {
        return args -> {
            if (clienteRepository.count() == 0) {
                setupController.initializeData();
                System.out.println("Datos de ejemplo inicializados automáticamente.");
            }
        };
    }
}