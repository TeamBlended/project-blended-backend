package com.gdsc.blended;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BlendedApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlendedApplication.class, args);
    }

}
