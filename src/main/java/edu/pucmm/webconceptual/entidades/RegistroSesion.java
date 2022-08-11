package edu.pucmm.webconceptual.entidades;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@Data
@Accessors(fluent = true)
public class RegistroSesion extends Base{

    public enum TipoRegistro{
        COMANDO, RESPUESTA
    }

    @ManyToOne
    SesionUsuario sesionUsuario;
    @Lob
    String log;
    @Enumerated(EnumType.STRING)
    TipoRegistro tipoRegistro;

}
