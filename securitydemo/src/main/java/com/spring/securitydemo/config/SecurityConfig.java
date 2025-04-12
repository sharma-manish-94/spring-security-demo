package com.spring.securitydemo.config;

import com.spring.securitydemo.exceptionhandling.CustomAccessDeniedHandler;
import com.spring.securitydemo.exceptionhandling.CustomBasicAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@Profile("dev")
public class SecurityConfig {
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.requiresChannel(config -> config.anyRequest().requiresInsecure()) // only http for dev
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(requests ->
                requests.requestMatchers("/myAccount", "/myBalance", "/myLoans", "/myCards").
                    authenticated()
                    .requestMatchers("/notices", "/contact", "/error", "/register", "/invalidSession",
                        "/expiredSession").permitAll());
        httpSecurity.formLogin(withDefaults());
        httpSecurity.httpBasic(hbc ->
            hbc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));
        httpSecurity.csrf(CsrfConfigurer::disable);
        httpSecurity.exceptionHandling(exceptionConfig ->
            exceptionConfig.accessDeniedHandler(new CustomAccessDeniedHandler()));
        httpSecurity.sessionManagement(config ->
            config
                .sessionFixation(SessionManagementConfigurer.SessionFixationConfigurer::migrateSession)
                .invalidSessionUrl("/invalidSession")
                .maximumSessions(3)
                .maxSessionsPreventsLogin(true)
                .expiredUrl("/expiredSession"));

        return httpSecurity.build();
    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails user = User.withUsername("user").password("{noop}password").roles("USER").build();
//        UserDetails admin = User.withUsername("admin").password("{noop}password").roles("USER", "ADMIN").build();
//        return new InMemoryUserDetailsManager(user, admin);
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    CompromisedPasswordChecker compromisedPasswordChecker() {
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }
}
