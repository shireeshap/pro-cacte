package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.core.domain.StudySite;
import gov.nih.nci.ctcae.core.query.StudyOrganizationQuery;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.core.repository.OrganizationRepository;
import gov.nih.nci.ctcae.core.repository.ParticipantRepository;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import gov.nih.nci.ctcae.web.ListValues;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Harsh Agarwal
 * @created Oct 21, 2008
 */
public abstract class ParticipantController extends CtcAeSimpleFormController {
    protected ParticipantRepository participantRepository;
    protected FinderRepository finderRepository;
    protected OrganizationRepository organizationRepository;

    protected ParticipantController() {
        setCommandClass(ParticipantCommand.class);
        setCommandName("participantCommand");
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

        for (int studyId : participantCommand.getStudyId()) {

            StudyOrganizationQuery query = new StudyOrganizationQuery();
            query.filterByOrganizationId(participantCommand.getSiteId());
            query.filterByStudyId(studyId);
            query.filterByStudySiteOnly();

            List<StudySite> persistables = (List<StudySite>) finderRepository.find(query);
            if (persistables.isEmpty()) {
                throw new Exception(("can not find study site:siteId- " + participantCommand.getSiteId()) + " study id:" + participantCommand.getStudyId());
            }
            StudySite studySite = persistables.get(0);

            participantCommand.setSiteName(studySite.getOrganization()
                    .getName());

            StudyParticipantAssignment spa = new StudyParticipantAssignment();
            spa.setStudySite(studySite);
            spa.setStudyParticipantIdentifier(request
                    .getParameter("participantStudyIdentifier" + studyId));
            participant.addStudyParticipantAssignment(spa);
        }
        participant = participantRepository.save(participant);
        participantCommand.setParticipant(participant);

        ModelAndView modelAndView = new ModelAndView(getSuccessView());
        modelAndView.addObject("participantCommand", participantCommand);
        return modelAndView;
    }

    @Override
    protected void onBindAndValidate(HttpServletRequest request,
                                     Object command, BindException errors) throws Exception {
        super.onBindAndValidate(request, command, errors);


        ParticipantCommand participantCommand = (ParticipantCommand) command;

        if (participantCommand.getStudyId() == null ||
                participantCommand.getStudyId().length == 0) {
            errors.reject(
                    "studyId", "Please select at least one study.");
        }

    }

    @Override
    protected Map referenceData(HttpServletRequest request, Object command,
                                Errors errors) throws Exception {
        HashMap<String, Object> referenceData = new HashMap<String, Object>();

        ArrayList<Organization> studySites = organizationRepository
                .findOrganizationsForStudySites();

        ListValues listValues = new ListValues();


        referenceData.put("genders", listValues.getGenderType());
        referenceData.put("ethnicities", listValues.getEthnicityType());
        referenceData.put("races", listValues.getRaceType());
        referenceData.put("studysites", ListValues.getStudySites(studySites));
        return referenceData;
    }

    @Required
    public void setParticipantRepository(
            ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    @Required
    public void setOrganizationRepository(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    @Required
    public void setFinderRepository(
            FinderRepository finderRepository) {
        this.finderRepository = finderRepository;
    }

}