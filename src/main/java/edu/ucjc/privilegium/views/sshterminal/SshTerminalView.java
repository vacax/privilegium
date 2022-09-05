package edu.ucjc.privilegium.views.sshterminal;

import com.flowingcode.vaadin.addons.xterm.*;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import de.codecamp.vaadin.components.messagedialog.MessageDialog;
import edu.ucjc.privilegium.entidades.RegistroSesion;
import edu.ucjc.privilegium.entidades.ServidorSsh;
import edu.ucjc.privilegium.entidades.SesionUsuario;
import edu.ucjc.privilegium.services.ConexionService;
import edu.ucjc.privilegium.services.SecurityService;
import edu.ucjc.privilegium.services.SesionUsuarioService;
import edu.ucjc.privilegium.services.UsuarioService;
import edu.ucjc.privilegium.util.FuncionalidadesHelper;
import edu.ucjc.privilegium.views.MainLayout;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;

import javax.annotation.security.PermitAll;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@PageTitle("Ssh Terminal")
@Route(value = "ssh-terminal/:id", layout = MainLayout.class)
@PermitAll
public class SshTerminalView extends VerticalLayout implements BeforeEnterObserver, BeforeLeaveObserver {

    private UI ui;
    private XTerm xterm;
    //private ConexionSsh conexion;
    private OutputStream outputStream;
    private InputStream inputStream;
    private String prompt = "";
    private String hostname;
    private final ConexionService conexionService;
    private ServidorSsh servidorSsh;
    private final SecurityService securityService;
    private final SesionUsuarioService sesionUsuarioService;
    private final UsuarioService usuarioService;
    private SesionUsuario sesionUsuario;
    private  SSHClient ssh;


    public SshTerminalView(ConexionService conexionService,
                           SecurityService securityService,
                           SesionUsuarioService sesionUsuarioService,
                           UsuarioService usuarioService) {

        this.conexionService = conexionService;
        this.securityService = securityService;
        this.sesionUsuarioService = sesionUsuarioService;
        this.usuarioService = usuarioService;

        //this.conexion =  conexion;

        setSizeFull();

        xterm = new XTerm();

        xterm.setCursorBlink(true);
        xterm.setCursorStyle(ITerminalOptions.CursorStyle.UNDERLINE);
        xterm.setBellStyle(ITerminalOptions.BellStyle.SOUND);
        xterm.setDrawBoldTextInBrightColors(true);


        xterm.setSizeFull();
        xterm.scrollToBottom();

        xterm.setCopySelection(true);
        xterm.setUseSystemClipboard(ITerminalClipboard.UseSystemClipboard.READWRITE);
        xterm.setPasteWithMiddleClick(true);
        xterm.setPasteWithRightClick(true);

        TerminalHistory.extend(xterm);

        xterm.addLineListener(lineEvent -> {

            String line = lineEvent.getLine();
            String lineaNueva = "";
            var comandos = line.split(" ");
            for (int i = 1; i < comandos.length; i++) {
                lineaNueva += comandos[i] + " ";
            }
            System.out.println("Comando: " + lineaNueva);
            try {
                // Enviando al registro de los log.
                sesionUsuarioService.registrarComandoTrabajado(sesionUsuario, lineaNueva, RegistroSesion.TipoRegistro.COMANDO);

                //Enviado al SSH.
                outputStream.write((lineaNueva + "\n").getBytes());
                outputStream.flush();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });


        xterm.focus();
        xterm.setVisible(true);
        //xterm.fit();
        add(xterm);

    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        try {
            ui = attachEvent.getUI();
            nombreHostnameConexion(servidorSsh);
            conexionShell(servidorSsh);
        } catch (IOException e) {
            //throw new RuntimeException(e);
            FuncionalidadesHelper.mostrarMensajeError("Error Conectado Terminal", ""+e.getMessage(), TerminalesDisponiblesView.class, e);
        }
    }

    protected void onDetach(DetachEvent detachEvent) {

    }

    /**
     * @param sshServidorSsh
     * @throws IOException
     */
    public void conexionShell(ServidorSsh sshServidorSsh) throws IOException {
        ssh = new SSHClient();
        //configuración de conexión
        ssh.addHostKeyVerifier(new PromiscuousVerifier());
        ssh.connect(sshServidorSsh.getHost(), sshServidorSsh.getPuerto());
        ssh.authPassword(sshServidorSsh.getUsuario(), sshServidorSsh.getPassword());
        final Session session = ssh.startSession();

        session.allocateDefaultPTY();

        final Session.Shell shell = session.startShell();

        //Obteniendo los flujos de datos.
        inputStream = shell.getInputStream();
        outputStream = shell.getOutputStream();

        new Thread(() -> {
            while (true) {
                try {
                    int byteDisponible = inputStream.available();
                    if (byteDisponible > 0) {
                        byte[] inputData = new byte[1024];
                        inputStream.read(inputData, 0, byteDisponible);
                        ui.access(() -> {
                            String entrada = new String(inputData, 0, byteDisponible, StandardCharsets.UTF_8);
                            //entrada = entrada.replaceAll(prompt,"");
                            xterm.write(entrada);
                            System.out.println("La entrada: " + entrada);
                            // Enviando al registro de los log.
                            sesionUsuarioService.registrarComandoTrabajado(sesionUsuario, entrada, RegistroSesion.TipoRegistro.RESPUESTA);
                        });
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    /**
     * @param sshServidorSsh
     * @throws IOException
     */
    public void nombreHostnameConexion(ServidorSsh sshServidorSsh) throws IOException {
        final SSHClient ssh = new SSHClient();
        try (ssh) {
            //configuración de conexión
            ssh.addHostKeyVerifier(new PromiscuousVerifier());
            ssh.connect(sshServidorSsh.getHost(), sshServidorSsh.getPuerto());
            ssh.authPassword(sshServidorSsh.getUsuario(), sshServidorSsh.getPassword());
            final Session session = ssh.startSession();
            try (session) {

                Session.Command command = session.exec("hostname");
                command.join(1, TimeUnit.SECONDS);
                ui.access(() -> {
                    try {
                        hostname = IOUtils.readFully(command.getInputStream()).toString().replaceAll("\n", "");
                        prompt = sshServidorSsh.getUsuario() + "@" + hostname + ":~$ "; //force la última parte
                        xterm.setPrompt(prompt);
                        System.out.println("El hostname es: " + hostname);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

            }
        }
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent event) {
        if (ssh !=null && ssh.isConnected()) {
            BeforeLeaveEvent.ContinueNavigationAction action =  event.postpone();
            MessageDialog messageDialog =
                    new MessageDialog().setTitle("¿Desea salir de una conexión activa?", VaadinIcon.EDIT.create()).setMessage(
                            "Tienes una conexión activa, salir de forma abrupta puede cerrar procesos que este ejecuando.");
            messageDialog.addButton().text("Salir").primary().onClick(ev -> {
                        Notification.show("Cerrando la conexión");
                        try {
                            ssh.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        action.proceed();
                    })
                    .closeOnClick();
            messageDialog.addButtonToLeft().text("Volver").tertiary()
                    .onClick(ev -> Notification.show("Canceled.")).closeOnClick();
            messageDialog.open();
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        String id = beforeEnterEvent.getRouteParameters().get("id").get();
        System.out.println("La conexión enviada: " + id);
        servidorSsh = conexionService.get(Long.parseLong(id)).get();

        //creando la sesión para el registro de los logs.
        usuarioService.get(securityService.getAuthenticatedUser().getUsername()).ifPresent(usuario -> {
            sesionUsuario = new SesionUsuario()
                    .usuario(usuario)
                    .servidorSsh(servidorSsh);
            sesionUsuarioService.save(sesionUsuario);
        });
    }
}
