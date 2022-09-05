package edu.ucjc.privilegium.services;

import edu.ucjc.privilegium.entidades.Usuario;
import edu.ucjc.privilegium.repositorios.UsuarioRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService extends BaseCrudService<Usuario, String>{

    private UsuarioRepository usuarioRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public JpaRepository<Usuario, String> getRepository() {
        return usuarioRepository;
    }

    @Override
    public Usuario update(Usuario entity) {
        Optional<Usuario> byId = usuarioRepository.findById(entity.getUsername());
        if(byId.isPresent()){
            if(!byId.get().getPassword().equals(entity.getPassword())){
                entity.setPassword(bCryptPasswordEncoder.encode(entity.getPassword()));
            }
        }else{
            entity.setPassword(bCryptPasswordEncoder.encode(entity.getPassword()));
        }
        return super.update(entity);
    }

    public Usuario actualizarUsuario(Usuario usuario){
        get(usuario.getUsername()).ifPresent(tmp -> {
            if(!tmp.getPassword().equals(usuario.getPassword())){
                usuario.setPassword(bCryptPasswordEncoder.encode(usuario.getPassword()));
            }

        });
        return save(usuario);
    }
}
