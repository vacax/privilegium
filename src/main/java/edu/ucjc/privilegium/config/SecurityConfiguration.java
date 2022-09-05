package edu.ucjc.privilegium.config;

import com.vaadin.flow.spring.security.VaadinWebSecurityConfigurerAdapter;
import edu.ucjc.privilegium.views.login.LoginView;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration
        extends VaadinWebSecurityConfigurerAdapter {


    /*private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
*/
    @Resource
    private UserDetailsService userDetailsService;

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*public SecurityConfiguration(@Autowired UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }*/

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        //Autentificación JPA.
        auth.authenticationProvider(authProvider());
        //auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Delegating the responsibility of general configurations
        // of http security to the super class. It is configuring
        // the followings: Vaadin's CSRF protection by ignoring
        // framework's internal requests, default request cache,
        // ignoring public views annotated with @AnonymousAllowed,
        // restricting access to other views/endpoints, and enabling
        // ViewAccessChecker authorization.
        // You can add any possible extra configurations of your own
        // here (the following is just an example):

        // http.rememberMe().alwaysRemember(false);

        super.configure(http);

        // This is important to register your login view to the
        // view access checker mechanism:
        setLoginView(http, LoginView.class);

    }

    /**
     * Allows access to static resources, bypassing Spring security.
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        // Configure your static resources with public access here:
        web.ignoring().antMatchers(
                "/images/**",
                // (development mode) H2 debugging console
                "/h2-console/**"
        );

        // Delegating the ignoring configuration for Vaadin's
        // related static resources to the super class:
        super.configure(web);
    }

}