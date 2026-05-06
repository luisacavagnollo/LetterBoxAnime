package com.example.cadastro_usuario.infrastructure.repository;

import com.example.cadastro_usuario.infrastructure.entitys.Usuarios;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuarios, Integer> {

    Optional<Usuarios> findByEmail(String email);

    @Transactional
    void deleteByEmail(String email);

}
