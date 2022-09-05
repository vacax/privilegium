package edu.ucjc.privilegium.services;

import edu.ucjc.privilegium.encapsulaciones.RegistroSesionRow;
import edu.ucjc.privilegium.encapsulaciones.SesionUsuarioLog;
import edu.ucjc.privilegium.entidades.RegistroSesion;
import edu.ucjc.privilegium.entidades.SesionUsuario;
import edu.ucjc.privilegium.repositorios.RegistroSesionRepository;
import edu.ucjc.privilegium.repositorios.SesionUsuarioRepository;
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
    public JpaRepository<SesionUsuario, Long> getRepository() {
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
        //
        RegistroSesion re = new RegistroSesion();
        re.setTipoRegistro(tipoRegistro);
        re.setSesionUsuario(sesionUsuario);
        re.setLog(comando);

        //
        registroSesionRepository.save(re);
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

    public List<RegistroSesionRow> getRegistroBySesion(SesionUsuario sesionUsuario){
        List<RegistroSesionRow> rows = new ArrayList<>();
        registroSesionRepository.findAllBySesionUsuarioOrderByDateCreated(sesionUsuario).forEach(r -> {
            rows.add(new RegistroSesionRow(r.getId(), r.getDateCreated(), r.getTipoRegistro().name(), r.getLog()));
        });
        return rows;
    }
}
