package com.taskflowx.config;

import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

  @Bean
  public CorsFilter corsFilter() {
    CorsConfiguration config = new CorsConfiguration();

    // Allow credentials (cookies, authorization headers)
    config.setAllowCredentials(true);

    // Allow specific origins (frontend URLs)
    config.setAllowedOrigins(Arrays.asList(
      "http://localhost:3000",      // React default
      "http://localhost:4200",      // Angular default
      "http://localhost:5173",      // Vite default
      "http://localhost:8081"       // Alternative port
    ));

    // Allow all headers
    config.addAllowedHeader("*");

    // Allow specific HTTP methods
    config.setAllowedMethods(Arrays.asList(
      "GET",
      "POST",
      "PUT",
      "DELETE",
      "OPTIONS"
    ));

    // Expose Authorization header to frontend
    config.setExposedHeaders(Arrays.asList("Authorization"));

    // Apply CORS configuration to all endpoints
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);

    return new CorsFilter(source);
  }
}