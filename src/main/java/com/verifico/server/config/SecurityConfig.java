package com.verifico.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

import com.verifico.server.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Value("${VERIFICO_USER_NAME}")
  private String username;

  @Value("${VERIFICO_PASSKEY}")
  private String password;

  @Value("${VERIFICO_ROLE}")
  private String role;

  @Autowired
  private CustomUserDetailsService customUserDetailsService;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.ignoringRequestMatchers("/api/users/create"))
        .authorizeHttpRequests(
            (requests) -> requests
                .requestMatchers("/api/users/create").permitAll()
                .requestMatchers("/api/users/**").authenticated().anyRequest().permitAll())
        .formLogin(Customizer.withDefaults()).httpBasic(Customizer.withDefaults());
    return http.build();
  }

  // @Bean
  // public UserDetailsService userDetailsService() {
  // var user =
  // User.builder().username(username).password(passwordEncoder().encode(password)).roles(role)
  // .build();

  // return new InMemoryUserDetailsManager(user);
  // }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(customUserDetailsService);
    daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
    return daoAuthenticationProvider;
  }
}
