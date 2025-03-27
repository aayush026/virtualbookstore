package com.virtualbookstore.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.virtualbookstore.filters.JwtFilter;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Autowired
    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/users/register", "/users/login").permitAll() 
                .requestMatchers(HttpMethod.POST, "/books/add").hasRole("ADMIN")  
                .requestMatchers(HttpMethod.GET, "/books/**", "/books/category/**").permitAll()  
                .requestMatchers(HttpMethod.DELETE, "/books/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/orders/user").authenticated()  
                .requestMatchers(HttpMethod.POST, "/orders/").authenticated()  
                .requestMatchers(HttpMethod.PUT, "/orders/**").authenticated()  
                .requestMatchers(HttpMethod.DELETE, "/orders/**").authenticated()  
                .requestMatchers("/orders/**").authenticated()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) 
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
