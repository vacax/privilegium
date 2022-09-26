package edu.ucjc.privilegium.config;

import edu.ucjc.privilegium.entidades.Role;
import edu.ucjc.privilegium.entidades.ServidorSsh;
import edu.ucjc.privilegium.entidades.Usuario;
import edu.ucjc.privilegium.services.ConexionService;
import edu.ucjc.privilegium.services.RoleService;
import edu.ucjc.privilegium.services.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@Component
@Order(1)
public class BoostrapApp implements ApplicationRunner {

    private final Logger logger = LoggerFactory.getLogger(BoostrapApp.class);
    private RoleService roleService;
    private UsuarioService usuarioService;
    private ConexionService conexionService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private Environment environment;
    @Value("${DEMOSTRACION}")
    private String demostracion;

    /**
     * @param roleService
     * @param conexionService
     */
    public BoostrapApp(RoleService roleService, UsuarioService usuarioService, ConexionService conexionService) {
        this.usuarioService = usuarioService;
        this.roleService = roleService;
        this.conexionService = conexionService;
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
        if (usuarioService.get("admin").isEmpty()) {
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

        if (demostracion != null && demostracion.equals("1")) {
            registroDemostracion();
        }


    }

    private void registroDemostracion () {
        System.out.println("Creando las terminales para la demostración");
        if (usuarioService.get("usuario").isEmpty()) {

            ServidorSsh servidorSsh = new ServidorSsh();
            servidorSsh.setHost("ssh-servidor-1");
            servidorSsh.setAlias("Servidor #1");
            servidorSsh.setUsuario("camacho");
            servidorSsh.setPuerto(2222);
            servidorSsh.setPassword("12345678");
            conexionService.save(servidorSsh);

            ServidorSsh servidorSsh2 = new ServidorSsh();
            servidorSsh2.setHost("ssh-servidor-2");
            servidorSsh2.setAlias("Servidor #2");
            servidorSsh2.setUsuario("carlos");
            servidorSsh2.setPuerto(2222);
            servidorSsh2.setPassword("12345678");
            conexionService.save(servidorSsh2);

            ServidorSsh servidorSsh3 = new ServidorSsh();
            servidorSsh3.setHost("ssh-servidor-3");
            servidorSsh3.setAlias("Servidor #3");
            servidorSsh3.setUsuario("ucjc");
            servidorSsh3.setPuerto(2222);
            servidorSsh3.setPassword("12345678");
            conexionService.save(servidorSsh3);

            //recuperando el admin.
            Usuario usuario = usuarioService.get("admin").orElseThrow();
            usuario.getListaServidoresSsh().addAll(List.of(servidorSsh, servidorSsh2, servidorSsh3));
            usuarioService.update(usuario);

            //creando los usuarios.
            if (usuarioService.get("usuario").isEmpty()) {
                logger.info("Creando usuario");
                Usuario usuario1 = new Usuario("usuario",
                        "usuario1@admin.com",
                        passwordEncoder.encode("usuario"),
                        "Usuario",
                        false,
                        true);
                usuario1.setListaRoles(new HashSet<>(Collections.singleton(roleService.findByCodigo(Role.RoleCodigo.USUARIO))));
                usuario1.getListaServidoresSsh().add(servidorSsh);
                usuarioService.save(usuario1);
            }

            if (usuarioService.get("demo").isEmpty()) {
                logger.info("Creando demo");
                Usuario demo = new Usuario("demo",
                        "demo@admin.com",
                        passwordEncoder.encode("demo"),
                        "Demostración",
                        false,
                        true);
                demo.setListaRoles(new HashSet<>(Collections.singleton(roleService.findByCodigo(Role.RoleCodigo.USUARIO))));
                demo.getListaServidoresSsh().add(servidorSsh2);
                usuarioService.save(demo);
            }
        }

    }

    @Component
    @Order(2)
    @Profile("dev")
    public static class BootStrapDatosDev implements ApplicationRunner{

        private ConexionService conexionService;
        private UsuarioService usuarioService;

        public BootStrapDatosDev(ConexionService conexionService, UsuarioService usuarioService) {
            this.conexionService = conexionService;
            this.usuarioService = usuarioService;
        }

        @Override
        public void run(ApplicationArguments args) throws Exception {
            System.out.println("Iniciando la data automatica para prueba....");
            ServidorSsh servidorSsh = new ServidorSsh();
            servidorSsh.setHost("192.168.0.24");
            servidorSsh.setAlias("Mi-MV-Prueba");
            servidorSsh.setUsuario("root");
            servidorSsh.setPuerto(22);
            servidorSsh.setPassword("12345678");
            //
            conexionService.save(servidorSsh);

            //recuperando el admin.
            Usuario usuario = usuarioService.get("admin").orElseThrow();
            usuario.getListaServidoresSsh().add(servidorSsh);
            //
            usuarioService.update(usuario);

        }


    }

}
