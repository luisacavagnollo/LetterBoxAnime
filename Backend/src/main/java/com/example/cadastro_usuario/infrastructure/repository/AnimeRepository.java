package com.example.cadastro_usuario.infrastructure.repository;

import com.example.cadastro_usuario.infrastructure.entitys.Animes;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnimeRepository extends JpaRepository<Animes, Integer> {

        Optional<Animes> findByTitulo(String titulo);

        @Transactional
        void deleteByTitulo(String titulo);

}
