package edu.pucmm.webconceptual.repositorios;

import edu.pucmm.webconceptual.entidades.Conexion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConexionRepository extends JpaRepository<Conexion, Long> {

}
