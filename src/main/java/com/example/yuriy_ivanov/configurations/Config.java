package com.example.yuriy_ivanov.configurations;

import com.example.yuriy_ivanov.services.Converter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public Converter converter(ObjectMapper objectMapper) {
       return new Converter(objectMapper);
    }

}
