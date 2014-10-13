package gov.nih.nci.ctcae.web.tag;

import org.springframework.web.servlet.tags.form.TagWriter;

import javax.servlet.jsp.JspException;
import java.io.IOException;

/**
 * Use this form tag instead of spring form tag. Note: Works only with Spring WebMVC 2.5.6
 * @author Kishore
 * @since 0.4
 */
public class FormTag extends org.springframework.web.servlet.tags.form.FormTag {

    @Override
    protected void writeDefaultAttributes(TagWriter tagWriter) throws JspException {
        super.writeDefaultAttributes(tagWriter);
        writeOptionalAttribute(tagWriter, "novalidate", "novalidate");
    }

    
}