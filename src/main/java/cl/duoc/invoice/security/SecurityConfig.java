/*
 * Copyright © 2026 DuocUC FullStack 1
 * Eduardo Bray
 * Rodrigo Callealta
 * Fernando Villalobos
 */
package cl.duoc.invoice.security;

import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private static final String[] SWAGGER_PATHS = {
        "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs", "/v3/api-docs/**"
    };

    private static final String INVOICES_PATH = "/api/v1/invoices";
    private static final String ADMIN_ROLE = "admin";
    private static final String SELLER_ROLE = "vendedor";

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Value("${jwt.public}")
    private String[] publicPaths;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.dispatcherTypeMatchers(DispatcherType.ERROR)
                        .permitAll()
                        .requestMatchers(SWAGGER_PATHS)
                        .permitAll()
                        .requestMatchers(publicPaths)
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, INVOICES_PATH)
                        .authenticated()
                        .requestMatchers(HttpMethod.GET, INVOICES_PATH + "/sale/*")
                        .authenticated()
                        .requestMatchers(HttpMethod.GET, INVOICES_PATH, INVOICES_PATH + "/*")
                        .hasAnyAuthority(ADMIN_ROLE, SELLER_ROLE)
                        .requestMatchers(HttpMethod.PUT, INVOICES_PATH + "/*/anular")
                        .hasAuthority(ADMIN_ROLE)
                        .anyRequest()
                        .authenticated())
                .exceptionHandling(ex -> ex.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
