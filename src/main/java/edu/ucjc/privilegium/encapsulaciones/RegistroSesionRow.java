package edu.ucjc.privilegium.encapsulaciones;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroSesionRow {
    long id;
    Date fechaCreacion;
    String tipoRegistro;
    String log;
}
