package edu.ucjc.privilegium.entidades;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
public class ServidorSsh extends Base implements Serializable {

    String host;
    int puerto;
    String usuario;
    String password;
    String alias;
    @Temporal(TemporalType.TIMESTAMP)
    Date fechaRotacion;
    Date fechaUltimaRotacion;

}
