package edu.ucjc.privilegium.repositorios;


import edu.ucjc.privilegium.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {

    Usuario findByUsername(String username);
    Usuario findByCorreo(String correo);

}
