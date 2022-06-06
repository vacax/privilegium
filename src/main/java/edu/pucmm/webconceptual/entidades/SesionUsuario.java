package edu.pucmm.webconceptual.entidades;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SesionUsuario extends Base{

    @ManyToOne
    Usuario usuario;
    @ManyToOne
    ServidorSsh servidorSsh;
    @ManyToMany()
    private Set<RegistroSesion> listaRegistroSesion = new HashSet<>();

}
