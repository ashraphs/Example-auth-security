package com.example.Exampleauthsecurity.configs;

import com.example.Exampleauthsecurity.repositories.impl.MasterEntityRepositoryImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableJpaRepositories(basePackages = {"com.example.Exampleauthsecurity.repositories"}, repositoryBaseClass = MasterEntityRepositoryImpl.class)
public class WebMvcConfig implements WebMvcConfigurer {

    private final static long MAX_AGE_SECS = 3600;

    /**
     * Cross origin sharing resource(CORS) - to allows restricted resources of web page to be requested from another domain outside the domain from which the
     * first resource was served
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("HEAD", "OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE")
                .maxAge(MAX_AGE_SECS);
    }
}
