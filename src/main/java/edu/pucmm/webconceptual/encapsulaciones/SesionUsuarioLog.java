package edu.pucmm.webconceptual.encapsulaciones;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
public class SesionUsuarioLog {
    long id;
    String usuario;
    String alias;
    String host;
    Date fecha;
}
