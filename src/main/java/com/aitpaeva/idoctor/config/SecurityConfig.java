package com.aitpaeva.idoctor.config;

import com.aitpaeva.idoctor.security.SecurityFilter;
import com.aitpaeva.idoctor.service.CustomOAuth2UserService;
import com.aitpaeva.idoctor.service.TokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final TokenService tokenService;

    private final CustomOAuth2UserService customOAuth2UserService;

    public SecurityConfig(TokenService tokenService, CustomOAuth2UserService customOAuth2UserService) {
        this.tokenService = tokenService;
        this.customOAuth2UserService = customOAuth2UserService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilter securityFilter() {
        return new SecurityFilter(tokenService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/doctors",
                                "/swagger-ui.html",
                                "/api/auth/**",
                                "/login/**",
                                "/oauth2/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("/oauth2/success", false)
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // URL to trigger logout
                        .logoutSuccessUrl("/login?logout") // Redirect after logout
                        .clearAuthentication(true) // Clear authentication information
                        .invalidateHttpSession(true) // Invalidate HTTP session
                        .addLogoutHandler(new SecurityContextLogoutHandler()) // Custom logout handler
                        .addLogoutHandler((request, response, authentication) -> {
                            // Redirect to Google logout to ensure the user is logged out from Google
                            try {
                                response.sendRedirect("https://accounts.google.com/Logout");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        })
                )
//                .addFilterBefore(securityFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
 }
