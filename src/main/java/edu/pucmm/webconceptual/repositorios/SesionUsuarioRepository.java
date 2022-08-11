package edu.pucmm.webconceptual.repositorios;

import edu.pucmm.webconceptual.entidades.ServidorSsh;
import edu.pucmm.webconceptual.entidades.SesionUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SesionUsuarioRepository extends JpaRepository<SesionUsuario, Long> {

}
