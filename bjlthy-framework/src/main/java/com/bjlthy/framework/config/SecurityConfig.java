//package com.bjlthy.framework.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
//import org.springframework.security.config.web.server.ServerHttpSecurity;
//import org.springframework.security.web.server.SecurityWebFilterChain;
//
//import java.time.Duration;
//
//@Configuration
//@EnableWebFluxSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) throws Exception {
//        http
//                // ...其他配置
//                .headers()
//                .contentSecurityPolicy("default-src 'self'; script-src 'self' 'unsafe-inline'")
//                .httpStrictTransportSecurity()
//                .includeSubDomains(true)
//                .maxAge(Duration.ofDays(365))
//                .and()
//                .xssProtection()
//                .and()
//                .frameOptions()
//                .deny()
//                .and()
//        // ...其他配置
//        ;
//
//        return http.build();
//    }
//}
