package com.khozzy.isitgood.configuration;

import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RConfiguration {

    @Bean
    public RConnection RConnection() throws RserveException {
        return new RConnection("localhost", 6311);
    }
}
