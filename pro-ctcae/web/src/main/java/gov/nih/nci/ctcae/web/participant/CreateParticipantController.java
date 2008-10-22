package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.Ethnicity;
import gov.nih.nci.ctcae.core.domain.Gender;
import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.Race;
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
		setCommandClass(Participant.class);
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

		Participant participant = (Participant) oCommand;

		participant = participantRepository.save(participant);
		ModelAndView modelAndView = new ModelAndView(getSuccessView());
		modelAndView.addObject("participantCommand", participant);
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

		ArrayList<Organization> studySiteOrganizations = studyOrganizationRepository
		.findStudySiteOrganizations();
		
		referenceData.put("genders", gender);
		referenceData.put("ethnicities", ethnicity);
		referenceData.put("races", race);
		referenceData.put("studysites", studySiteOrganizations);
		return referenceData;
	}

	@Override
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		Participant participant = new Participant();
		return participant;
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