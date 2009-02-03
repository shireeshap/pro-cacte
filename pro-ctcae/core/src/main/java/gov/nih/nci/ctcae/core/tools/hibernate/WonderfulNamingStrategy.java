package gov.nih.nci.ctcae.core.tools.hibernate;

import org.hibernate.cfg.ImprovedNamingStrategy;

// TODO: Auto-generated Javadoc
/**
 * The Class WonderfulNamingStrategy.
 * 
 * @author Vinay Kumar
 * @crated Dec 9, 2008
 */
public class WonderfulNamingStrategy extends ImprovedNamingStrategy {
	
	/* (non-Javadoc)
	 * @see org.hibernate.cfg.ImprovedNamingStrategy#foreignKeyColumnName(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String foreignKeyColumnName(String propertyName, String propertyEntityName, String propertyTableName, String referencedColumnName) {
		return columnName(propertyName) + "_id";
	}

	/* (non-Javadoc)
	 * @see org.hibernate.cfg.ImprovedNamingStrategy#classToTableName(java.lang.String)
	 */
	@Override
	public String classToTableName(String className) {
		return pluralize(super.classToTableName(className));
	}

	/**
	 * Pluralize.
	 * 
	 * @param name the name
	 * 
	 * @return the string
	 */
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

