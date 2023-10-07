package com.nhn.cigarwebapp.config;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .headers(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(request -> {
                    request.requestMatchers("/api/v1/auth/**", "/api/v1/auth/refresh/**").permitAll();
                    request.requestMatchers("/api/v1/test**", "/api/v1/test/**").permitAll();
                    request.requestMatchers(
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
                    request.requestMatchers(
                                    "/api/v1/payments**",
                                    "/api/v1/payments/**")
                            .permitAll();
                    request.requestMatchers(
                                    "/api/v1/order-statuses**",
                                    "/api/v1/order-statuses/**")
                            .permitAll();
                    request.requestMatchers(
                                    "/api/v1/brands**",
                                    "/api/v1/brands/**")
                            .permitAll();
                    request.requestMatchers(
                                    "/api/v1/products**",
                                    "/api/v1/products/**")
                            .permitAll();
                    request.requestMatchers(
                                    "/api/v1/carts**",
                                    "/api/v1/carts/**")
                            .permitAll();
                    request.requestMatchers(
                                    "/api/v1/files**",
                                    "/api/v1/files/**")
                            .permitAll();
                    request.requestMatchers(
                                    "/api/v1/payment-destinations**",
                                    "/api/v1/payment-destinations/**")
                            .permitAll();
                    request.requestMatchers("/api/v1/admin/**").hasAuthority("ADMIN");
                    request.anyRequest().authenticated();
                })
                .exceptionHandling(exceptionHandling -> {
//                    exceptionHandling.authenticationEntryPoint((request, response, authException) -> {
//                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                    });

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
