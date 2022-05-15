package edu.pucmm.webconceptual.config;

import edu.pucmm.webconceptual.entidades.Role;
import edu.pucmm.webconceptual.services.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class BoostrapApp implements ApplicationRunner {

    private final Logger logger = LoggerFactory.getLogger(BoostrapApp.class);
    private RoleService roleService;

    /**
     *
     * @param roleService
     */
    public BoostrapApp(RoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("Inicializando la aplicación");

        //Agregando los permisos.
        logger.info("Creando nuevos roles...");
        for (Role.RoleCodigo rol : Role.RoleCodigo.values()) {
            if (roleService.findByCodigo(rol) == null) {
                Role role = roleService.save(new Role(rol.getCodigo(), rol.name(), rol.getDescripcion(), rol.getValor()));
                logger.info(
                        String.format(
                                "Nuevo rol --> Nombre: %s, Código: %s, Valor: %s, Descripción: %s",
                                role.getNombre(),
                                role.getCodigo(),
                                role.getValor(),
                                role.getDescripcion()
                        )
                );
            }
        }
    }
}
