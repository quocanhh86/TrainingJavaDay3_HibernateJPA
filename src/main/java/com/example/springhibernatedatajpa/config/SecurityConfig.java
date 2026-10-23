package com.example.springhibernatedatajpa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Objects;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth ->
                auth
                        .requestMatchers(
                                "/",
                                "/login",
                                "/register",
                                "/maintenance",
                                "/401",
                                "/403",
                                "/500",
                                "/css/**",
                                "/js/**",
                                "/img/**",
                                "/favicon.ico"
                        ).permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form ->
                        form.loginPage("/login")
                                .successHandler((request, response, authentication) -> {
                                    boolean admin = authentication.getAuthorities().stream()
                                            .anyMatch(a -> Objects.equals(a.getAuthority(), "ROLE_ADMIN"));
                                    response.sendRedirect(request.getContextPath() + (admin ? "/admin" : "/employee"));
                                })
                                .permitAll())
                .logout(logout ->
                        logout.logoutSuccessUrl("/login?logout").permitAll())
                .exceptionHandling(ex ->
                        ex.accessDeniedPage("/403"));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public static RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.withDefaultRolePrefix()
                .role("ADMIN").implies("EMPLOYEE")
                .build();
    }
 }
