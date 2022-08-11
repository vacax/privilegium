package edu.pucmm.webconceptual.config;

import edu.pucmm.webconceptual.entidades.Role;
import edu.pucmm.webconceptual.entidades.Usuario;
import edu.pucmm.webconceptual.services.RoleService;
import edu.pucmm.webconceptual.services.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class BoostrapApp implements ApplicationRunner {

    private final Logger logger = LoggerFactory.getLogger(BoostrapApp.class);
    private RoleService roleService;
    private UsuarioService usuarioService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     *
     * @param roleService
     */
    public BoostrapApp(RoleService roleService, UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
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

        //Creación del usuario administrador.
        if(usuarioService.get("admin").isEmpty()){
            logger.info("Creando Usuario administrador");
            Usuario admin = new Usuario("admin",
                    "admin@admin.com",
                    passwordEncoder.encode("admin"),
                    "Administrador",
                    true,
                    true);
            admin.setListaRoles(new HashSet<>(roleService.findAll()));
            usuarioService.save(admin);
        }
    }
}
