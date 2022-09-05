package edu.ucjc.privilegium.entidades;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

@Entity
@Data
public class RotacionPasswordLog extends Base{
    @ManyToOne
    ServidorSsh servidorSsh;
    @Lob
    String resultado;
}
