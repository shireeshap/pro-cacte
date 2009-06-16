package gov.nih.nci.ctcae.core.security.beforeinvocation;

import gov.nih.nci.ctcae.core.domain.StudyOrganization;
import gov.nih.nci.ctcae.core.repository.OrganizationClinicalStaffRepository;
import gov.nih.nci.ctcae.core.repository.StudyOrganizationRepository;
import gov.nih.nci.ctcae.core.security.DomainObjectAuthorizationCheck;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.AccessDeniedException;
import org.springframework.security.Authentication;

import java.lang.reflect.Method;

/**
 * @author Vinay Kumar
 * @since Mar 16, 2009
 */
public class OrganizationClinicalStaffAuthorizationCheck implements MethodAuthorizationCheck {

    protected final Log logger = LogFactory.getLog(getClass());

    private StudyOrganizationRepository studyOrganizationRepository;
    private DomainObjectAuthorizationCheck domainObjectAuthorizationCheck;

    public boolean authorize(Authentication authentication, MethodInvocation methodInvocation) throws AccessDeniedException {

        Method method = methodInvocation.getMethod();
        if (method.getName().equals("findByStudyOrganizationId")) {
            logger.debug(String.format("found method findByStudyOrganizationId of %s . Checking if user can see the StudyOrganization or not.",
                    OrganizationClinicalStaffRepository.class));
            Integer studyOrganizationId = (Integer) methodInvocation.getArguments()[1];
            StudyOrganization studyOrganization = studyOrganizationRepository.findById(studyOrganizationId);
            return domainObjectAuthorizationCheck.authorize(authentication, studyOrganization);
        }
        return false;

    }

    @Required
    public void setStudyOrganizationRepository(StudyOrganizationRepository studyOrganizationRepository) {
        this.studyOrganizationRepository = studyOrganizationRepository;
    }

    @Required
    public void setDomainObjectAuthorizationCheck(DomainObjectAuthorizationCheck domainObjectAuthorizationCheck) {
        this.domainObjectAuthorizationCheck = domainObjectAuthorizationCheck;
    }
}
