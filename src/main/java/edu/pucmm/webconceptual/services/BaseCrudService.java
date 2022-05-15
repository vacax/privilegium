package edu.pucmm.webconceptual.services;


import edu.pucmm.webconceptual.util.FuncionalidadesHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

public abstract class BaseCrudService<T, ID> {

    private final Logger logger =  LoggerFactory.getLogger(BaseCrudService.class);

    protected abstract JpaRepository<T, ID> getRepository();

    public Optional<T> get(ID id) {
        return this.getRepository().findById(id);
    }

    public T save(T entity) {
        return this.getRepository().save(entity);
    }

    public Page<T> list(Pageable pageable) {
        return this.getRepository().findAll(pageable);
    }

    public List<T> findAll(){
        return this.getRepository().findAll();
    }

    public int count() {
        return (int)this.getRepository().count();
    }

    public void delete(T entidad){
        this.getRepository().delete(entidad);
    }

    public void deleteById(ID id) {
        this.getRepository().deleteById(id);
    }

    public void anular(T entidad) {
        Field idField= FuncionalidadesHelper.getIdField(entidad.getClass());
        if (idField != null) {
            idField.setAccessible(true);
            try {
                Object idValue = idField.get(entidad);
                anularById((ID) idValue);
            } catch (IllegalAccessException e) {
                logger.error("Error obteniendo el valor del campo @ID de la entidad " + entidad.getClass().getName() + " " + e.getMessage());
            }
        } else {
            logger.error("Imposible obtener el campo @ID de " + entidad.getClass().getName());
        }
    }

    public void anularById(ID id) {
        Optional<T> optionalT = get(id);
        if (optionalT.isPresent()) {
            T entidad = optionalT.get();
            try {
                save(FuncionalidadesHelper.setFieldValue(entidad, "habilitado", false));
            } catch (NoSuchFieldException e) {
                logger.error("La clase " + entidad.getClass().getName() + " no tiene el atributo habilitado!");
            } catch (IllegalAccessException e) {
                logger.error("La clase " + entidad.getClass().getName() + " no tiene el atributo habilitado accesible!");
            }
        } else {
            logger.error("Se esta intentando anular una entidad que no existe...!");
        }
    }

    public T update(T entity) {
        return this.getRepository().save(entity);
    }

    public void deleteEntidad(T entidad){
        this.getRepository().delete(entidad);
    }
}
