package edu.pucmm.webconceptual.views.sshterminal;

import com.flowingcode.vaadin.addons.xterm.*;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import edu.pucmm.webconceptual.entidades.Conexion;
import edu.pucmm.webconceptual.services.ConexionService;
import edu.pucmm.webconceptual.views.MainLayout;
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
public class SshTerminalView extends VerticalLayout implements BeforeEnterObserver {

    private UI ui;
    private XTerm xterm;
    //private ConexionSsh conexion;
    private OutputStream outputStream;
    private InputStream inputStream;
    private String prompt = "";
    private String hostname;
    private ConexionService conexionService;
    private Conexion conexion;



    public SshTerminalView(ConexionService conexionService) {

        this.conexionService = conexionService;

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
                lineaNueva+=comandos[i]+" ";
            }
            System.out.println("Comando: "+lineaNueva);
            try {
                outputStream.write((lineaNueva+"\n").getBytes());
                outputStream.flush();
            }catch (IOException e) {
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
            nombreHostnameConexion(conexion);
            conexionShell(conexion);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void onDetach(DetachEvent detachEvent) {

    }

    /**
     *
     * @param sshConexion
     * @throws IOException
     */
    public void conexionShell(Conexion sshConexion) throws IOException {
        final SSHClient ssh = new SSHClient();
        //configuración de conexión
        ssh.addHostKeyVerifier(new PromiscuousVerifier());
        ssh.connect(sshConexion.getHost(), sshConexion.getPuerto());
        ssh.authPassword(sshConexion.getUsuario(), sshConexion.getPassword());
        final Session session = ssh.startSession();

        session.allocateDefaultPTY();

        final Session.Shell shell = session.startShell();

        //Obteniendo los flujos de datos.
        inputStream = shell.getInputStream();
        outputStream = shell.getOutputStream();

        new Thread(() -> {
            while(true){
                try {
                    int byteDisponible = inputStream.available();
                    if(byteDisponible > 0){
                        byte[] inputData = new byte[1024];
                        inputStream.read(inputData, 0, byteDisponible);
                        ui.access(() -> {
                            String entrada = new String(inputData, 0, byteDisponible, StandardCharsets.UTF_8);
                            //entrada = entrada.replaceAll(prompt,"");
                            xterm.write(entrada);
                            System.out.println("La entrada: "+entrada);
                        });
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    /**
     *
     * @param sshConexion
     * @throws IOException
     */
    public void nombreHostnameConexion(Conexion sshConexion) throws IOException {
        final SSHClient ssh = new SSHClient();
        try(ssh) {
            //configuración de conexión
            ssh.addHostKeyVerifier(new PromiscuousVerifier());
            ssh.connect(sshConexion.getHost(), sshConexion.getPuerto());
            ssh.authPassword(sshConexion.getUsuario(), sshConexion.getPassword());
            final Session session = ssh.startSession();
            try(session) {

                Session.Command command = session.exec("hostname");
                command.join(1, TimeUnit.SECONDS);
                ui.access(() -> {
                    try {
                        hostname = IOUtils.readFully(command.getInputStream()).toString().replaceAll("\n","");
                        prompt = sshConexion.getUsuario()+"@"+hostname+":~$ "; //force la última parte
                        xterm.setPrompt(prompt);
                        System.out.println("El hostname es: "+hostname);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

            }
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        String id = beforeEnterEvent.getRouteParameters().get("id").get();
        System.out.println("La conexión enviada: "+id);
        conexion = conexionService.get(Long.parseLong(id)).get();
    }
}
