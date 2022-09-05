package edu.ucjc.privilegium.repositorios;

import edu.ucjc.privilegium.entidades.RegistroSesion;
import edu.ucjc.privilegium.entidades.SesionUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistroSesionRepository extends JpaRepository<RegistroSesion, Long> {


    List<RegistroSesion> findAllBySesionUsuarioOrderByDateCreated(SesionUsuario sesionUsuario);
}
