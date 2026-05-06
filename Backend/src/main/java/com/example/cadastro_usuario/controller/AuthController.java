package com.example.cadastro_usuario.controller;

import com.example.cadastro_usuario.business.UsuarioService;
import com.example.cadastro_usuario.infrastructure.entitys.Usuarios;
import com.example.cadastro_usuario.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final UsuarioService usuarioService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Usuarios usuarios){

        Usuarios user = usuarioService.login(
                usuarios.getEmail(),
                usuarios.getSenha()
        );

        String token = jwtUtil.gerarToken(user.getEmail());

        return ResponseEntity.ok(token);
    }
}
