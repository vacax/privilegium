package edu.ucjc.privilegium.encapsulaciones;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class RotacionLogRow {
    long id;
    String terminal;
    Date fecha;
    String log;
}
