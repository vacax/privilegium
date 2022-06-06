package edu.pucmm.webconceptual.entidades;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Data
public class RegistroSesion extends Base{

    @ManyToOne
    SesionUsuario sesionUsuario;
    String log;
}
