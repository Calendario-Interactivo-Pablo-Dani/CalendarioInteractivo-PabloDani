package com.planify.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
/*Para poder encriptar las contraseñas con él hash vamos a usar BCryptPasswordEncoder
* Esta función se encuentra dentro de la dependencia spring-security-crypto
* La hemos añadido al pom, pero Spring Security por defecto nos va a bloquear la API
*Con esta clase vamos a conseguir usar BCrypto sin que Spring bloquee la API */

/*Le indicamos a spring que aquí hay una configuración del sistema*/
@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                .anyRequest().permitAll());
        return http.build();
    }
}
