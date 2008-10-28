package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.core.query.StudyQuery;
import gov.nih.nci.ctcae.core.repository.OrganizationRepository;
import gov.nih.nci.ctcae.core.repository.ParticipantRepository;
import gov.nih.nci.ctcae.core.repository.StudyRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Required;

/**
 * @author Vinay Kumar
 * @crated Oct 17, 2008
 */
public class StudyAjaxFacade {
	private StudyRepository studyRepository;
	private OrganizationRepository organizationRepository;
	private ParticipantRepository participantRepository;

	public String searchStudies(Map parameterMap, String type, String text,
			HttpServletRequest request) {
		List<Study> studies = getObjects(type, text);

		StudyTableModel studyTableModel = new StudyTableModel();
		String table = studyTableModel.buildStudyTable(parameterMap, studies,
				request);
		return table;
	}

	public String searchStudiesForSelection(Map parameterMap, String type,
			String text, HttpServletRequest request) {
		List<Study> studies = getObjects(type, text);
		StudyTableModel studyTableModel = new StudyTableModel();

		String participantId = (String) parameterMap.get("participant.id");
		HashMap<String, Object> map = new HashMap<String, Object>(parameterMap);

		if (participantId != null) {
			HashSet<Integer> studyIdSet = new HashSet<Integer>();
			HashMap<Integer, String> participantStudyIdentifierMap = new HashMap<Integer,String>();
			Participant participant = participantRepository
					.findById(new Integer(participantId));
			List<StudyParticipantAssignment> spas = participant
					.getStudyParticipantAssignments();
			for (StudyParticipantAssignment spa : spas) {
				studyIdSet.add(spa.getStudySite().getStudy().getId());
				participantStudyIdentifierMap.put(spa.getStudySite().getStudy().getId(),spa.getStudyParticipantIdentifier());
			}
			map.put("participant.studies", studyIdSet);
			map.put("participant.participantstudyidentifier", participantStudyIdentifierMap);
		}

		String table = studyTableModel.buildStudyTableForSelection(map,
				studies, request);
		return table;
	}

	private List<Study> getObjects(String type, String text) {
		StudyQuery studyQuery = new StudyQuery();
		List<Study> studies = new ArrayList<Study>();

		if ("shortTitle".equals(type)) {
			studyQuery.filterStudiesByShortTitle(text);
			studies = (List<Study>) studyRepository.find(studyQuery);
		} else if ("assignedIdentifier".equals(type)) {
			studyQuery.filterStudiesByAssignedIdentifier(text);
			studies = (List<Study>) studyRepository.find(studyQuery);
		} else if ("site".equals(type)) {
			studies = organizationRepository.findStudiesForOrganization(text);
		}

		return studies;
	}

	@Required
	public void setStudyRepository(StudyRepository studyRepository) {
		this.studyRepository = studyRepository;
	}

	@Required
	public void setOrganizationRepository(
			OrganizationRepository organizationRepository) {
		this.organizationRepository = organizationRepository;
	}

	@Required
	public void setParticipantRepository(
			ParticipantRepository participantRepository) {
		this.participantRepository = participantRepository;
	}
}
