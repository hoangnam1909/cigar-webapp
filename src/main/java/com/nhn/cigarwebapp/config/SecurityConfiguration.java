package com.nhn.cigarwebapp.config;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(request -> {
                    request.requestMatchers("/api/v1/auth/**").permitAll();
                    request.requestMatchers(HttpMethod.GET,
                                    "/api/v1/categories**",
                                    "/api/v1/categories/**")
                            .permitAll();
                    request.requestMatchers(
                                    "/api/v1/customers**",
                                    "/api/v1/customers/**",
                                    "/api/v1/customers/validate/**")
                            .permitAll();
                    request.requestMatchers(
                                    "/api/v1/orders**",
                                    "/api/v1/orders/**")
                            .permitAll();
                    request.requestMatchers(HttpMethod.GET,
                                    "/api/v1/order-statuses**",
                                    "/api/v1/order-statuses/**")
                            .permitAll();
                    request.requestMatchers(HttpMethod.GET,
                                    "/api/v1/brands**",
                                    "/api/v1/brands/**")
                            .permitAll();
                    request.requestMatchers(HttpMethod.GET,
                                    "/api/v1/products**",
                                    "/api/v1/products/**")
                            .permitAll();
                    request.anyRequest().authenticated();
                })
                .exceptionHandling(exceptionHandling -> {
                    exceptionHandling.authenticationEntryPoint((request, response, authException) -> {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    });

                    exceptionHandling.accessDeniedHandler((request, response, accessDeniedException) -> {
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    });
                })
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(AbstractHttpConfigurer::disable);


        return http.build();
    }

}