package gov.nih.nci.ctcae.core.tools.commons;

import org.apache.commons.lang.builder.StandardToStringStyle;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Vinay Kumar
 * @crated Feb 9, 2009
 */
public class CtcAeToStringStyle extends StandardToStringStyle {
    public CtcAeToStringStyle() {

        super();
        setUseShortClassName(true);
        setUseIdentityHashCode(false);
    }

    protected void appendDetail(StringBuffer buffer, String fieldName, Object value) {
        if (value instanceof Date) {
            value = new SimpleDateFormat("yyyy-MM-dd").format(value);
        }
        buffer.append(value);
    }

}
