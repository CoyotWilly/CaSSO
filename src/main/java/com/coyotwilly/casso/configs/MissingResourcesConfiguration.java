package com.coyotwilly.casso.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
public class MissingResourcesConfiguration {
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer configurer =  new PropertySourcesPlaceholderConfigurer();
        configurer.setIgnoreResourceNotFound(true);

        return configurer;
    }
}
