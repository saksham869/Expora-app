package com.example.demo.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String SECURITY_SCHEME_NAME = "JWT";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // Attach Security Requirement (so swagger-ui knows to use JWT)
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                // Register Security Scheme
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .name(AUTHORIZATION_HEADER)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .info(new Info()
                        .title("Expora : Real experience, shared growth")
                        .version("1.0")
                        .description("This project is developed by Satyam Mishra")
                        .contact(new Contact()
                                .name("Satyam Mishra")
                                .url("https://Expora.com")
                                .email("expora@gmail.com"))
                        .license(new License()
                                .name("API License")
                                .url("API License URL")))
                .externalDocs(new ExternalDocumentation()
                        .description("Project Documentation")
                        .url("https://yourdocumentation.url"));
    }
}
