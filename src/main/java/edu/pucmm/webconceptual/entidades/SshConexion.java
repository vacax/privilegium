package edu.pucmm.webconceptual.entidades;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


public record SshConexion(String host, int puerto , String usuario, String password) {
}
