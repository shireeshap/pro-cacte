package gov.nih.nci.ctcae.web.tags;

import org.springframework.web.servlet.tags.form.AbstractDataBoundFormElementTag;
import org.springframework.web.servlet.tags.form.TagWriter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import java.io.IOException;

/**
 * This tag will print the value of a path evaluated against the command object.
 * &lt;caaersTag:value path=&quots;....&quots; /&gt;
 *
 * @author Vinay Kumar
 * @crated Nov 6, 2008
 */

public class ValueTag extends AbstractDataBoundFormElementTag {

    @Override
    protected int writeTagContent(TagWriter writer) throws JspException {
        try {
            pageContext.getOut().write(getDisplayString(getBoundValue(), getPropertyEditor()));
        } catch (IOException e) {
            throw new JspTagException(e.getMessage());
        }
        return EVAL_PAGE;
    }


}
