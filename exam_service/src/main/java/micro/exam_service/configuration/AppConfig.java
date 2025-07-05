package micro.exam_service.configuration;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    @LoadBalanced // This annotation is crucial for service discovery (e.g., with Eureka)
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}