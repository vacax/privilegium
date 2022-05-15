package edu.pucmm.webconceptual.entidades;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Role extends Base {

    public enum RoleCodigo {
        ADMIN(2000, "ADMIN", "Role Administrador."),
        USUARIO(2001, "USUARIO", "Role Usuario del Sistema");

        private final Integer codigo;
        private final String valor;
        private final String descripcion;

        RoleCodigo(Integer codigo, String valor, String descripcion) {
            this.codigo = codigo;
            this.valor = valor;
            this.descripcion = descripcion;
        }

        public Integer getCodigo() {
            return codigo;
        }

        public String getValor() {
            return valor;
        }

        public String getDescripcion() {
            return descripcion;
        }

    }

    private Integer codigo;
    private String nombre;
    private String descripcion;
    private String valor;

    @Override
    public String toString() {
        return String.format("%s: %s", nombre, valor);
    }
}
