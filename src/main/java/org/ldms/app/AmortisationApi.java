package org.ldms.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("org.ldms")
@ComponentScan("org.ldms")
public class AmortisationApi {
    public static void main(String[] args) {
        SpringApplication.run(AmortisationApi.class, args);
    }
}