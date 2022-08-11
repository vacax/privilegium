package edu.pucmm.webconceptual.services;

import edu.pucmm.webconceptual.entidades.ServidorSsh;
import edu.pucmm.webconceptual.repositorios.ConexionRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class ConexionService extends BaseCrudService<ServidorSsh, Long>{

    private ConexionRepository conexionRepository;

    public ConexionService(ConexionRepository conexionRepository) {
        this.conexionRepository = conexionRepository;
    }

    @Override
    protected JpaRepository<ServidorSsh, Long> getRepository() {
        return conexionRepository;
    }


}
