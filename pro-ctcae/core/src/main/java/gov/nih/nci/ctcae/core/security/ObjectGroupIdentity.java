package gov.nih.nci.ctcae.core.security;

import org.springframework.security.acls.objectidentity.ObjectIdentity;

/**
 * @author Vinay Kumar
 * @crated Feb 5, 2009
 */
public interface ObjectGroupIdentity extends ObjectIdentity {

    boolean equals(java.lang.Object o);

    java.io.Serializable getIdentifier();

    java.lang.Class getJavaType();

    int hashCode();

}
