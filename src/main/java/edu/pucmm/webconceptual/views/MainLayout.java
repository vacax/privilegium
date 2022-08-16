package edu.pucmm.webconceptual.views;


import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import edu.pucmm.webconceptual.services.SecurityService;
import edu.pucmm.webconceptual.views.about.AboutView;
import edu.pucmm.webconceptual.views.conexiones.ConexionCrudView;
import edu.pucmm.webconceptual.views.conexiones.RotacionesPasswordView;
import edu.pucmm.webconceptual.views.dashboard.DashboardView;
import edu.pucmm.webconceptual.views.seguridad.UsuarioView;
import edu.pucmm.webconceptual.views.sesiones.SesionesUsuarioView;
import edu.pucmm.webconceptual.views.sshterminal.SshTerminalView;
import edu.pucmm.webconceptual.views.sshterminal.TerminalesDisponiblesView;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {

    private SecurityService securityService;

    /**
     * A simple navigation item component, based on ListItem element.
     */
    public static class MenuItemInfo extends ListItem {

        private final Class<? extends Component> view;

        public MenuItemInfo(String menuTitle, String iconClass, Class<? extends Component> view) {
            this.view = view;
            RouterLink link = new RouterLink();
            link.addClassNames("menu-item-link");
            link.setRoute(view);

            Span text = new Span(menuTitle);
            text.addClassNames("menu-item-text");

            link.add(new LineAwesomeIcon(iconClass), text);
            add(link);
        }

        public Class<?> getView() {
            return view;
        }

        /**
         * Simple wrapper to create icons using LineAwesome iconset. See
         * https://icons8.com/line-awesome
         */
        @NpmPackage(value = "line-awesome", version = "1.3.0")
        public static class LineAwesomeIcon extends Span {
            public LineAwesomeIcon(String lineawesomeClassnames) {
                addClassNames("menu-item-icon");
                if (!lineawesomeClassnames.isEmpty()) {
                    addClassNames(lineawesomeClassnames);
                }
            }
        }

    }

    private H1 viewTitle;

    public MainLayout(SecurityService securityService) {
        this.securityService = securityService;

        setPrimarySection(Section.DRAWER);
        addToNavbar(true, createHeaderContent());
        addToDrawer(createDrawerContent());
    }

    private Component createHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.addClassNames("view-toggle");
        toggle.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        toggle.getElement().setAttribute("aria-label", "Menu toggle");

        viewTitle = new H1();
        viewTitle.addClassNames("view-title");

        Header header = new Header(toggle, viewTitle);
        header.addClassNames("view-header");
        return header;
    }

    private Component createDrawerContent() {
        H2 appName = new H2("Web-Conceptual-PAM");
        appName.addClassNames("app-name");

        com.vaadin.flow.component.html.Section section = new com.vaadin.flow.component.html.Section(appName,
                createNavigation(), createFooter());
        section.addClassNames("drawer-section");
        return section;
    }

    private Nav createNavigation() {
        Nav nav = new Nav();
        nav.addClassNames("menu-item-container");
        nav.getElement().setAttribute("aria-labelledby", "views");

        // Wrap the links in a list; improves accessibility
        UnorderedList list = new UnorderedList();
        list.addClassNames("navigation-list");
        nav.add(list);

        for (MenuItemInfo menuItem : createMenuItems()) {
            list.add(menuItem);

        }
        return nav;
    }

    private MenuItemInfo[] createMenuItems() {
        return new MenuItemInfo[]{ //
                //new MenuItemInfo("Dashboard", "la la-dashboard", DashboardView.class), //
                new MenuItemInfo("Terminales", "la la-terminal", TerminalesDisponiblesView.class), //
                new MenuItemInfo("Conexiones", "la la-terminal", ConexionCrudView.class), //
                new MenuItemInfo("Registros", "la la-terminal", SesionesUsuarioView.class), //
                new MenuItemInfo("Rotación Password", "la la-terminal", RotacionesPasswordView.class), //
                new MenuItemInfo("Usuarios", "la la-file", UsuarioView.class), //
                new MenuItemInfo("About", "la la-file", AboutView.class), //
        };
    }

    private Footer createFooter() {
        Footer layout = new Footer();
        layout.addClassNames("footer");

        UserDetails maybeUser = securityService.getAuthenticatedUser();
        if (maybeUser!=null) {

            Avatar avatar = new Avatar(maybeUser.getUsername(), "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=128&h=128&q=80");
            avatar.addClassNames("me-xs");

            ContextMenu userMenu = new ContextMenu(avatar);
            userMenu.setOpenOnClick(true);
            userMenu.addItem("Logout", e -> {
                securityService.logout();
            });

            Span name = new Span(maybeUser.getUsername());
            name.addClassNames("font-medium", "text-s", "text-secondary");

            layout.add(avatar, name);
        } else {
            Anchor loginLink = new Anchor("login", "Sign in");
            layout.add(loginLink);
        }
        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
