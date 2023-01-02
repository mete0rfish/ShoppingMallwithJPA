package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableJpaAuditing // JPA의 Auditing 기능 활성화
public class AuditConfig {

    @Bean
    public AuditorAware<String> auditorProvider(){ // AuditorAware을 빈으로 등록
        return new AuditorAwareImpl();
    }
}
