package Ishimura.uade.IshimuraCollectibles.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import Ishimura.uade.IshimuraCollectibles.controllers.auth.AuthenticationRequest;
import Ishimura.uade.IshimuraCollectibles.controllers.auth.AuthenticationResponse;
import Ishimura.uade.IshimuraCollectibles.controllers.auth.RegisterRequest;
import Ishimura.uade.IshimuraCollectibles.controllers.config.JwtService;
import Ishimura.uade.IshimuraCollectibles.entity.Usuario;
import Ishimura.uade.IshimuraCollectibles.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
        private final UserRepository repository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;
        private final AuthenticationManager authenticationManager;

        public AuthenticationResponse register(RegisterRequest request) {
                var usuario = Usuario.builder()
                                .email(request.getEmail())
                                .nombre(request.getNombre())
                                .apellido(request.getApellido())
                                .direccion(request.getDireccion())
                                .password(passwordEncoder.encode(request.getPassword()))
                                .rol(request.getRol())
                                .build();

                repository.save(usuario);
                var jwtToken = jwtService.generateToken(usuario);
                return AuthenticationResponse.builder()
                                .accessToken(jwtToken)
                                .build();
        }

        public AuthenticationResponse authenticate(AuthenticationRequest request) {
                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getEmail(),
                                                request.getPassword()));

                var user = repository.findByEmail(request.getEmail())
                                .orElseThrow();
                var jwtToken = jwtService.generateToken(user);
                return AuthenticationResponse.builder()
                                .accessToken(jwtToken)
                                .build();
        }
}
