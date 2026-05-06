package com.example.cadastro_usuario.controller;

import com.example.cadastro_usuario.business.UsuarioService;
import com.example.cadastro_usuario.infrastructure.entitys.Usuarios;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService usuarioService;

    // CADASTRO
    @PostMapping
    public ResponseEntity<Void> salvarUsuario(@RequestBody Usuarios usuarios){
        usuarioService.salvarUsuarios(usuarios);
        return ResponseEntity.ok().build();
    }

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<Integer> login(@RequestBody Usuarios usuarios){
        Usuarios user = usuarioService.login(
                usuarios.getEmail(),
                usuarios.getSenha()
        );

        return ResponseEntity.ok(user.getId());
    }

    @GetMapping
    public ResponseEntity<Usuarios> buscarUsuarioPorEmail(@RequestParam String email){
        return ResponseEntity.ok(usuarioService.buscarUsuarioPorEmail(email));
    }

    @DeleteMapping
    public ResponseEntity<Void> deletarUsuarioPorEmail(@RequestParam String email){
        usuarioService.deletarUsuarioPorEmail(email);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Void> atualizarUsuarioPorId(
            @RequestParam Integer id,
            @RequestBody Usuarios usuarios){

        usuarioService.atualizarUsuarioPorId(id, usuarios);
        return ResponseEntity.ok().build();
    }
}