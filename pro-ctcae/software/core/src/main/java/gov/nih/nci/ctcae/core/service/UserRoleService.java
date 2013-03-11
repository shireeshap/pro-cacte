package gov.nih.nci.ctcae.core.service;

import java.util.List;

import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyOrganizationClinicalStaff;

	public interface UserRoleService {
		void addUserRoleForUpdatedLCRAorPI(Study study);
}
