package edu.pucmm.webconceptual.views.sesiones;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import edu.pucmm.webconceptual.encapsulaciones.RegistroSesionRow;
import edu.pucmm.webconceptual.entidades.RegistroSesion;
import edu.pucmm.webconceptual.entidades.SesionUsuario;
import edu.pucmm.webconceptual.services.SesionUsuarioService;
import edu.pucmm.webconceptual.views.MainLayout;
import edu.pucmm.webconceptual.views.dashboard.DashboardView;

import javax.annotation.security.PermitAll;

@PageTitle("Registro de Eventos")
@Route(value = "registro-eventos/:id", layout = MainLayout.class)
@PermitAll
public class RegistroSesionView extends Div implements BeforeEnterObserver {

    private SesionUsuarioService sesionUsuarioService;
    private SesionUsuario sesionUsuario;
    private H2 h2;
    private Grid<RegistroSesionRow> grid;

    public RegistroSesionView(SesionUsuarioService sesionUsuarioService) {

        //
        this.sesionUsuarioService = sesionUsuarioService;

        //
        VerticalLayout layout = new VerticalLayout();
        grid = new Grid<>(RegistroSesionRow.class);
        grid.setColumns("id", "fechaCreacion","tipoRegistro");
        grid.getColumns().forEach(c -> {
            c.setAutoWidth(true);
            c.setResizable(true);
        });
        grid.addColumn(new ComponentRenderer<>(r -> {
            TextArea t = new TextArea();
            t.setValue(r.getLog());
            t.setReadOnly(true);
            t.setSizeFull();
            return t;
        })).setHeader("log");


        //
        h2 = new H2("");

        //
        layout.add(h2);
        layout.add(grid);
        layout.setSizeFull();
        add(layout);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        //
        beforeEnterEvent.getRouteParameters().get("id").ifPresent(s -> {
            try {
                sesionUsuario = sesionUsuarioService.get(Long.parseLong(s)).orElseThrow();
                h2.setText(String.format("ID: %d - Usuario: %s - Terminal: %s - Fecha: %s",
                        sesionUsuario.getId(),
                        sesionUsuario.usuario().getUsername(),
                        sesionUsuario.servidorSsh().getAlias(),
                        sesionUsuario.getDateCreated()));
                grid.setItems(sesionUsuarioService.getRegistroBySesion(sesionUsuario));
            }catch (Exception ex){
                Notification.show("No existe el registro consultado");
                UI.getCurrent().navigate(DashboardView.class);
            }
        });

        //
    }
}
