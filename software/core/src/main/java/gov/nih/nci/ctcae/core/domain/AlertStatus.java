package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.cabig.ctms.domain.CodedEnum;
import gov.nih.nci.cabig.ctms.domain.CodedEnumHelper;

/**
 * @author Amey
 * AlertStatus enum for system alerts functionality
 */
public enum AlertStatus implements CodedEnum<String> {
	ACTIVE("Active"),
	IN_ACTIVE("Inactive");
	
	private final String displayText;
	
	AlertStatus(String displayText) {
		this.displayText = displayText;
		CodedEnumHelper.register(this);
	}
	
	public static AlertStatus getByCode(String code) {
		return CodedEnumHelper.getByClassAndCode(AlertStatus.class, code);
	}

	@Override
	public String getCode() {
		return getDisplayName();
	}

	@Override
	public String getDisplayName() {
		return displayText;
	}
	
	@Override
	public String toString() {
		return displayText;
	}
	
	public static AlertStatus getByDisplayName(String displayName) {
        for (AlertStatus roleStatus : AlertStatus.values()) {

            if (roleStatus.getDisplayName().equals(displayName)) {
                return roleStatus;
            }
        }
        return null;
    }

}