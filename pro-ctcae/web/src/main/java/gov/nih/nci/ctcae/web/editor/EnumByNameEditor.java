package gov.nih.nci.ctcae.web.editor;

import gov.nih.nci.cabig.ctms.domain.CodedEnum;
import static gov.nih.nci.cabig.ctms.domain.CodedEnumHelper.getByClassAndCode;
import org.apache.commons.lang.StringUtils;

import java.beans.PropertyEditorSupport;

/**
 * @author Vinay Kumar
 * @crated Dec 18, 2008
 */
public class EnumByNameEditor<E extends CodedEnum> extends PropertyEditorSupport {
	Class<E> enumClass;

	public EnumByNameEditor(Class<E> enumClass) {
		this.enumClass = enumClass;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (text == null || StringUtils.isBlank(text)) {
			setValue(null);
		} else {
			CodedEnum code = getByClassAndCode(enumClass, text);

			setValue(code);
		}
	}

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
