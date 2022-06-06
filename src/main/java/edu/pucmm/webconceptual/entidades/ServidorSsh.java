package edu.pucmm.webconceptual.entidades;

import lombok.Data;

import javax.persistence.Entity;
import java.io.Serializable;

@Entity
@Data
public class ServidorSsh extends Base implements Serializable {

    String host;
    int puerto;
    String usuario;
    String password;

}
