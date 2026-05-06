package com.example.cadastro_usuario.business;

import org.springframework.stereotype.Service;
import com.example.cadastro_usuario.infrastructure.entitys.Animes;
import com.example.cadastro_usuario.infrastructure.entitys.Usuarios;
import com.example.cadastro_usuario.infrastructure.repository.AnimeRepository;
import com.example.cadastro_usuario.infrastructure.repository.UsuarioRepository;

import java.util.List;

@Service
public class AnimeService {
    private final AnimeRepository repository;
    private final UsuarioRepository usuarioRepository;

    public AnimeService(AnimeRepository repository, UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
    }

    public void salvarAnime(Animes animes, String email) {
        Usuarios usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        animes.setUsuario(usuario); // ✅ associa o usuário ao anime
        repository.save(animes);
    }

    public List<Animes> listarTodos() {
        return repository.findAll();
    }

    public Animes buscarAnimePorTitulo(String titulo) {
        return repository.findByTitulo(titulo).orElseThrow(
                () -> new RuntimeException("Titulo não encontrado")
        );
    }

    public void deletarAnimePorTitulo(String titulo) {
        repository.deleteByTitulo(titulo);
    }

    public void atualizarAnimePorTitulo(String titulo, Animes animes) {
        Animes animesEntity = repository.findByTitulo(titulo)
                .orElseThrow(() -> new RuntimeException("Anime não encontrado"));

        Animes animesAtualizado = Animes.builder()
                .id(animesEntity.getId())
                .titulo(animes.getTitulo() != null ? animes.getTitulo() : animesEntity.getTitulo())
                .descricao(animes.getDescricao() != null ? animes.getDescricao() : animesEntity.getDescricao())
                .categoria(animes.getCategoria() != null ? animes.getCategoria() : animesEntity.getCategoria())
                .imagemUrl(animes.getImagemUrl() != null ? animes.getImagemUrl() : animesEntity.getImagemUrl())
                .build();

        repository.save(animesAtualizado);
    }
}