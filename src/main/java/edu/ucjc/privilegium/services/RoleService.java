package edu.ucjc.privilegium.services;


import edu.ucjc.privilegium.entidades.Role;
import edu.ucjc.privilegium.repositorios.RoleRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService extends BaseCrudService<Role, Long> {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public JpaRepository<Role, Long> getRepository() {
        return roleRepository;
    }

    /**
     * Función para obtener toda la información del rol desde la DB. (no toma en cuenta habilitado)
     *
     * @param roleCodigo Enum del rol que se requiere.
     * @return Objecto rol de la DB si se encontró, de lo contrario retorna null.
     */
    public Role findByCodigo(Role.RoleCodigo roleCodigo) {
        return roleRepository.findByCodigo(roleCodigo.getCodigo());
    }

    /**
     * Busqueda de todos los roles habilitados.
     *
     * @param habilitado si esta anulado o no.
     * @return lista de roles.
     */
    public List<Role> findAllByHabilitado(boolean habilitado) {
        return roleRepository.findAllByHabilitado(habilitado);
    }

    /**
     * Función para obtener toda la información del rol desde la DB.
     *
     * @param habilitado si esta anulado o no.
     * @param roleCodigo Enum del rol que se requiere.
     * @return Objecto rol de la DB si se encontró, de lo contrario retorna null.
     */
    public Role findByHabilitadoAndCodigo(boolean habilitado, Role.RoleCodigo roleCodigo) {
        return roleRepository.findByHabilitadoAndCodigo(habilitado, roleCodigo.getCodigo());
    }
}
