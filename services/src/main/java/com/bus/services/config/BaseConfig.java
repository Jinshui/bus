package com.bus.services.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * BaseConfig
 * @author <a href="mailto:jeffb@thistech.com">Jeff Bailey</a>
 */
@Configuration
public class BaseConfig {

    @Bean
    public static PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
        PropertyPlaceholderConfigurer placeholderConfigurer = new PropertyPlaceholderConfigurer();
        placeholderConfigurer.setIgnoreResourceNotFound(true);
        placeholderConfigurer.setSystemPropertiesMode(PropertyPlaceholderConfigurer.SYSTEM_PROPERTIES_MODE_OVERRIDE);
        placeholderConfigurer.setLocations(new Resource[]{
                new ClassPathResource("/config-defaults.properties"),
                new ClassPathResource("/config.properties"),
                new ClassPathResource("/config-test-defaults.properties"),
                new ClassPathResource("/config-test.properties"),
        });
        placeholderConfigurer.setOrder(1);
        return placeholderConfigurer;
    }
}
