package com.example.backendstreaming.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> {}) // habilita CORS
                .csrf(AbstractHttpConfigurer::disable) // desactiva CSRF
                .authorizeHttpRequests(auth -> auth
                        // 🚀 Endpoints públicos
                        .requestMatchers("/api/songs").permitAll()           // lista de canciones
                        .requestMatchers("/api/songs/*/stream").permitAll()  // streaming libre
                        // 🔒 Todo lo demás requiere login
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults()); // autenticación básica (puedes cambiarla)

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:4200"); // frontend Angular
        config.addAllowedMethod("*");                     // GET, POST, etc.
        config.addAllowedHeader("*");                     // headers permitidos

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);  // aplica a todos los endpoints
        return source;
    }
}