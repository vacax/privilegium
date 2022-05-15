package edu.pucmm.webconceptual.repositorios;


import edu.pucmm.webconceptual.entidades.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByCodigo(Integer codigo);
    List<Role> findAllByHabilitado(boolean habilitado);
    Role findByHabilitadoAndCodigo(boolean habilitado, Integer codigo);
}
