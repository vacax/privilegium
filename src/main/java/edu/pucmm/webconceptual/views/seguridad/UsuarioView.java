package edu.pucmm.webconceptual.views.seguridad;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import edu.pucmm.webconceptual.entidades.Role;
import edu.pucmm.webconceptual.entidades.ServidorSsh;
import edu.pucmm.webconceptual.entidades.Usuario;
import edu.pucmm.webconceptual.services.ConexionService;
import edu.pucmm.webconceptual.services.RoleService;
import edu.pucmm.webconceptual.services.UsuarioService;
import edu.pucmm.webconceptual.views.MainLayout;
import org.vaadin.crudui.crud.impl.GridCrud;
import org.vaadin.crudui.form.impl.field.provider.CheckBoxGroupProvider;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;

@PageTitle("Usuarios")
@Route(value = "usuarios", layout = MainLayout.class)
@RolesAllowed({"ADMIN"})
public class UsuarioView extends Div implements AfterNavigationObserver {

    public UsuarioView(UsuarioService usuarioServices, RoleService rolesServices, ConexionService conexionService) {
        setId("crud-usuario-view");
        setSizeFull();
        HorizontalLayout layout = new HorizontalLayout();
        GridCrud<Usuario> crud = new GridCrud<>(Usuario.class);
        crud.getGrid().setColumns("username", "nombre", "correo", "administrador" , "habilitado", "listaRoles", "listaServidoresSsh" );
        crud.getCrudFormFactory().setVisibleProperties("username", "password", "nombre", "administrador", "correo", "habilitado", "listaRoles", "listaServidoresSsh");
        //
        crud.getCrudFormFactory().setFieldProvider("listaRoles",
                new CheckBoxGroupProvider<>("Lista Roles", rolesServices.findAll(), Role::getDescripcion));

        crud.getCrudFormFactory().setFieldProvider("listaServidoresSsh",
                new CheckBoxGroupProvider<>("Lista Servidores", conexionService.findAll(), ServidorSsh::getAlias));


        crud.getCrudFormFactory().setFieldType("password", PasswordField.class);

        crud.setOperations(
                usuarioServices::findAll,
                usuarioServices::update,
                usuarioServices::actualizarUsuario,
                usuarioServices::deleteEntidad
        );

        layout.setSizeFull();
        layout.add(crud);
        add(layout);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {

    }
}
