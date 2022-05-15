package edu.pucmm.webconceptual.util;

import javax.persistence.Id;
import java.lang.reflect.Field;

public class FuncionalidadesHelper {

    public static <T> Field getIdField(Class<T> entityType) {
        for(Field field : entityType.getSuperclass().getDeclaredFields()) {
            if(field.isAnnotationPresent(Id.class)){
                return field;
            }
        }
        for(Field field : entityType.getDeclaredFields()) {
            if(field.isAnnotationPresent(Id.class)){
                return field;
            }
        }
        return null;
    }

    public static <T> T setFieldValue(T entidad, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field;
        try {
            field = entidad.getClass().getSuperclass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException ignored) {
            try {
                field = entidad.getClass().getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                throw e;
            }
        }
        field.setAccessible(true);
        field.set(entidad, value);
        return entidad;
    }
}
