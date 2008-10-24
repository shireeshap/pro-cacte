package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.Ethnicity;
import gov.nih.nci.ctcae.core.domain.Gender;
import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.Race;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.core.domain.StudySite;
import gov.nih.nci.ctcae.core.query.StudySiteQuery;
import gov.nih.nci.ctcae.core.repository.ParticipantRepository;
import gov.nih.nci.ctcae.core.repository.StudyOrganizationRepository;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;


/**
 * @author Harsh Agarwal
 * @created Oct 21, 2008
 */
public class CreateParticipantController extends CtcAeSimpleFormController {
	private ParticipantRepository participantRepository;
	private StudyOrganizationRepository studyOrganizationRepository;
	
	public CreateParticipantController() {
		setCommandClass(ParticipantCommand.class);
		setCommandName("participantCommand");
		setFormView("participant/createParticipant");
		setSuccessView("participant/confirmParticipant");
		setBindOnNewForm(true);
		setSessionForm(true);
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object oCommand,
			org.springframework.validation.BindException errors)
			throws Exception {

		ParticipantCommand participantCommand = (ParticipantCommand) oCommand;
		Participant participant = participantCommand.getParticipant();

		StudySiteQuery query = new StudySiteQuery();
		query.filterByOrganizationId(participantCommand.getSiteId());
		query.filterByStudyId(participantCommand.getStudyId());		
		
		StudySite studySite = (StudySite)studyOrganizationRepository.findSingle(query);
		
		StudyParticipantAssignment spa = new StudyParticipantAssignment();
		spa.setStudySite(studySite);
		participant.addStudyParticipantAssignment(spa);
		
		participant = participantRepository.save(participant);
		participantCommand.setParticipant(participant);
		
		ModelAndView modelAndView = new ModelAndView(getSuccessView());
		modelAndView.addObject("participantCommand", participantCommand);
		return modelAndView;
	}

	@Override
	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) throws Exception {
		HashMap<String, Object> referenceData = new HashMap<String, Object>();

		ArrayList<Gender> gender = new ArrayList<Gender>();
		for (Gender value : Gender.values()) {
			gender.add(value);
		}

		ArrayList<Ethnicity> ethnicity = new ArrayList<Ethnicity>();
		for (Ethnicity value : Ethnicity.values()) {
			ethnicity.add(value);
		}

		ArrayList<Race> race = new ArrayList<Race>();
		for (Race value : Race.values()) {
			race.add(value);
		}

		ArrayList<Organization> studySites = studyOrganizationRepository
		.findStudySites();
		
		referenceData.put("genders", gender);
		referenceData.put("ethnicities", ethnicity);
		referenceData.put("races", race);
		referenceData.put("studysites", studySites);
		return referenceData;
	}

	@Override
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		ParticipantCommand participantCommand = new ParticipantCommand();
		return participantCommand;
	}

	@Required
	public void setParticipantRepository(
			ParticipantRepository participantRepository) {
		this.participantRepository = participantRepository;
	}

	@Required
	public void setStudyOrganizationRepository(
			StudyOrganizationRepository studyOrganizationRepository) {
		this.studyOrganizationRepository = studyOrganizationRepository;
	}

}