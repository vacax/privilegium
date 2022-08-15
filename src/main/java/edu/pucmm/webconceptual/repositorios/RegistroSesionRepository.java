package edu.pucmm.webconceptual.repositorios;

import edu.pucmm.webconceptual.entidades.RegistroSesion;
import edu.pucmm.webconceptual.entidades.SesionUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistroSesionRepository extends JpaRepository<RegistroSesion, Long> {


    List<RegistroSesion> findAllBySesionUsuarioOrderByDateCreated(SesionUsuario sesionUsuario);
}
