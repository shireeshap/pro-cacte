package gov.nih.nci.ctcae.web.clinicalStaff;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.StudyOrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.helper.ClinicalStaffTestHelper;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.web.AbstractWebIntegrationTestCase;

import java.util.List;
import java.util.Map;

/**
 * @author Mehul Gulati
 *         Date: Nov 5, 2008
 */
public class ClinicalStaffAjaxFacadeIntegrationTest extends AbstractWebIntegrationTestCase {

    private ClinicalStaffAjaxFacade clinicalStaffAjaxFacade;
    protected Map parameterMap;

    public void testFind() {

        List<StudyOrganizationClinicalStaff> organizationClinicalStaffList = clinicalStaffAjaxFacade.matchStudyOrganizationClinicalStaffByStudyOrganizationIdAndRole
                ("%", StudyTestHelper.getDefaultStudy().getLeadStudySite().getId(), "NURSE");

        assertFalse(organizationClinicalStaffList.isEmpty());
    }


    public void setClinicalStaffAjaxFacade(ClinicalStaffAjaxFacade clinicalStaffAjaxFacade) {
        this.clinicalStaffAjaxFacade = clinicalStaffAjaxFacade;
    }

}

