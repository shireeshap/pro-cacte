/* Copyright 2004, 2005, 2006 Acegi Technology Pty Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gov.nih.nci.ctcae.core.security;

import org.springframework.security.Authentication;
import org.springframework.security.ConfigAttribute;
import org.springframework.security.acls.AclService;
import org.springframework.security.acls.Permission;
import org.springframework.security.acls.objectidentity.ObjectIdentity;
import org.springframework.security.acls.objectidentity.ObjectIdentityRetrievalStrategy;
import org.springframework.security.acls.sid.SidRetrievalStrategy;
import org.springframework.security.afterinvocation.AfterInvocationProvider;
import org.springframework.util.Assert;


/**
 * Abstract {@link AfterInvocationProvider} which provides commonly-used ACL-related services.
 *
 * @author Ben Alex
 * @version $Id: AbstractAclProvider.java 3132 2008-06-06 03:01:51Z benalex $
 */
public abstract class AbstractAuthorizationProvider extends org.springframework.security.afterinvocation.AclEntryAfterInvocationProvider {
    //~ Instance fields ================================================================================================

    protected ObjectGroupIdentityRetrievalStrategyImpl objectGroupIdentityRetrievalStrategy = new ObjectGroupIdentityRetrievalStrategyImpl();

    private ObjectAuthorizationStrategy objectAuthorizationStrategy;

    //~ Constructors ===================================================================================================

    public AbstractAuthorizationProvider(AclService aclService, String processConfigAttribute, Permission[] requirePermission) {
        super(aclService, requirePermission);
    }

    //~ Methods ========================================================================================================

    protected Class getProcessDomainObjectClass() {
        return processDomainObjectClass;
    }

    protected boolean hasPermission(Authentication authentication, Object domainObject) {
        // Obtain the OID applicable to the domain object
        ObjectIdentity objectIdentity = objectIdentityRetrievalStrategy.getObjectIdentity(domainObject);

        ObjectGroupIdentity objectGroupIdentity = objectGroupIdentityRetrievalStrategy.getObjectGroupIdentity(domainObject);

        //now check if user can access this group..if user can  access this group with the same permission, user can access all childs also for same permission


        //permission are READ or Admin

        Boolean isGranted = objectAuthorizationStrategy.isGranted(requirePermission, objectGroupIdentity, authentication);

        return isGranted ? isGranted : objectAuthorizationStrategy.isGranted(requirePermission, objectIdentity, authentication);


    }

    public void setObjectIdentityRetrievalStrategy(ObjectIdentityRetrievalStrategy objectIdentityRetrievalStrategy) {
        Assert.notNull(objectIdentityRetrievalStrategy, "ObjectIdentityRetrievalStrategy required");
        this.objectIdentityRetrievalStrategy = objectIdentityRetrievalStrategy;
    }

    protected void setProcessConfigAttribute(String processConfigAttribute) {
        Assert.hasText(processConfigAttribute, "A processConfigAttribute is mandatory");
        this.processConfigAttribute = processConfigAttribute;
    }

    public void setProcessDomainObjectClass(Class processDomainObjectClass) {
        Assert.notNull(processDomainObjectClass, "processDomainObjectClass cannot be set to null");
        this.processDomainObjectClass = processDomainObjectClass;
    }

    public void setSidRetrievalStrategy(SidRetrievalStrategy sidRetrievalStrategy) {
        Assert.notNull(sidRetrievalStrategy, "SidRetrievalStrategy required");
        this.sidRetrievalStrategy = sidRetrievalStrategy;
    }

    public boolean supports(ConfigAttribute attribute) {
        return processConfigAttribute.equals(attribute.getAttribute());
    }

    /**
     * This implementation supports any type of class, because it does not query the presented secure object.
     *
     * @param clazz the secure object
     * @return always <code>true</code>
     */
    public boolean supports(Class clazz) {
        return true;
    }
}
