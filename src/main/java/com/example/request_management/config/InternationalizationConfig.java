package com.example.request_management.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

@Configuration
public class InternationalizationConfig {

    @Bean
    public ResourceBundleMessageSource messageSource() {

        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasename("messages/labels");
        source.setUseCodeAsDefaultMessage(true);
        source.setDefaultEncoding("UTf-8");
        return source;
    }
    @Bean
    public LocaleResolver locale() {
        return new FixedLocaleResolver();
    }

}
