package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.CtcTerm;
import gov.nih.nci.ctcae.core.domain.meddra.LowLevelTerm;
import gov.nih.nci.ctcae.core.query.MeddraQuery;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;
import gov.nih.nci.ctcae.core.query.StudyQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
import gov.nih.nci.ctcae.core.repository.MeddraRepository;
import gov.nih.nci.ctcae.core.repository.secured.ParticipantRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyRepository;
import gov.nih.nci.ctcae.web.form.SubmitFormCommand;
import gov.nih.nci.ctcae.web.form.SubmitFormController;
import gov.nih.nci.ctcae.web.tools.ObjectTools;
import org.springframework.beans.factory.annotation.Required;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//

/**
 * The Class ScheduleCrfAjaxFacade.
 *
 * @author Harsh Agarwal
 * @created Date: Oct 23, 2008
 */
public class ScheduleCrfAjaxFacade {

    /**
     * The participant repository.
     */
    private ParticipantRepository participantRepository;

    /**
     * The study repository.
     */
    private StudyRepository studyRepository;
    private GenericRepository genericRepository;
    private ProCtcTermRepository proCtcTermRepository;
    private MeddraRepository meddraRepository;

    /**
     * Match studies.
     *
     * @param text          the text
     * @param participantId the participant id
     * @return the list< study>
     */
    public List<Study> matchStudies(String text, Integer participantId) {
        StudyQuery studyQuery = new StudyQuery();
        studyQuery.filterStudiesWithMatchingText(text);
        if (participantId != null) {
            studyQuery.filterByParticipant(participantId);
        }
        List<Study> studies = (List<Study>) studyRepository.find(studyQuery);
        return ObjectTools.reduceAll(studies, "id", "shortTitle", "assignedIdentifier");
    }

    /**
     * Match participants.
     *
     * @param text    the text
     * @param studyId the study id
     * @return the list< participant>
     */
    public List<Participant> matchParticipants(String text, Integer studyId) {
        ParticipantQuery participantQuery = new ParticipantQuery();
        participantQuery.filterParticipantsWithMatchingText(text);
        participantQuery.filterByStudy(studyId);
        List<Participant> participants = (List<Participant>) participantRepository.find(participantQuery);
        return ObjectTools.reduceAll(participants, "id", "firstName", "lastName", "assignedIdentifier");
    }

    /**
     * Match Symptoms.
     *
     * @param text the text
     * @return the list< participant>
     */
    public List<String> matchSymptoms(HttpServletRequest request, String text) {
        SubmitFormCommand submitFormCommand = (SubmitFormCommand)
                request.getSession().getAttribute(SubmitFormController.class.getName() + ".FORM." + "command");
        List<ProCtcTerm> symptoms = submitFormCommand.getSortedSymptoms();
        List<String> results = new ArrayList<String>();
        for (ProCtcTerm symptom : symptoms) {
            if (symptom.getTerm().toLowerCase().contains((text.toLowerCase()))) {
                results.add(symptom.getTerm());
            }
        }
        MeddraQuery meddraQuery = new MeddraQuery(true);
        if (text != null) {
            meddraQuery.filterMeddraWithMatchingText(text);
            List meddraTermsObj = genericRepository.find(meddraQuery);
            List<String> meddraTerms = (List<String>) meddraTermsObj;

            results.addAll(meddraTerms);
            Collections.sort(results);
        }
        return results;
    }

    public String checkIfSymptomAlreadyExistsInForm(HttpServletRequest request, String text) {
        SubmitFormCommand submitFormCommand = (SubmitFormCommand)
                request.getSession().getAttribute(SubmitFormController.class.getName() + ".FORM." + "command");
        ProCtcTerm proCtcTerm = proCtcTermRepository.findProCtcTermBySymptom(text);
        if (proCtcTerm != null) {
            List<CtcTerm> ctcTerms = new ArrayList<CtcTerm>();
            ctcTerms.add(proCtcTerm.getCtcTerm());
            if (submitFormCommand.ctcTermAlreadyExistsInForm(ctcTerms)) {
                return proCtcTerm.getCtcTerm().getTerm();
            }
        }
        LowLevelTerm meddraTerm = submitFormCommand.findMeddraTermBySymptom(text);
        if (meddraTerm != null) {
            List<CtcTerm> ctcTerms = meddraRepository.findCtcTermForMeddraTerm(meddraTerm.getMeddraTerm());
            if (submitFormCommand.ctcTermAlreadyExistsInForm(ctcTerms)) {
                return ctcTerms.get(0).getTerm();
            }
        }
        return "";
    }


    /**
     * Sets the participant repository.
     *
     * @param participantRepository the new participant repository
     */
    @Required
    public void setParticipantRepository
            (
                    ParticipantRepository
                            participantRepository) {
        this.participantRepository = participantRepository;
    }

    /**
     * Sets the study repository.
     *
     * @param studyRepository the new study repository
     */
    @Required
    public void setStudyRepository
            (StudyRepository
                    studyRepository) {
        this.studyRepository = studyRepository;
    }

    @Required
    public void setGenericRepository
            (GenericRepository
                    genericRepository) {
        this.genericRepository = genericRepository;
    }

    @Required
    public void setProCtcTermRepository
            (ProCtcTermRepository
                    proCtcTermRepository) {
        this.proCtcTermRepository = proCtcTermRepository;
    }

    @Required
    public void setMeddraRepository(MeddraRepository meddraRepository) {
        this.meddraRepository = meddraRepository;
    }
}