package com.eltrebol.pos.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        try {
            return ResponseEntity.ok(authService.login(body.get("username"), body.get("password")));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/users")
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok(authService.listarUsuarios());
    }

    @PostMapping("/users")
    public ResponseEntity<?> crear(@RequestBody Map<String, String> body) {
        try {
            Usuario creado = authService.crearUsuario(
                    body.get("nombreCompleto"),
                    body.get("username"),
                    body.get("password"),
                    body.get("rol")
            );
            return ResponseEntity.ok(creado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PatchMapping("/users/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Long id, @RequestBody Map<String, Boolean> body) {
        authService.cambiarEstadoUsuario(id, body.getOrDefault("activo", true));
        return ResponseEntity.ok(Map.of("mensaje", "Estado actualizado"));
    }
}