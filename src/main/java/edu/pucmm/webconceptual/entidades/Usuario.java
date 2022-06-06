package edu.pucmm.webconceptual.entidades;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Usuario implements Serializable {

    @Id
    private String username;
    private String correo;
    private String password;
    private String nombre;
    private boolean administrador;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> listaRoles = new HashSet<>();
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<ServidorSsh> listaServidoresSsh = new HashSet<>();
    private boolean habilitado = true;
    @CreatedBy
    private String creadoPor = "sistemas";
    @LastModifiedBy
    private String modificadoPor = "sistemas";
    @CreatedDate
    private Date dateCreated = new Date();
    @LastModifiedDate
    private Date lastUpdated = new Date();
}
