package gov.nih.nci.ctcae.web.editor;

import gov.nih.nci.cabig.ctms.domain.CodedEnum;
import static gov.nih.nci.cabig.ctms.domain.CodedEnumHelper.getByClassAndCode;
import org.apache.commons.lang.StringUtils;

import java.beans.PropertyEditorSupport;

//
/**
 * The Class EnumByNameEditor.
 *
 * @author Vinay Kumar
 * @since Dec 18, 2008
 */
public class EnumByNameEditor<E extends CodedEnum> extends PropertyEditorSupport {

    /**
     * The enum class.
     */
    Class<E> enumClass;

    /**
     * Instantiates a new enum by name editor.
     *
     * @param enumClass the enum class
     */
    public EnumByNameEditor(Class<E> enumClass) {
        super();
        this.enumClass = enumClass;
    }

    /* (non-Javadoc)
      * @see java.beans.PropertyEditorSupport#setAsText(java.lang.String)
      */
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text == null || StringUtils.isBlank(text)) {
            setValue(null);
        } else {
            CodedEnum code = getByClassAndCode(enumClass, text);

            setValue(code);
        }
    }

    /* (non-Javadoc)
      * @see java.beans.PropertyEditorSupport#getAsText()
      */
    @Override
    @SuppressWarnings("unchecked")
    public String getAsText() {
        Object o = getValue();
        if (o == null) {
            return null;
        } else {
            return o.toString();
		}
	}
}
