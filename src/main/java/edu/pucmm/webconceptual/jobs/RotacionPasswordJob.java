package edu.pucmm.webconceptual.jobs;

import edu.pucmm.webconceptual.entidades.ServidorSsh;
import edu.pucmm.webconceptual.repositorios.ConexionRepository;
import edu.pucmm.webconceptual.services.ConexionService;
import edu.pucmm.webconceptual.util.FuncionalidadesHelper;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Configuration
@EnableScheduling
public class RotacionPasswordJob {

    private ConexionService conexionService;

    public RotacionPasswordJob(ConexionService conexionService) {
        this.conexionService = conexionService;
    }

    @Scheduled(fixedDelay = 5000)
    public void aplicarRotacionPasswordTerminal(){
        System.out.println("Proceso para dar inicio a la rotación del password");
        List<ServidorSsh> lista = ((ConexionRepository)conexionService.getRepository()).findAllConexionesParaRotar(new Date());
        System.out.println("La cantidad de contraseñas para rotar: "+lista.size());
        lista.forEach(ssh -> {
            //aplicando la rotación
            try {
                conexionService.aplicarRotacionPassword(ssh);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
