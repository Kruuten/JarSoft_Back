package com.kruten.jarsofttesttask.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI().info(new Info().title("Kruten's JarSoft test task CRUD ui").version("0.0.1")
                .contact(new Contact().email("anton.kruten@gmail.com").name("Anton Kruten")));
    }
}
