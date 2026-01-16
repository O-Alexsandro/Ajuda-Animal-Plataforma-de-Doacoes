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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
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
                        .requestMatchers(HttpMethod.POST, "/doacoes/confirmar/{idUsuario}/{idDoacao}").hasAnyRole("ADMIN", "ONG")
                        .requestMatchers(HttpMethod.GET, "/doacoes/usuario/{idUsuario}").hasAnyRole("USUARIO", "ONG", "ADMIN")

                        // INTERESSES
                        .requestMatchers(HttpMethod.POST, "/interesse").hasAnyRole("USUARIO", "ONG", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/interesse").hasAnyRole("USUARIO", "ONG", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/interesse/{id}*").hasAnyRole("USUARIO", "ONG", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/interesse/usuario/{id}*").hasAnyRole("USUARIO", "ONG", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/interesse").hasAnyRole("ONG", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/interesse/cancelar/**").hasAnyRole("USUARIO", "ONG", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/interesse/status/**").hasAnyRole("USUARIO", "ONG", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/interesse/status/doacao/{idDoacao}").hasAnyRole("USUARIO", "ONG", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/interesse/cancelar/{idUsuario}/{idInteresse}").hasAnyRole("USUARIO", "ONG", "ADMIN")

                        // ADMIN
                        .requestMatchers(HttpMethod.GET, "/usuarios").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/usuarios/{id}").hasAnyRole("ADMIN", "USUARIO", "ONG")
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of(
                "http://127.0.0.1:5500",
                "http://localhost:5500"
        ));

        configuration.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
        ));

        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);
        return source;
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