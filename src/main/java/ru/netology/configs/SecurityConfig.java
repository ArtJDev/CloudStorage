package ru.netology.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import ru.netology.security.JwtEntryPoint;
import ru.netology.security.JwtTokenFilter;
import ru.netology.services.UserDetailsServiceImpl;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtEntryPoint jwtEntryPoint;
    public final JwtTokenFilter jwtTokenFilter;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService, JwtEntryPoint jwtEntryPoint, JwtTokenFilter jwtTokenFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtEntryPoint = jwtEntryPoint;
        this.jwtTokenFilter = jwtTokenFilter;
    }

    @Bean
    public AuthenticationManager authManager(UserDetailsService userDetailsService) { //UserDetailsService или UserDetailsServiceImpl???
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        return new ProviderManager(authProvider);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return
                http
                        .cors(c -> {
                            CorsConfigurationSource cs = r -> {
                                CorsConfiguration cc = new CorsConfiguration();
                                cc.setAllowedOrigins(List.of("http://localhost:8080/"));
                                cc.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
                                cc.setAllowedHeaders(List.of("*"));
                                cc.setAllowCredentials(true);
                                return cc;
                            };
                            c.configurationSource(cs);
                        })
                        .csrf(csrf -> csrf.disable())
                        .authorizeRequests(auth -> auth
                                .mvcMatchers("/login").permitAll()
                                .anyRequest().authenticated())
                        .logout()
                        .logoutUrl("/logout")
                        .deleteCookies("JSESSIONID")
                        .clearAuthentication(true)
                        .logoutSuccessUrl("/login")
                        .and()
                        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .exceptionHandling((ex) -> ex.authenticationEntryPoint(jwtEntryPoint))
                        .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                        .build();
    }
}