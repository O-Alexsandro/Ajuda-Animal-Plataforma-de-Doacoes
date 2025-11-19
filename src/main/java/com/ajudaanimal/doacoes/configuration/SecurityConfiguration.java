package com.ajudaanimal.doacoes.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(a -> a

                        // TODOS OS USUÁRIOS PODEM REALIZAR
                        .requestMatchers(HttpMethod.POST,"/login").permitAll()
                        .requestMatchers(HttpMethod.POST,"/usuarios").permitAll()
                        .requestMatchers(HttpMethod.PUT,"/usuarios").permitAll()
                        .requestMatchers(HttpMethod.GET,"/doacoes").permitAll()
                        .requestMatchers(HttpMethod.GET,"/doacoes/**").permitAll()

                        // DOAÇÕES
                        .requestMatchers(HttpMethod.POST, "/doacoes").hasAnyRole("ADMIN", "ONG")
                        .requestMatchers(HttpMethod.PUT, "/doacoes").hasAnyRole("ADMIN", "ONG")
                        .requestMatchers(HttpMethod.DELETE, "/doacoes/{id}").hasAnyRole("ADMIN", "ONG")

                        // INTERESSES
                        .requestMatchers(HttpMethod.POST, "/interesse").hasAnyRole("USUARIO", "ONG", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/interesse").hasAnyRole("USUARIO", "ONG", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/interesse/{id}*").hasAnyRole("USUARIO", "ONG", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/interesse").hasAnyRole("ONG", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/interesse/cancelar/**").hasAnyRole("USUARIO", "ONG", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/interesse/status/**").hasAnyRole("USUARIO", "ONG", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/interesse/status/adocao/**").hasAnyRole("USUARIO", "ONG", "ADMIN")

                        // ADMIN
                        .requestMatchers(HttpMethod.GET, "/usuarios").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/usuarios/{id}").hasRole("ADMIN")
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager (AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
