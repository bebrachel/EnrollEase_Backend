package ru.nsu.enrollease.configuration;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Collection;
import java.util.Collections;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.nsu.enrollease.service.ColleagueService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    @Autowired
    private ColleagueService colleagueService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .oauth2ResourceServer(oauth -> oauth.jwt(
                jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
            .authorizeHttpRequests(auth -> {
                auth.requestMatchers("/swagger-ui/**").permitAll();
                auth.requestMatchers("/api-docs/**").permitAll();
                auth.requestMatchers("/**").authenticated();
            })
            .csrf(AbstractHttpConfigurer::disable)
            .cors(withDefaults())
            .sessionManagement(a -> a.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .build();
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        class CustomJwtGrantedAuthoritiesConverter implements
            Converter<Jwt, Collection<GrantedAuthority>> {

            @Override
            public Collection<GrantedAuthority> convert(Jwt jwt) {
                String email = (String) jwt.getClaims().get("email");
                System.out.println(email);
                // Проверяем, существует ли email в базе данных
                if (colleagueService.isAllowed(email)) {
                    // роли делать
                    return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
                }
                throw new RuntimeException("Email not found in database");
            }
        }
        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(new CustomJwtGrantedAuthoritiesConverter());
        return jwtConverter;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {

        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry
                    .addMapping("/**")
                    .allowedMethods(CorsConfiguration.ALL)
                    .allowedHeaders(CorsConfiguration.ALL)
                    .allowedOriginPatterns(CorsConfiguration.ALL)
                    .allowedOrigins(CorsConfiguration.ALL);
            }
        };
    }
}
