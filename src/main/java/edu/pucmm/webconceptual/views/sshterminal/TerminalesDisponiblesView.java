package edu.pucmm.webconceptual.views.sshterminal;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import edu.pucmm.webconceptual.services.ConexionService;
import edu.pucmm.webconceptual.services.SecurityService;
import edu.pucmm.webconceptual.services.UsuarioService;
import edu.pucmm.webconceptual.views.MainLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import java.security.Principal;

@PageTitle("Conexión Servidores")
@Route(value = "terminales", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@RolesAllowed({"USUARIO","ADMIN"})
@CssImport("./styles/botones.css")
public class TerminalesDisponiblesView extends VerticalLayout implements AfterNavigationObserver, BeforeLeaveObserver {

    private final Logger logger = LoggerFactory.getLogger(TerminalesDisponiblesView.class);

    public TerminalesDisponiblesView(SecurityService securityService,
                                     UsuarioService usuarioService,
                                     ConexionService conexionService){

        //
        HorizontalLayout hz = new HorizontalLayout();

        //Obteniendo lalista de terminales del cliente.
        usuarioService.get(securityService.getAuthenticatedUser().getUsername()).ifPresent(usuario -> {
            logger.info("Usuario Terminal: {}", usuario.getUsername());
            usuario.getListaServidoresSsh().forEach(servidorSsh -> {
                logger.info("Terminal asociada: {}", servidorSsh.getAlias());
                Button boton = new Button(servidorSsh.getAlias(), buttonClickEvent -> {
                    System.out.println("Presionando la terminal del "+ servidorSsh.getAlias());
                    UI.getCurrent().navigate(SshTerminalView.class, new RouteParameters("id", String.valueOf(servidorSsh.getId())));
                });
                //incluyendo el css.
                boton.addClassName("boton-terminales");
                //
                hz.add(boton);
            });
            if(hz.getComponentCount() == 0 ){
                hz.add(new H2("Sin Acceso a Terminales"));
            }
        });

        //agregando al componente.
        add(new H1("Listado de Terminales Disponibles"));
        add(hz);
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent event) {

    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {

    }
}
