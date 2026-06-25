package com.example.cadastro_usuario.business;

import com.example.cadastro_usuario.infrastructure.entitys.Animes;
import com.example.cadastro_usuario.infrastructure.entitys.Categoria;
import com.example.cadastro_usuario.infrastructure.entitys.Usuarios;
import com.example.cadastro_usuario.infrastructure.repository.AnimeRepository;
import com.example.cadastro_usuario.infrastructure.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnimeServiceTest {

    @Mock
    private AnimeRepository repository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private AnimeService service;

    @Test
    void salvarAnime_usuarioEncontrado_associaUsuarioEsalva() {
        String email = "user@example.com";

        Usuarios usuario = Usuarios.builder()
                .id(1)
                .nome("Fulano")
                .email(email)
                .senha("senha123")
                .build();

        Animes anime = Animes.builder()
                .titulo("Naruto")
                .categoria(Categoria.Terror)
                .descricao("Ninja que quer ser Hokage")
                .imagemUrl("http://imagem.com/naruto.jpg")
                .build();

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));

        service.salvarAnime(anime, email);

        ArgumentCaptor<Animes> animeCaptor = ArgumentCaptor.forClass(Animes.class);
        verify(repository).save(animeCaptor.capture());

        Animes animeCapturado = animeCaptor.getValue();
        assertEquals(usuario, animeCapturado.getUsuario());
    }

    @Test
    void salvarAnime_usuarioNaoEncontrado_lancaRuntimeException() {
        String email = "inexistente@example.com";

        Animes anime = Animes.builder()
                .titulo("One Piece")
                .categoria(Categoria.Terror)
                .build();

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.empty());

        RuntimeException excecao = assertThrows(RuntimeException.class,
                () -> service.salvarAnime(anime, email));

        assertEquals("Usuário não encontrado", excecao.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void listarTodos_repositorioRetornaLista_retornaListaCompleta() {
        Animes anime1 = Animes.builder()
                .id(1)
                .titulo("Naruto")
                .categoria(Categoria.Terror)
                .descricao("Ninja que quer ser Hokage")
                .imagemUrl("http://imagem.com/naruto.jpg")
                .build();

        Animes anime2 = Animes.builder()
                .id(2)
                .titulo("One Piece")
                .categoria(Categoria.Terror)
                .descricao("Pirata em busca do One Piece")
                .imagemUrl("http://imagem.com/onepiece.jpg")
                .build();

        List<Animes> listaEsperada = List.of(anime1, anime2);

        when(repository.findAll()).thenReturn(listaEsperada);

        List<Animes> resultado = service.listarTodos();

        assertEquals(listaEsperada, resultado);
        verify(repository).findAll();
    }

    @Test
    void buscarAnimePorTitulo_tituloExistente_retornaAnime() {
        String titulo = "Naruto";

        Animes anime = Animes.builder()
                .id(1)
                .titulo(titulo)
                .categoria(Categoria.Terror)
                .descricao("Ninja que quer ser Hokage")
                .imagemUrl("http://imagem.com/naruto.jpg")
                .build();

        when(repository.findByTitulo(titulo)).thenReturn(Optional.of(anime));

        Animes resultado = service.buscarAnimePorTitulo(titulo);

        assertEquals(anime, resultado);
        verify(repository).findByTitulo(titulo);
    }

    @Test
    void buscarAnimePorTitulo_tituloInexistente_lancaRuntimeException() {
        String titulo = "TituloInexistente";

        when(repository.findByTitulo(titulo)).thenReturn(Optional.empty());

        RuntimeException excecao = assertThrows(RuntimeException.class,
                () -> service.buscarAnimePorTitulo(titulo));

        assertEquals("Titulo não encontrado", excecao.getMessage());
    }

    @Test
    void deletarAnimePorTitulo_tituloInformado_delegaDelecaoAoRepository() {
        service.deletarAnimePorTitulo("Naruto");

        verify(repository).deleteByTitulo("Naruto");
    }

    static Stream<Arguments> atualizarCenarios() {
        return Stream.of(
                Arguments.of(
                        Animes.builder().titulo("NarutoNovo").build(),
                        "NarutoNovo",
                        "DescricaoOriginal",
                        Categoria.Terror,
                        "http://original.com/img.jpg"
                ),
                Arguments.of(
                        Animes.builder().descricao("NovaDescricao").build(),
                        "NarutoOriginal",
                        "NovaDescricao",
                        Categoria.Terror,
                        "http://original.com/img.jpg"
                ),
                Arguments.of(
                        Animes.builder()
                                .titulo("NarutoCompleto")
                                .descricao("DescricaoCompleta")
                                .categoria(Categoria.Romance)
                                .imagemUrl("http://novo.com/img.jpg")
                                .build(),
                        "NarutoCompleto",
                        "DescricaoCompleta",
                        Categoria.Romance,
                        "http://novo.com/img.jpg"
                ),
                Arguments.of(
                        Animes.builder().build(),
                        "NarutoOriginal",
                        "DescricaoOriginal",
                        Categoria.Terror,
                        "http://original.com/img.jpg"
                )
        );
    }

    @ParameterizedTest
    @MethodSource("atualizarCenarios")
    void atualizarAnimePorTitulo_partialMerge_aplicaCamposNaoNulosEPreservaOriginal(
            Animes updateInput,
            String expectedTitulo,
            String expectedDescricao,
            Categoria expectedCategoria,
            String expectedImagemUrl
    ) {
        Animes entidadeExistente = Animes.builder()
                .id(1)
                .titulo("NarutoOriginal")
                .descricao("DescricaoOriginal")
                .categoria(Categoria.Terror)
                .imagemUrl("http://original.com/img.jpg")
                .build();

        when(repository.findByTitulo("NarutoOriginal")).thenReturn(Optional.of(entidadeExistente));

        service.atualizarAnimePorTitulo("NarutoOriginal", updateInput);

        ArgumentCaptor<Animes> captor = ArgumentCaptor.forClass(Animes.class);
        verify(repository).save(captor.capture());

        Animes salvo = captor.getValue();
        assertEquals(1, salvo.getId());
        assertEquals(expectedTitulo, salvo.getTitulo());
        assertEquals(expectedDescricao, salvo.getDescricao());
        assertEquals(expectedCategoria, salvo.getCategoria());
        assertEquals(expectedImagemUrl, salvo.getImagemUrl());
    }

    @Test
    void atualizarAnimePorTitulo_animeNaoEncontrado_lancaRuntimeException() {
        when(repository.findByTitulo("TituloInexistente")).thenReturn(Optional.empty());

        Animes updateInput = Animes.builder().titulo("QualquerTitulo").build();

        RuntimeException excecao = assertThrows(RuntimeException.class,
                () -> service.atualizarAnimePorTitulo("TituloInexistente", updateInput));

        assertEquals("Anime não encontrado", excecao.getMessage());
        verify(repository, never()).save(any());
    }
}
