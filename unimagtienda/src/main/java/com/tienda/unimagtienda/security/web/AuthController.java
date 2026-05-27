package com.tienda.unimagtienda.security.web;

import com.tienda.unimagtienda.security.domine.AppUser;
import com.tienda.unimagtienda.security.domine.Role;
import com.tienda.unimagtienda.security.dto.AuthDtos;
import com.tienda.unimagtienda.security.jwt.JwtService;
import com.tienda.unimagtienda.security.repository.AppUserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AppUserRepository users;
    private final BCryptPasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtService jwt;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody AuthDtos.RegisterRequest req) {

        if (users.existsByEmailIgnoreCase(req.email())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "status", 400,
                            "error", "Bad Request",
                            "message", "El correo electrónico ya está registrado"
                    ));
        }

        var roles = Set.of(Role.ROLE_USER);

        var user = AppUser.builder()
                .email(req.email())
                .password(encoder.encode(req.password()))
                .roles(roles)
                .build();

        users.save(user);

        var roleNames = roles.stream()
                .map(Enum::name)
                .toList();

        var principal = User.withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(roleNames.toArray(String[]::new))
                .build();

        var token = jwt.generateToken(
                principal,
                Map.of("roles", roleNames)
        );

        return ResponseEntity.ok(
                new AuthDtos.AuthResponse(
                        token,
                        "Bearer",
                        jwt.getExpirationSeconds(),
                        roleNames
                )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthDtos.LoginRequest req) {

        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            req.email(),
                            req.password()
                    )
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "status", 401,
                            "error", "Unauthorized",
                            "message", "Correo o contraseña incorrectos"
                    ));
        }

        var user = users.findByEmailIgnoreCase(req.email())
                .orElseThrow(() -> new RuntimeException("User not found"));

        var roleNames = user.getRoles()
                .stream()
                .map(Enum::name)
                .toList();

        var principal = User.withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(roleNames.toArray(String[]::new))
                .build();

        var token = jwt.generateToken(
                principal,
                Map.of("roles", roleNames)
        );

        return ResponseEntity.ok(
                new AuthDtos.AuthResponse(
                        token,
                        "Bearer",
                        jwt.getExpirationSeconds(),
                        roleNames
                )
        );
    }
}