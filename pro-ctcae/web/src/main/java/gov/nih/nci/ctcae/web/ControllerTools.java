package gov.nih.nci.ctcae.web;

import gov.nih.nci.ctcae.web.editor.EnumByNameEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.binding.convert.converters.Converter;
import org.springframework.binding.convert.converters.StringToDate;

import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyEditor;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author
 */
public class ControllerTools {
    private static ThreadLocal<DateFormat> dateFormat = new ThreadLocal<DateFormat>();

    // TODO: make date format externally configurable
    public static PropertyEditor getDateEditor(boolean required) {
        // note that date formats are not threadsafe, so we have to create a new one each time
        return new CustomDateEditor(createDateFormat(), !required);
    }

    // TODO: make date format externally configurable
    public static DateFormat createDateFormat() {
        return new SimpleDateFormat("MM/dd/yyyy");
    }

    public static String formatDate(Date date) {
        if (dateFormat.get() == null) {
            dateFormat.set(createDateFormat());
        }
        return dateFormat.get().format(date);
    }


    public static <E extends Enum<E>> void registerEnumEditor(ServletRequestDataBinder binder,
                                                              Class<E> enumClass) {
        binder.registerCustomEditor(enumClass, new EnumByNameEditor<E>(enumClass));
    }

    /**
     * Determine whether the given request was made via an asynchronous request mechanism. Current
     * implementation works for prototype.js-initiated requests only.
     *
     * @param request
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        String header = request.getHeader("X-Requested-With");
        return header != null && "XMLHttpRequest".equals(header);
    }

    public static Converter getDateConverter() {
        StringToDate stringToDate = new StringToDate();
        stringToDate.setPattern("MM/dd/yyyy");

        return stringToDate;
    }

    public ControllerTools() {
    }
}
