package com.example.cadastro_usuario.infrastructure.entitys;

import javax.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name= "animes")
@Entity
public class Animes {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name= "titulo", unique = true)
    private String titulo;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria", nullable = false)
    private Categoria categoria;

    @Column(name = "imagem_url")
    private String imagemUrl;

    @Column(name = "descricao", length = 1000)
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuarios usuario;

}
