package edu.pucmm.webconceptual.views.conexiones;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.*;
import edu.pucmm.webconceptual.entidades.Conexion;
import edu.pucmm.webconceptual.services.ConexionService;
import edu.pucmm.webconceptual.views.MainLayout;
import edu.pucmm.webconceptual.views.sshterminal.SshTerminalView;
import org.vaadin.crudui.crud.impl.GridCrud;

import javax.annotation.security.RolesAllowed;

@PageTitle("Conexión Servidores")
@Route(value = "conexiones", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class ConexionCrudView extends Div implements AfterNavigationObserver {

    public ConexionCrudView(ConexionService conexionService) {
        setId("crud-conexion-view");
        setSizeFull();
        HorizontalLayout layout = new HorizontalLayout();
        GridCrud<Conexion> crud = new GridCrud<>(Conexion.class);
        crud.getGrid().setColumns("id", "usuario","host", "puerto");
        crud.getCrudFormFactory().setVisibleProperties("host", "puerto", "usuario", "password", "habilitado");

        //
        crud.getCrudFormFactory().setFieldType("password", PasswordField.class);

        //
        Button abrirConexionbtn = new Button("Abrir Conexión", buttonClickEvent -> {
            Conexion tmp = crud.getGrid().getSelectedItems().stream().findFirst().get();
            System.out.println("Abriendo la conexión seleccionada: "+tmp.getHost());
            UI.getCurrent().navigate(SshTerminalView.class, new RouteParameters("id", String.valueOf(tmp.getId())));
        });
        abrirConexionbtn.setEnabled(false);
        crud.getCrudLayout().addToolbarComponent(abrirConexionbtn);

        //
        crud.setOperations(
                conexionService::findAll,
                conexionService::update,
                conexionService::update,
                conexionService::deleteEntidad
        );

        crud.getGrid().addSelectionListener(selectionEvent -> {
            abrirConexionbtn.setEnabled(selectionEvent.getFirstSelectedItem().isPresent());
        });

        layout.setSizeFull();
        layout.add(crud);
        add(layout);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {

    }
}
