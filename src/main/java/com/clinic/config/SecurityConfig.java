package com.clinic.config;

import com.clinic.security.CustomUserDetailsService;
import com.clinic.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .userDetailsService(customUserDetailsService)
                .authorizeHttpRequests(auth -> auth
                        // CHANGED: Removed /api/auth/register from here
                        .requestMatchers("/api/auth/login", "/debug/auth").permitAll()

                        // CHANGED: Removed /register and /auth/** from here
                        .requestMatchers("/login", "/css/**", "/js/**", "/").permitAll()

                        // NEW: Lock ALL registration endpoints to ADMIN only
                        .requestMatchers("/api/auth/register", "/register", "/auth/register").hasRole("ADMIN")

                        // NEW: Lock create/edit/delete endpoints by role
                        .requestMatchers("/doctors/new", "/doctors/create").hasRole("ADMIN")
                        .requestMatchers("/doctors/*/edit", "/doctors/*/update", "/doctors/*/delete").hasRole("ADMIN")
                        .requestMatchers("/patients/new", "/patients/create").hasAnyRole("ADMIN", "RECEPTIONIST")
                        .requestMatchers("/patients/*/edit", "/patients/*/update").hasAnyRole("ADMIN", "RECEPTIONIST")
                        .requestMatchers("/patients/*/delete").hasRole("ADMIN")
                        .requestMatchers("/appointments/new", "/appointments/create").hasAnyRole("ADMIN", "RECEPTIONIST")
                        .requestMatchers("/appointments/*/edit", "/appointments/*/update").hasAnyRole("ADMIN", "RECEPTIONIST")
                        .requestMatchers("/appointments/*/delete").hasRole("ADMIN")
                        .requestMatchers("/prescriptions/new", "/prescriptions/create").hasAnyRole("ADMIN", "DOCTOR")
                        .requestMatchers("/prescriptions/*/edit", "/prescriptions/*/update").hasAnyRole("ADMIN", "DOCTOR")
                        .requestMatchers("/prescriptions/*/delete").hasRole("ADMIN")
                        .requestMatchers("/medical-records/new", "/medical-records/create").hasAnyRole("ADMIN", "DOCTOR")
                        .requestMatchers("/medical-records/*/edit", "/medical-records/*/update").hasAnyRole("ADMIN", "DOCTOR")
                        .requestMatchers("/medical-records/*/delete").hasRole("ADMIN")

                        .requestMatchers("/swagger-ui/**", "/api-docs/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/")
                        .failureUrl("/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}