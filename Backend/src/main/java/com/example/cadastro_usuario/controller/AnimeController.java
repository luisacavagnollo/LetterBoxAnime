package com.example.cadastro_usuario.controller;

import com.example.cadastro_usuario.business.AnimeService;
import com.example.cadastro_usuario.infrastructure.entitys.Animes;
import com.example.cadastro_usuario.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/animes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AnimeController {

    private final AnimeService animeService;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<Void> salvarAnime(
            @RequestBody Animes animes,
            @RequestHeader("Authorization") String authHeader) { // ✅ pega o token

        String token = authHeader.substring(7);
        String email = jwtUtil.extrairEmail(token); // ✅ extrai o email

        animeService.salvarAnime(animes, email);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/todos")
    public ResponseEntity<List<Animes>> listarTodos() {
        return ResponseEntity.ok(animeService.listarTodos());
    }

    @DeleteMapping
    public ResponseEntity<Void> deletarAnimePorTitulo(@RequestParam String titulo) {
        animeService.deletarAnimePorTitulo(titulo);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Void> atualizarAnimePorTitulo(
            @RequestParam String titulo,
            @RequestBody Animes animes) {
        animeService.atualizarAnimePorTitulo(titulo, animes);
        return ResponseEntity.ok().build();
    }
}