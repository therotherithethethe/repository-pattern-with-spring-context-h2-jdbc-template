package com.arakviel.config;

import com.arakviel.persistance.entity.Parrot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.arakviel")
public class ApplicationConfig {

    @Bean
    protected Parrot boklah() {
        return new Parrot("Боклах попугай");
    }
}
