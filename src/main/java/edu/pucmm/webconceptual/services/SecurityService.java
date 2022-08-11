package edu.pucmm.webconceptual.services;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServletRequest;
import edu.pucmm.webconceptual.entidades.Role;
import edu.pucmm.webconceptual.entidades.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SecurityService implements UserDetailsService {

    private static final String LOGOUT_SUCCESS_URL = "/";
    private final Logger logger = LoggerFactory.getLogger(SecurityService.class);
    private UsuarioService usuarioService;

    public SecurityService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public UserDetails getAuthenticatedUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Object principal = context.getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return (UserDetails) context.getAuthentication().getPrincipal();
        }
        // Anonymous or no authentication.
        return null;
    }

    public void logout() {
        UI.getCurrent().getPage().setLocation(LOGOUT_SUCCESS_URL);
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(
                VaadinServletRequest.getCurrent().getHttpServletRequest(), null,
                null);
    }

    /**
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario user = usuarioService.get(username).orElseThrow(() ->new UsernameNotFoundException("Usuario " + username + " no existe o esta deshabilitado!!!"));

        Set<GrantedAuthority> roles = new HashSet<>();
        for (Role role : user.getListaRoles()) {
            logger.info("Usuario {} - Roles: {}",user.getUsername(),  role.getNombre());
            roles.add(new SimpleGrantedAuthority(role.getValor()));
        }

        /*List<GrantedAuthority> grantedAuthorities = new ArrayList<>(roles);
        System.out.println("Listado Roles: "+grantedAuthorities.toString());*/

        return User.withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities("ROLE_ADMIN").build();
    }
}