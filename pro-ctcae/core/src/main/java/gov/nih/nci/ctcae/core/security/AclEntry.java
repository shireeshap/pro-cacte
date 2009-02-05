package gov.nih.nci.ctcae.core.security;

import gov.nih.nci.ctcae.core.domain.BaseVersionable;

/**
 * @author Vinay Kumar
 * @crated Feb 5, 2009
 */
public class AclEntry extends BaseVersionable {


    private String objectIdentity;

    private String objectPermission;

    public String getObjectIdentity() {
        return objectIdentity;
    }

    public void setObjectIdentity(String objectIdentity) {
        this.objectIdentity = objectIdentity;
    }

    public String getObjectPermission() {
        return objectPermission;
    }

    public void setObjectPermission(String objectPermission) {
        this.objectPermission = objectPermission;
    }

    public Integer getId() {
        return null;


    }

    public void setId(Integer id) {


    }
}
