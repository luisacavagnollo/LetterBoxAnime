package com.example.cadastro_usuario.business;


import com.example.cadastro_usuario.infrastructure.entitys.Usuarios;
import com.example.cadastro_usuario.infrastructure.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Service
public class UsuarioService {
    private final UsuarioRepository repository;

    public Usuarios login(String email, String senha) {
        Usuarios usuario = repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(senha, usuario.getSenha())) { // ✅ era .equals() antes
            throw new RuntimeException("Senha inválida");
        }

        return usuario;
    }


    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }


    public void salvarUsuarios(Usuarios usuarios){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        usuarios.setSenha(encoder.encode(usuarios.getSenha()));
        repository.save(usuarios);
    }



    public Usuarios buscarUsuarioPorEmail(String email){

        return repository.findByEmail(email).orElseThrow(
                ()-> new RuntimeException("Email não encontrado")
        );
    }

    public void deletarUsuarioPorEmail(String email){
        repository.deleteByEmail(email);
    }

    public void atualizarUsuarioPorId(Integer id, Usuarios usuarios){
        Usuarios usuarioEntity = repository.findById(id).orElseThrow(
                ()-> new RuntimeException("Usuario não encontrado")
        );
        Usuarios usuarioAtualizado = Usuarios.builder()
                .email(usuarios.getEmail() !=null ? usuarios.getEmail() : usuarioEntity.getEmail())
                .nome(usuarios.getNome() !=null ? usuarios.getNome() :usuarioEntity.getNome())
                .id(usuarioEntity.getId())
                .senha(usuarios.getSenha() != null ? usuarios.getSenha() : usuarioEntity.getSenha())
                .build();
        repository.saveAndFlush(usuarioAtualizado);
    }
}

