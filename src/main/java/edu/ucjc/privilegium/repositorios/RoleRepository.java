package edu.ucjc.privilegium.repositorios;


import edu.ucjc.privilegium.entidades.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByCodigo(Integer codigo);
    List<Role> findAllByHabilitado(boolean habilitado);
    Role findByHabilitadoAndCodigo(boolean habilitado, Integer codigo);
}
