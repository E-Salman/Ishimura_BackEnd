package Ishimura.uade.IshimuraCollectibles.controllers.config;

import Ishimura.uade.IshimuraCollectibles.entity.Rol;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(req -> req
        
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/error/**").permitAll()

                // listar el catalogo y poder filtrar por coleccionable 
                .requestMatchers("/catalogo", "/catalogo/*").hasAnyAuthority(Rol.USER.name(), Rol.ADMIN.name())

                //operaciones de stock
                .requestMatchers("/catalogo/*/incrementarstock", "/catalogo/*/decrementarstock")
                    .hasAnyAuthority(Rol.ADMIN.name())

                // Categorías: acceso a USER y ADMIN
                .requestMatchers("/categories/**").hasAnyAuthority(Rol.USER.name(), Rol.ADMIN.name())

                // todo lo demás requiere estar autenticado
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
