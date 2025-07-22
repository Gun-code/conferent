package com.conferent.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI conferentOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8080");
        devServer.setDescription("개발 서버");
        
        Server prodServer = new Server();
        prodServer.setUrl("https://conferent.com");
        prodServer.setDescription("운영 서버");
        
        Contact contact = new Contact();
        contact.setEmail("admin@conferent.com");
        contact.setName("Conferent Team");
        contact.setUrl("https://conferent.com");
        
        License mitLicense = new License()
                .name("MIT License")
                .url("https://choosealicense.com/licenses/mit/");
        
        Info info = new Info()
                .title("Conferent API")
                .version("1.0.0")
                .contact(contact)
                .description("회의실 예약 시스템 API 문서")
                .termsOfService("https://conferent.com/terms")
                .license(mitLicense);
        
        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer, prodServer));
    }
} 