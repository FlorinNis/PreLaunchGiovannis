package com.brand.prelaunch.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // Handler-ul standard pentru CSRF în Spring Security 6
        CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
        requestHandler.setCsrfRequestAttributeName(null); // Important pentru compatibilitate Thymeleaf

        http
                // 1. Permitem accesul public, dar păstrăm protecțiile active
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )

                // 2. CONFIGURARE CSRF (ACTIVĂ, NU DISABLED)
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // Stocăm token-ul în cookie
                        .csrfTokenRequestHandler(requestHandler)
                )

                // 3. FILTRUL MAGIC: Forțăm crearea token-ului la început
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)

                // 4. (Opțional) Dacă folosești H2 Console, trebuie permis frame-ul
                .headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }
}

/**
 * Acest filtru obligă Spring să genereze token-ul CSRF imediat ce vine cererea,
 * evitând eroarea "Cannot create session after response committed".
 */
class CsrfCookieFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Încercăm să obținem token-ul CSRF
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());

        // Dacă există, îi apelăm metoda getToken() pentru a forța încărcarea lui ACUM, nu mai târziu
        if (csrfToken != null) {
            csrfToken.getToken();
        }

        filterChain.doFilter(request, response);
    }
}