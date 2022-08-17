package edu.pucmm.webconceptual.services;

import edu.pucmm.webconceptual.encapsulaciones.RotacionLogRow;
import edu.pucmm.webconceptual.entidades.RotacionPasswordLog;
import edu.pucmm.webconceptual.entidades.ServidorSsh;
import edu.pucmm.webconceptual.repositorios.ConexionRepository;
import edu.pucmm.webconceptual.repositorios.RotacionPasswordLogRepository;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ConexionService extends BaseCrudService<ServidorSsh, Long>{

    private ConexionRepository conexionRepository;
    private RotacionPasswordLogRepository rotacionPasswordLogRepository;

    public ConexionService(ConexionRepository conexionRepository, RotacionPasswordLogRepository rotacionPasswordLogRepository) {
        this.conexionRepository = conexionRepository;
        this.rotacionPasswordLogRepository = rotacionPasswordLogRepository;
    }

    @Override
    public JpaRepository<ServidorSsh, Long> getRepository() {
        return conexionRepository;
    }

    public void aplicarRotacionPassword(ServidorSsh sshServidorSsh) throws IOException {
        final SSHClient ssh = new SSHClient();
        try (ssh) {
            //configuración de conexión
            ssh.addHostKeyVerifier(new PromiscuousVerifier());
            ssh.connect(sshServidorSsh.getHost(), sshServidorSsh.getPuerto());
            ssh.authPassword(sshServidorSsh.getUsuario(), sshServidorSsh.getPassword());
            final Session session = ssh.startSession();
            try (session) {
                // creación de la nueva contraseña
                String contrasena = RandomStringUtils.random(10, true, true);
                System.out.println("Contraseña nueva:"+contrasena);

                //
                sshServidorSsh.setPassword(contrasena);
                sshServidorSsh.setFechaUltimaRotacion(new Date());
                LocalDateTime fechaUnaSemana = sshServidorSsh.getFechaUltimaRotacion().toInstant().atZone(ZoneId.systemDefault()).plusWeeks(1).toLocalDateTime();
                sshServidorSsh.setFechaRotacion(java.sql.Timestamp.valueOf(fechaUnaSemana));
                System.out.println("La proxima fecha: "+fechaUnaSemana.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));

                //
                Session.Command command = session.exec(String.format("echo '%s:%s' | sudo chpasswd && history -c", sshServidorSsh.getUsuario(), contrasena));
                command.join(1, TimeUnit.SECONDS);

                //
                String resultado = IOUtils.readFully(command.getInputStream()).toString();
                System.out.println(""+resultado);
                save(sshServidorSsh);

                //
                var log = new RotacionPasswordLog();
                log.setServidorSsh(sshServidorSsh);
                log.setResultado(resultado);
                rotacionPasswordLogRepository.save(log);

            }
        }
    }

    public List<RotacionLogRow> listaRotacionLog(){
        List<RotacionLogRow> l = new ArrayList<>();
        var lista = rotacionPasswordLogRepository.findAll(Sort.by("dateCreated"));
        lista.forEach(r -> {
            l.add(new RotacionLogRow(r.getId(), r.getServidorSsh().getAlias(), r.getDateCreated(), r.getResultado()));
        });
        return l;
    }



}
