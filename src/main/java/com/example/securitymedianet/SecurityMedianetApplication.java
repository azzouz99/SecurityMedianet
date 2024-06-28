package com.example.securitymedianet;

import com.example.securitymedianet.Entites.Role;
import com.example.securitymedianet.Repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableAsync
public class SecurityMedianetApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityMedianetApplication.class, args);
    }

    @Bean
    public CommandLineRunner runner(RoleRepository roleRepository) {
        return args -> {
            if (roleRepository.findByName("USER").isEmpty()) {
                roleRepository.save(Role.builder().name("USER").build());
            }
        };
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


}
