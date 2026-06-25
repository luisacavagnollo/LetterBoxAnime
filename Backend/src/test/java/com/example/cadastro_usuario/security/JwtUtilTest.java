package com.example.cadastro_usuario.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
    }

    @ParameterizedTest
    @ValueSource(strings = {"a@b.com", "usuario@dominio.com.br", "teste123@x.org", "x@y.z"})
    void gerarToken_emailValido_retornaTokenNaoNuloENaoVazio(String email) {
        String token = jwtUtil.gerarToken(email);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void gerarToken_emailsDistintos_retornaTokensDistintos() {
        String token1 = jwtUtil.gerarToken("a@b.com");
        String token2 = jwtUtil.gerarToken("b@c.com");

        assertNotEquals(token1, token2);
    }

    @ParameterizedTest
    @ValueSource(strings = {"a@b.com", "usuario@dominio.com.br", "teste123@x.org"})
    void extrairEmail_tokenGeradoParaEmail_retornaEmailOriginal(String email) {
        String token = jwtUtil.gerarToken(email);
        String emailExtraido = jwtUtil.extrairEmail(token);

        assertEquals(email, emailExtraido);
    }

    @ParameterizedTest
    @ValueSource(strings = {"a@b.com", "usuario@dominio.com.br"})
    void validarToken_tokenGerado_retornaTrue(String email) {
        String token = jwtUtil.gerarToken(email);

        assertTrue(jwtUtil.validarToken(token));
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalido", "", "abc.def.ghi"})
    void validarToken_stringInvalida_retornaFalse(String token) {
        assertFalse(jwtUtil.validarToken(token));
    }

    @Test
    void validarToken_assinaturaAdulterada_retornaFalse() {
        String token = jwtUtil.gerarToken("a@b.com");
        int indiceUltimoPonto = token.lastIndexOf('.');
        String tokenComAssinaturaAdulterada = token.substring(0, indiceUltimoPonto + 1)
                + token.substring(indiceUltimoPonto + 1)
                + "a";

        assertFalse(jwtUtil.validarToken(tokenComAssinaturaAdulterada));
    }
}
