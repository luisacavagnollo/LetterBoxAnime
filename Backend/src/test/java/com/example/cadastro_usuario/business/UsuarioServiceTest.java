package com.example.cadastro_usuario.business;

import com.example.cadastro_usuario.infrastructure.entitys.Usuarios;
import com.example.cadastro_usuario.infrastructure.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    UsuarioRepository repository;

    @InjectMocks
    UsuarioService service;

    @ParameterizedTest
    @ValueSource(strings = {"senha123", "abc", "P@ssw0rd!", "umaSenhaMuitoLonga1234567890"})
    void salvarUsuarios_senhaFornecida_deveSalvarSenhaHasheada(String senhaOriginal) {
        Usuarios usuario = Usuarios.builder()
                .email("usuario@email.com")
                .nome("Teste")
                .senha(senhaOriginal)
                .build();

        service.salvarUsuarios(usuario);

        ArgumentCaptor<Usuarios> captor = ArgumentCaptor.forClass(Usuarios.class);
        verify(repository, times(1)).save(captor.capture());

        String senhaCapturada = captor.getValue().getSenha();

        assertNotEquals(senhaOriginal, senhaCapturada,
                "A senha armazenada nao pode ser igual a senha original");

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        assertTrue(encoder.matches(senhaOriginal, senhaCapturada),
                "O hash BCrypt deve corresponder a senha original");
    }

    @Test
    void login_credenciaisValidas_deveRetornarUsuario() {
        String email = "usuario@email.com";
        String senhaPlain = "senha123";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        Usuarios usuario = Usuarios.builder()
                .email(email)
                .nome("Teste")
                .senha(encoder.encode(senhaPlain))
                .build();

        when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));

        Usuarios resultado = service.login(email, senhaPlain);

        assertEquals(usuario, resultado);
    }

    @Test
    void login_emailNaoEncontrado_deveLancarExcecao() {
        String email = "inexistente@email.com";

        when(repository.findByEmail(email)).thenReturn(Optional.empty());

        RuntimeException excecao = assertThrows(RuntimeException.class,
                () -> service.login(email, "senha123"));

        assertEquals("Usuário não encontrado", excecao.getMessage());
    }

    @Test
    void login_senhaErrada_deveLancarExcecao() {
        String email = "usuario@email.com";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        Usuarios usuario = Usuarios.builder()
                .email(email)
                .nome("Teste")
                .senha(encoder.encode("senha123"))
                .build();

        when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));

        RuntimeException excecao = assertThrows(RuntimeException.class,
                () -> service.login(email, "senhaErrada"));

        assertEquals("Senha inválida", excecao.getMessage());
    }

    @Test
    void buscarUsuarioPorEmail_emailExistente_deveRetornarUsuario() {
        String email = "usuario@email.com";

        Usuarios usuario = Usuarios.builder()
                .email(email)
                .nome("Teste")
                .senha("hashQualquer")
                .build();

        when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));

        Usuarios resultado = service.buscarUsuarioPorEmail(email);

        assertEquals(usuario, resultado);
    }

    @Test
    void buscarUsuarioPorEmail_emailNaoEncontrado_deveLancarExcecao() {
        String email = "inexistente@email.com";

        when(repository.findByEmail(email)).thenReturn(Optional.empty());

        RuntimeException excecao = assertThrows(RuntimeException.class,
                () -> service.buscarUsuarioPorEmail(email));

        assertEquals("Email não encontrado", excecao.getMessage());
    }

    @Test
    void deletarUsuarioPorEmail_emailFornecido_deveChamarDeleteByEmail() {
        service.deletarUsuarioPorEmail("fulano@email.com");

        verify(repository).deleteByEmail("fulano@email.com");
    }

    static Stream<Arguments> atualizarCenarios() {
        return Stream.of(
                Arguments.of(Usuarios.builder().nome("NomeNovo").email(null).senha(null).build()),
                Arguments.of(Usuarios.builder().nome(null).email("novo@email.com").senha(null).build()),
                Arguments.of(Usuarios.builder().nome("NomeNovo").email("novo@email.com").senha("senhaNova").build()),
                Arguments.of(Usuarios.builder().nome(null).email(null).senha(null).build())
        );
    }

    @ParameterizedTest
    @MethodSource("atualizarCenarios")
    void atualizarUsuarioPorId_camposFornecidos_deveFazerMergeCorreto(Usuarios updateInput) {
        Usuarios entity = Usuarios.builder()
                .id(1)
                .nome("NomeOriginal")
                .email("original@email.com")
                .senha("senhaOriginal")
                .build();

        when(repository.findById(1)).thenReturn(Optional.of(entity));

        service.atualizarUsuarioPorId(1, updateInput);

        ArgumentCaptor<Usuarios> captor = ArgumentCaptor.forClass(Usuarios.class);
        verify(repository).saveAndFlush(captor.capture());

        Usuarios salvo = captor.getValue();

        String nomeEsperado = updateInput.getNome() != null ? updateInput.getNome() : "NomeOriginal";
        String emailEsperado = updateInput.getEmail() != null ? updateInput.getEmail() : "original@email.com";
        String senhaEsperada = updateInput.getSenha() != null ? updateInput.getSenha() : "senhaOriginal";

        assertEquals(nomeEsperado, salvo.getNome());
        assertEquals(emailEsperado, salvo.getEmail());
        assertEquals(senhaEsperada, salvo.getSenha());
        assertEquals(1, salvo.getId());
    }

    @Test
    void atualizarUsuarioPorId_idNaoEncontrado_deveLancarExcecao() {
        when(repository.findById(99)).thenReturn(Optional.empty());

        Usuarios updateInput = Usuarios.builder().nome("Qualquer").build();

        RuntimeException excecao = assertThrows(RuntimeException.class,
                () -> service.atualizarUsuarioPorId(99, updateInput));

        assertEquals("Usuario não encontrado", excecao.getMessage());
    }
}
