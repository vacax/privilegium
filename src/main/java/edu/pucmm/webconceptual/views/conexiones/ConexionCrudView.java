package edu.pucmm.webconceptual.views.conexiones;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.*;
import edu.pucmm.webconceptual.entidades.ServidorSsh;
import edu.pucmm.webconceptual.services.ConexionService;
import edu.pucmm.webconceptual.views.MainLayout;
import edu.pucmm.webconceptual.views.sshterminal.SshTerminalView;
import org.vaadin.crudui.crud.impl.GridCrud;
import org.vaadin.crudui.form.impl.field.provider.ComboBoxProvider;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;

@PageTitle("Conexión Servidores")
@Route(value = "conexiones", layout = MainLayout.class)
//@RolesAllowed("ADMIN")
@PermitAll
public class ConexionCrudView extends Div implements AfterNavigationObserver {

    public ConexionCrudView(ConexionService conexionService) {
        setId("crud-conexion-view");
        setSizeFull();
        HorizontalLayout layout = new HorizontalLayout();
        GridCrud<ServidorSsh> crud = new GridCrud<>(ServidorSsh.class);
        crud.getGrid().setColumns("alias","id", "usuario","host", "puerto");
        crud.getCrudFormFactory().setVisibleProperties("alias","host", "puerto", "usuario", "password", "fechaRotacion", "habilitado");
        //
        crud.getCrudFormFactory().setFieldType("password", PasswordField.class);

        //
        crud.getCrudFormFactory().setFieldProvider("fechaRotacion", o -> {
            ServidorSsh t = (ServidorSsh) o;
            DateTimePicker dateTimePicker = new DateTimePicker("Fecha Rotación");
            if(t.getFechaRotacion()!=null) {
                dateTimePicker.setValue(t.getFechaRotacion().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            }
            return  dateTimePicker;
        });

        //Convertidor de las fechas.
        crud.getCrudFormFactory().setConverter("fechaRotacion", new Converter<LocalDateTime, Date>() {

            @Override
            public Result<Date> convertToModel(LocalDateTime localDateTime, ValueContext valueContext) {
                Date fecha = java.sql.Timestamp.valueOf(localDateTime);
                return Result.ok(fecha);
            }

            @Override
            public LocalDateTime convertToPresentation(Date date, ValueContext valueContext) {

                return date != null ? date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime() : null;
            }
        });

        //
        Button abrirConexionbtn = new Button("Abrir Conexión", buttonClickEvent -> {
            ServidorSsh tmp = crud.getGrid().getSelectedItems().stream().findFirst().get();
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
