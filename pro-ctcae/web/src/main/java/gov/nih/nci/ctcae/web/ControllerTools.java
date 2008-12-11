package gov.nih.nci.ctcae.web;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.binding.convert.converters.Converter;
import org.springframework.binding.convert.converters.StringToDate;

import java.beans.PropertyEditor;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

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


//	/**
//	 * Determine whether the given request was made via an asynchronous request mechanism. Current
//	 * implementation works for prototype.js-initiated requests only.
//	 *
//	 * @param request
//	 */
//	public static boolean isAjaxRequest(HttpServletRequest request) {
//		String header = request.getHeader("X-Requested-With");
//		return header != null && "XMLHttpRequest".equals(header);
//	}

	public static Converter getDateConverter() {
		StringToDate stringToDate = new StringToDate();
		stringToDate.setPattern("MM/dd/yyyy");

		return stringToDate;
	}

	public ControllerTools() {
	}
}
