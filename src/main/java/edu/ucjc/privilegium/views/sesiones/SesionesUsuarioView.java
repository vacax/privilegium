package edu.ucjc.privilegium.views.sesiones;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.*;
import edu.ucjc.privilegium.encapsulaciones.SesionUsuarioLog;
import edu.ucjc.privilegium.services.SesionUsuarioService;
import edu.ucjc.privilegium.views.MainLayout;

import javax.annotation.security.RolesAllowed;

@PageTitle("Registros de Conexiones")
@Route(value = "log-conexiones", layout = MainLayout.class)
//@RolesAllowed("ADMIN")
@RolesAllowed({"ADMIN"})
public class SesionesUsuarioView extends Div implements AfterNavigationObserver {

    public SesionesUsuarioView(SesionUsuarioService sesionUsuarioService) {
        setId("log-conexion-view");
        setSizeFull();
        HorizontalLayout layout = new HorizontalLayout();
        Grid<SesionUsuarioLog> grid = new Grid<>(SesionUsuarioLog.class);
        grid.setColumns("usuario","alias","host", "fecha");

        //agregando el boton.
        grid.addColumn(new ComponentRenderer<>(sesionUsuarioLog -> new Button("Ver log", buttonClickEvent -> {
            System.out.println("El sesion usuario: "+sesionUsuarioLog.getUsuario());
            UI.getCurrent().navigate(RegistroSesionView.class, new RouteParameters("id", String.valueOf(sesionUsuarioLog.getId())));
        })));

        //Habilitando la consulta. TODO: cambiar a lazy load
        grid.setItems(sesionUsuarioService.listaRegistroLog());

        layout.setSizeFull();
        layout.add(grid);
        add(layout);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {

    }
}
