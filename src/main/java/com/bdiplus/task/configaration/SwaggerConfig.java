package com.bdiplus.task.configaration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("Task Application")
                        .version("1.0")
                        .description("Task assignment for BDIPlus")
                        .contact(new Contact()
                                .name("Sufail Badusha")
                                .email("sufailbadusha@gmail.com"))
                        .license(new License()
                                .name("Under license")));
    }
}
