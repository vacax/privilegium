package edu.ucjc.privilegium.repositorios;

import edu.ucjc.privilegium.entidades.SesionUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SesionUsuarioRepository extends JpaRepository<SesionUsuario, Long> {

}
