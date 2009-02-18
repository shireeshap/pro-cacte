package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.CRFRepository;
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
import java.util.*;

//
/**
 * The Class ParticipantController.
 *
 * @author Harsh Agarwal
 * @created Oct 21, 2008
 */
public abstract class ParticipantController extends CtcAeSimpleFormController {

    /**
     * The participant repository.
     */
    protected ParticipantRepository participantRepository;

    /**
     * The organization repository.
     */
    protected OrganizationRepository organizationRepository;

    /**
     * The crf repository.
     */
    private CRFRepository crfRepository;

    /**
     * Instantiates a new participant controller.
     */
    protected ParticipantController() {
        super();
        setCommandClass(ParticipantCommand.class);
        setCommandName("participantCommand");
        setSuccessView("participant/confirmParticipant");
        setBindOnNewForm(true);
        setSessionForm(true);
    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
     */
    @Override
    protected ModelAndView onSubmit(HttpServletRequest request,
                                    HttpServletResponse response, Object oCommand,
                                    org.springframework.validation.BindException errors)
            throws Exception {

        ParticipantCommand command = (ParticipantCommand) oCommand;
        for (StudySite studySite : command.getStudySite()) {
            command.setSiteName(studySite.getOrganization().getName());
            StudyParticipantAssignment studyParticipantAssignment = command.createStudyParticipantAssignments(studySite, request.getParameter("participantStudyIdentifier_" + studySite.getId()));
            command.assignCrfsToParticipant(studyParticipantAssignment, crfRepository, request);
        }
        participantRepository.save(command.getParticipant());
        return showForm(request, errors, getSuccessView());
    }


    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.web.CtcAeSimpleFormController#onBindAndValidate(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.BindException)
     */
    @Override
    protected void onBindAndValidate(HttpServletRequest request,
                                     Object command, BindException errors) throws Exception {
        super.onBindAndValidate(request, command, errors);


        ParticipantCommand participantCommand = (ParticipantCommand) command;

        if (participantCommand.getStudySite() == null ||
                participantCommand.getStudySite().length == 0) {
            errors.reject(
                    "studyId", "Please select at least one study.");
        }

    }

    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.web.CtcAeSimpleFormController#referenceData(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors)
     */
    @Override
    protected Map referenceData(HttpServletRequest request, Object command,
                                Errors errors) throws Exception {
        HashMap<String, Object> referenceData = new HashMap<String, Object>();

        ArrayList<Organization> organizationsHavingStudySite = organizationRepository
                .findOrganizationsForStudySites();

        ListValues listValues = new ListValues();


        referenceData.put("genders", listValues.getGenderType());
        referenceData.put("ethnicities", listValues.getEthnicityType());
        referenceData.put("races", listValues.getRaceType());
        referenceData.put("organizationsHavingStudySite", ListValues.getOrganizationsHavingStudySite(organizationsHavingStudySite));
        return referenceData;
    }

    /**
     * Sets the crf repository.
     *
     * @param crfRepository the new crf repository
     */
    @Required
    public void setCrfRepository(CRFRepository crfRepository) {
        this.crfRepository = crfRepository;
    }

    /**
     * Sets the participant repository.
     *
     * @param participantRepository the new participant repository
     */
    @Required
    public void setParticipantRepository(
            ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    /**
     * Sets the organization repository.
     *
     * @param organizationRepository the new organization repository
     */
    @Required
    public void setOrganizationRepository(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }
}