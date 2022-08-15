package edu.pucmm.webconceptual.jobs;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
public class RotacionPasswordJob {

    @Scheduled(fixedDelay = 5000)
    public void aplicarRotacionPasswordTerminal(){
        System.out.println("Proceso para dar inicio a la rotación del password");
    }
}
