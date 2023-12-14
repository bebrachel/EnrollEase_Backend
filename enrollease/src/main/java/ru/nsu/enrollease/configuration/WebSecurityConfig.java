package ru.nsu.enrollease.configuration;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Collection;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.nsu.enrollease.service.ColleagueService;
import ru.nsu.enrollease.service.MyUserDetailsService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final ColleagueService colleagueService;
    private final MyUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .oauth2ResourceServer(oauth -> oauth.jwt(
                jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
            .authorizeHttpRequests(auth -> {
                auth.requestMatchers("/swagger-ui/**").permitAll();
                auth.requestMatchers("/api-docs/**").permitAll();
                auth.requestMatchers("/admin/**").hasAuthority("HEAD_OF_COMMISSION");
                auth.requestMatchers("/**").hasAuthority("DEFAULT_COLLEAGUE");
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
                // Проверяем, существует ли email в базе данных
                if (colleagueService.isAllowed(email)) {
                    // роли делать
                    return (Collection<GrantedAuthority>) userDetailsService.loadUserByUsername(email).getAuthorities();
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

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        String hierarchy = "HEAD_OF_COMMISSION > DEFAULT_COLLEAGUE";
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }

    @Bean
    public DefaultWebSecurityExpressionHandler customWebSecurityExpressionHandler() {
        DefaultWebSecurityExpressionHandler expressionHandler =
            new DefaultWebSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy());
        return expressionHandler;
    }

}
