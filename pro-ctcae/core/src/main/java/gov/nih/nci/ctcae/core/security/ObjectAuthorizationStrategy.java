package gov.nih.nci.ctcae.core.security;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.Authentication;
import org.springframework.security.acls.Permission;
import org.springframework.security.acls.objectidentity.ObjectIdentity;

import java.io.Serializable;
import java.util.List;

/**
 * @author Vinay Kumar
 * @crated Feb 5, 2009
 */
public class ObjectAuthorizationStrategy {


    public Boolean isGranted(Permission[] requirePermission, ObjectIdentity objectIdentity, Authentication authentication) {
        Object principal = authentication.getPrincipal();
        UserDetails userDetails = (UserDetails) principal;

        List<AclEntry> aclEntries = userDetails.getAclEntries();
        for (Permission permission : requirePermission) {
            String pattern = permission.getPattern();
            Serializable identifier = objectIdentity.getIdentifier();

            for (AclEntry aclEntry : aclEntries) {
                if (StringUtils.equals(pattern, aclEntry.getObjectPermission()) && StringUtils.equals(String.valueOf(identifier), aclEntry.getObjectIdentity())) {
                    //add logger here
                    return true;
                }
            }
        }

        //add logger here
        return false;

    }
}
