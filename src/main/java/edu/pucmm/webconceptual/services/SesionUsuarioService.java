package edu.pucmm.webconceptual.services;

import edu.pucmm.webconceptual.encapsulaciones.SesionUsuarioLog;
import edu.pucmm.webconceptual.entidades.RegistroSesion;
import edu.pucmm.webconceptual.entidades.SesionUsuario;
import edu.pucmm.webconceptual.repositorios.RegistroSesionRepository;
import edu.pucmm.webconceptual.repositorios.SesionUsuarioRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SesionUsuarioService extends BaseCrudService<SesionUsuario, Long>{

    private final SesionUsuarioRepository sesionUsuarioRepository;
    private final RegistroSesionRepository registroSesionRepository;

    public SesionUsuarioService(SesionUsuarioRepository sesionUsuarioRepository, RegistroSesionRepository registroSesionRepository) {
        this.sesionUsuarioRepository = sesionUsuarioRepository;
        this.registroSesionRepository = registroSesionRepository;
    }

    @Override
    protected JpaRepository<SesionUsuario, Long> getRepository() {
        return sesionUsuarioRepository;
    }

    /**
     *
     * @param sesionUsuario
     * @param comando
     * @param tipoRegistro
     */
    public void registrarComandoTrabajado(SesionUsuario sesionUsuario,
                                          String comando,
                                          RegistroSesion.TipoRegistro tipoRegistro){
        registroSesionRepository.save(new RegistroSesion()
                .tipoRegistro(tipoRegistro)
                .sesionUsuario(sesionUsuario)
                .log(comando));
    }

    public List<SesionUsuarioLog> listaRegistroLog(){
        List<SesionUsuarioLog> lista = new ArrayList<>();
        sesionUsuarioRepository.findAll().forEach(sesionUsuario -> {
            SesionUsuarioLog log =  new SesionUsuarioLog();
            log.setUsuario(sesionUsuario.usuario().getUsername());
            log.setAlias(sesionUsuario.servidorSsh().getAlias());
            log.setHost(sesionUsuario.servidorSsh().getHost());
            log.setFecha(sesionUsuario.getDateCreated());
            log.setId(sesionUsuario.getId());
            lista.add(log);
        });
        return lista;
    }
}
