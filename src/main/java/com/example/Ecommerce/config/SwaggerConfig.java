package com.example.Ecommerce.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI openAPI() {
    Info info = new Info()
        .title("ZB E-Commerce API Document")
        .version("1.0.0")
        .description("ZB E-Commerce API 명세서입니다.");
    return new OpenAPI()
        .components(new Components())
        .info(info);
  }

}
