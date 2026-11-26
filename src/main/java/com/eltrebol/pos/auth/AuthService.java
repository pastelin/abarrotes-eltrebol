package com.eltrebol.pos.auth;

import com.eltrebol.pos.config.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public Map<String, Object> login(String username, String password) {
        Usuario user = usuarioRepository.findByUsernameAndActivoTrue(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario o contraseña incorrectos"));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new IllegalArgumentException("Usuario o contraseña incorrectos");
        }

        String token = jwtUtil.generarToken(user.getUsername(), user.getRol());
        return Map.of(
                "token", token,
                "username", user.getUsername(),
                "nombreCompleto", user.getNombreCompleto(),
                "rol", user.getRol()
        );
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario crearUsuario(String nombreCompleto, String username, String password, String rol) {
        if (usuarioRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("El nombre de usuario ya existe");
        }
        Usuario u = new Usuario();
        u.setNombreCompleto(nombreCompleto);
        u.setUsername(username);
        u.setPasswordHash(passwordEncoder.encode(password));
        u.setRol(rol.toUpperCase());
        u.setActivo(true);
        return usuarioRepository.save(u);
    }

    public void cambiarEstadoUsuario(Long id, boolean activo) {
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        u.setActivo(activo);
        usuarioRepository.save(u);
    }
}