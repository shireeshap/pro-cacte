package gov.nih.nci.ctcae.core.tools.hibernate;

import org.hibernate.cfg.ImprovedNamingStrategy;

/**
 * @author Vinay Kumar
 * @crated Dec 9, 2008
 */
public class WonderfulNamingStrategy extends ImprovedNamingStrategy {
	@Override
	public String foreignKeyColumnName(String propertyName, String propertyEntityName, String propertyTableName, String referencedColumnName) {
		return columnName(propertyName) + "_id";
	}

	@Override
	public String classToTableName(String className) {
		return pluralize(super.classToTableName(className));
	}

	private String pluralize(String name) {
		StringBuilder p = new StringBuilder(name);
		if (name.endsWith("y")) {
			p.deleteCharAt(p.length() - 1);
			p.append("ies");
		} else {
			p.append('s');
		}
		return p.toString();
	}
}

