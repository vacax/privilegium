package edu.pucmm.webconceptual.views.sesiones;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import edu.pucmm.webconceptual.encapsulaciones.SesionUsuarioLog;
import edu.pucmm.webconceptual.entidades.SesionUsuario;
import edu.pucmm.webconceptual.services.SesionUsuarioService;
import edu.pucmm.webconceptual.views.MainLayout;
import org.vaadin.crudui.crud.impl.GridCrud;

import javax.annotation.security.PermitAll;

@PageTitle("Registros de Conexiones")
@Route(value = "log-conexiones", layout = MainLayout.class)
//@RolesAllowed("ADMIN")
@PermitAll
public class SesionesUsuarioView extends Div implements AfterNavigationObserver {

    public SesionesUsuarioView(SesionUsuarioService sesionUsuarioService) {
        setId("log-conexion-view");
        setSizeFull();
        HorizontalLayout layout = new HorizontalLayout();
        GridCrud<SesionUsuarioLog> crud = new GridCrud<>(SesionUsuarioLog.class);
        crud.getGrid().setColumns("usuario","alias","host", "fecha");

        //
        crud.setFindAllOperation(sesionUsuarioService::listaRegistroLog);
        /*crud.setOperations(
                sesionUsuarioService::findAll,
                sesionUsuarioService::update,
                sesionUsuarioService::update,
                sesionUsuarioService::deleteEntidad
        );*/

        layout.setSizeFull();
        layout.add(crud);
        add(layout);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {

    }
}
