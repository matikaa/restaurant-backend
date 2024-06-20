package configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
class CorsConfiguration {

    @Value("${management.endpoints.web.cors.mapping-pattern}")
    private String mappingPattern;

    @Value("${management.endpoints.web.cors.allowed-methods}")
    private String allowedMethods;

    @Value("${management.endpoints.web.cors.allowed-headers}")
    private String allowedHeaders;

    @Value("${management.endpoints.web.cors.allowed-origins}")
    private String allowedOrigins;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping(mappingPattern)
                        .allowedMethods(allowedMethods.split(","))
                        .allowedHeaders(allowedHeaders)
                        .allowedOrigins(allowedOrigins);
            }
        };
    }
}