package edu.pucmm.webconceptual.entidades;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Base implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected boolean habilitado = true;
    @CreatedBy
    protected String creadoPor = "sistemas";
    @LastModifiedBy
    protected String modificadoPor = "sistemas";
    @CreatedDate
    protected Date dateCreated = new Date();
    @LastModifiedDate
    protected Date lastUpdated = new Date();
    @Version
    protected Long version;

}
