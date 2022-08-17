package edu.pucmm.webconceptual.views.conexiones;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.*;
import edu.pucmm.webconceptual.encapsulaciones.RotacionLogRow;
import edu.pucmm.webconceptual.encapsulaciones.SesionUsuarioLog;
import edu.pucmm.webconceptual.entidades.RotacionPasswordLog;
import edu.pucmm.webconceptual.services.ConexionService;
import edu.pucmm.webconceptual.services.SesionUsuarioService;
import edu.pucmm.webconceptual.views.MainLayout;
import edu.pucmm.webconceptual.views.sesiones.RegistroSesionView;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;

@PageTitle("Registros de Rotaciones Contraseña")
@Route(value = "log-rotaciones", layout = MainLayout.class)
@RolesAllowed({"ADMIN"})
public class RotacionesPasswordView extends Div implements AfterNavigationObserver {

    public RotacionesPasswordView(ConexionService conexionService) {
        setId("log-rotacion-view");
        setSizeFull();
        HorizontalLayout layout = new HorizontalLayout();
        Grid<RotacionLogRow> grid = new Grid<>(RotacionLogRow.class);
        grid.setColumns("id","fecha","terminal", "log");

        //Habilitando la consulta. TODO: cambiar a lazy load
        grid.setItems(conexionService.listaRotacionLog());

        layout.setSizeFull();
        layout.add(grid);
        add(layout);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {

    }
}
