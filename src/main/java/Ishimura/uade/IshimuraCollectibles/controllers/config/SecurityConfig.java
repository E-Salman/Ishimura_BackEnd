package Ishimura.uade.IshimuraCollectibles.controllers.config;

import Ishimura.uade.IshimuraCollectibles.entity.Rol;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthFilter;
        private final AuthenticationProvider authenticationProvider;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                                .csrf(csrf -> csrf.disable())
                                .csrf(AbstractHttpConfigurer::disable)
                                .authorizeHttpRequests(req -> req.requestMatchers("/api/v1/auth/**").permitAll()
                                                .requestMatchers("/error/**").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/categories*").permitAll()
                                                .requestMatchers(HttpMethod.POST, "/categories/*")
                                                .hasAuthority(Rol.ADMIN.name())
                                                .requestMatchers(HttpMethod.DELETE, "/imagenes/**").hasAuthority(Rol.ADMIN.name())
                                                .requestMatchers(HttpMethod.GET, "/imagenes/**").permitAll()
                                                .requestMatchers(HttpMethod.POST, "/imagenes/**").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/coleccionable/**").permitAll()
                                                .requestMatchers(HttpMethod.POST, "/coleccionable/**")
                                                .hasAuthority(Rol.ADMIN.name())
                                                .requestMatchers(HttpMethod.GET, "/lineas/**").permitAll()
                                                .requestMatchers(HttpMethod.POST, "/lineas/**")
                                                .hasAuthority(Rol.ADMIN.name())
                                                .requestMatchers(HttpMethod.GET, "/marcas/**").permitAll()
                                                .requestMatchers(HttpMethod.POST, "/marcas/**")
                                                .hasAuthority(Rol.ADMIN.name())
                                                // Imagenes de marcas (controlador MarcaImagesController)
                                                .requestMatchers(HttpMethod.GET, "/marcasImages/**").permitAll()
                                                .requestMatchers(HttpMethod.POST, "/marcasImages/**")
                                                .hasAuthority(Rol.ADMIN.name())
                                                .requestMatchers(HttpMethod.PUT, "/marcasImages/**")
                                                .hasAuthority(Rol.ADMIN.name())
                                                .requestMatchers(HttpMethod.DELETE, "/marcasImages/**")
                                                .hasAuthority(Rol.ADMIN.name())
                                                // Promociones
                                                .requestMatchers(HttpMethod.GET, "/promociones/**").permitAll()
                                                .requestMatchers(HttpMethod.POST, "/promociones/**").hasAuthority(Rol.ADMIN.name())
                                                .requestMatchers(HttpMethod.PUT, "/promociones/**").hasAuthority(Rol.ADMIN.name())
                                                .requestMatchers(HttpMethod.DELETE, "/promociones/**").hasAuthority(Rol.ADMIN.name())
                                                // Preview de precios
                                                .requestMatchers(HttpMethod.GET, "/precio/**").permitAll()

                                                .requestMatchers("/catalogo", "/catalogo/*")
                                                .hasAnyAuthority(Rol.USER.name(), Rol.ADMIN.name())
                                                // .requestMatchers(HttpMethod.POST, "/marcas/crear")
                                                // .hasAuthority(Rol.ADMIN.name())
                                                // .requestMatchers(HttpMethod.POST, "/lineas/crear")
                                                // .hasAuthority(Rol.ADMIN.name())

                                                .requestMatchers("/catalogo/*/incrementarstock",
                                                                "/catalogo/*/decrementarstock")
                                                .hasAnyAuthority(Rol.ADMIN.name())
                                                .requestMatchers("/mostrar/coleccionable/**").permitAll()
                                                .requestMatchers("/listarColeLineas/**").permitAll()
                                                // listar el catalogo y poder filtrar por coleccionable
                                                .requestMatchers("/catalogo", "/catalogo/*")
                                                .hasAnyAuthority(Rol.USER.name(), Rol.ADMIN.name())

                                                // operaciones de stock
                                                .requestMatchers("/catalogo/*/incrementarstock",
                                                                "/catalogo/*/decrementarstock")
                                                .hasAnyAuthority(Rol.ADMIN.name())

                                                .requestMatchers("/categories/**")
                                                .hasAnyAuthority(Rol.USER.name(), Rol.ADMIN.name())

                                                .requestMatchers("/usuarios/**").hasAuthority(Rol.ADMIN.name())
                                                .requestMatchers("/mis-compras/**").hasAnyAuthority(Rol.USER.name())
                                                .requestMatchers("/admin/compras/**").hasAnyAuthority(Rol.ADMIN.name())
                                                .requestMatchers(HttpMethod.POST, "/ordenes")
                                                .hasAnyAuthority(Rol.USER.name(), Rol.ADMIN.name())
                                                .requestMatchers("/ordenes/mias").hasAnyAuthority(Rol.USER.name())
                                                .requestMatchers("ordenes/**").hasAnyAuthority(Rol.ADMIN.name())

                                                .anyRequest().authenticated())
                                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                                .authenticationProvider(authenticationProvider)
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(List.of("http://localhost:5173"));
                configuration.setAllowedMethods(List.of("*"));
                configuration.setAllowedHeaders(List.of("*"));
                configuration.setAllowCredentials(true);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }
}
