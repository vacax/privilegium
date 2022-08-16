package edu.pucmm.webconceptual.repositorios;

import edu.pucmm.webconceptual.entidades.ServidorSsh;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ConexionRepository extends JpaRepository<ServidorSsh, Long> {

    @Query("select s from ServidorSsh s where s.fechaRotacion <= :fechaActual")
    List<ServidorSsh> findAllConexionesParaRotar(Date fechaActual);
}
