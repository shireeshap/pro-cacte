package gov.nih.nci.ctcae.web;

import org.springframework.beans.propertyeditors.CustomDateEditor;

import java.beans.PropertyEditor;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * The Class ControllerTools.
 *
 * @author
 */
public class ControllerTools {

    // TODO: make date format externally configurable
    /**
     * Gets the date editor.
     *
     * @param required the required
     * @return the date editor
     */
    public static PropertyEditor getDateEditor(boolean required) {
        // note that date formats are not threadsafe, so we have to create a new one each time
        return new CustomDateEditor(createDateFormat(), !required);
    }

    /**
     * Creates the date format.
     *
     * @return the date format
     */
    public static DateFormat createDateFormat() {
        return new SimpleDateFormat("MM/dd/yyyy");
    }

    /**
     * Instantiates a new controller tools.
     */
    public ControllerTools() {
        super();
    }
}
