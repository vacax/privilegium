package edu.pucmm.webconceptual.entidades;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
public class SesionUsuario extends Base{

    @ManyToOne
    Usuario usuario;
    @ManyToOne
    ServidorSsh servidorSsh;
    @OneToMany(mappedBy = "sesionUsuario")
    private Set<RegistroSesion> listaRegistroSesion = new HashSet<>();

}
