package edu.ucjc.privilegium.util;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextArea;
import de.codecamp.vaadin.components.messagedialog.MessageDialog;
import org.apache.commons.lang3.exception.ExceptionUtils;

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

    /**
     * Visualiza el mensaje de error.
     * @param titulo
     * @param mensaje
     * @param exception
     */
    public static void mostrarMensajeError(String titulo, String mensaje,Class clase ,Exception exception){
        MessageDialog dialog = new MessageDialog();
        dialog.setTitle(titulo, VaadinIcon.WARNING.create());
        dialog.setMessage(mensaje);

        dialog.addButtonToLeft().text("Detalle del Error").title("Tooltip").icon(VaadinIcon.ARROW_DOWN)
                .toggleDetails();
        dialog.addButton().text("Aceptar").icon(VaadinIcon.EXIT).primary().onClick(buttonClickEvent -> {
            dialog.close();
            UI.getCurrent().navigate(clase);
        });

        TextArea detailsText = new TextArea();
        detailsText.setWidthFull();
        detailsText.setMaxHeight("15em");
        detailsText.setReadOnly(true);
        detailsText.setValue(ExceptionUtils.getStackTrace(exception));
        dialog.getDetails().add(detailsText);

        dialog.open();

    }

}
