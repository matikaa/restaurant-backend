package configuration;

import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    private final RestaurantCorsConfiguration cors;

    public AppConfig(RestaurantCorsConfiguration cors) {
        this.cors = cors;
    }
}
