package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.domain.meddra.LowLevelTerm;
import gov.nih.nci.ctcae.core.query.MeddraQuery;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;
import gov.nih.nci.ctcae.core.query.StudyQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.MeddraRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
import gov.nih.nci.ctcae.core.repository.secured.ParticipantRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyRepository;
import gov.nih.nci.ctcae.core.utils.ranking.RankBasedSorterUtils;
import gov.nih.nci.ctcae.core.utils.ranking.Serializer;
import gov.nih.nci.ctcae.web.form.SubmitFormCommand;
import gov.nih.nci.ctcae.web.form.SubmitFormController;
import gov.nih.nci.ctcae.web.tools.ObjectTools;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Required;

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
        studies = RankBasedSorterUtils.sort(studies, text, new Serializer<Study>() {
            public String serialize(Study object) {
                return object.getDisplayName();
            }
        });
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

        participants = RankBasedSorterUtils.sort(participants, text, new Serializer<Participant>() {
            public String serialize(Participant object) {
                return object.getDisplayName();
            }
        });

        return ObjectTools.reduceAll(participants, "id", "firstName", "lastName", "assignedIdentifier", "displayName");
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
        StudyParticipantAssignment spa = submitFormCommand.getSchedule().getStudyParticipantCrf().getStudyParticipantAssignment();
        String language = spa.getHomeWebLanguage();
        if (language == null || language == "") {
            language = SupportedLanguageEnum.ENGLISH.getName();
        }
        List<ProCtcTerm> symptoms = submitFormCommand.getSortedSymptoms();
        List<String> results = new ArrayList<String>();
        for (ProCtcTerm symptom : symptoms) {
            if (symptom.getProCtcTermVocab().getTermEnglish().toLowerCase().contains((text.toLowerCase()))) {
                if (language.equals(SupportedLanguageEnum.ENGLISH.getName())) {
                    results.add(symptom.getProCtcTermVocab().getTermEnglish());
                } else {
                    results.add(symptom.getProCtcTermVocab().getTermSpanish());
                }
            }
        }
        MeddraQuery meddraQuery;
        if (language.equals(SupportedLanguageEnum.ENGLISH.getName())) {
            meddraQuery = new MeddraQuery(true);
        } else {
            meddraQuery = new MeddraQuery(language);
        }
        if (text != null) {
            if (language.equals(SupportedLanguageEnum.ENGLISH.getName())) {
            meddraQuery.filterMeddraWithMatchingText(text);
            } else {
            meddraQuery.filterMeddraWithSpanishText(text);
            }
            List meddraTermsObj = genericRepository.find(meddraQuery);
            List<String> meddraTerms = (List<String>) meddraTermsObj;

//            for (String meddraTerm : meddraTerms) {
//                List<CtcTerm> ctcTerms;
//                if (language.equals(SupportedLanguageEnum.ENGLISH.getName())) {
//                   ctcTerms = meddraRepository.findCtcTermForMeddraTerm(meddraTerm);
//                } else {
//                   ctcTerms = meddraRepository.findCtcTermForSpanishMeddraTerm(meddraTerm);
//                }
//                if (ctcTerms != null) {
//                    for (CtcTerm ctcTerm : ctcTerms) {
//                        if (ctcTerm != null) {
//                            List<ProCtcTerm> proCtcTerms = ctcTerm.getProCtcTerms();
//                            if (proCtcTerms != null && proCtcTerms.size() > 0) {
//                                results.add(meddraTerm + " (" + proCtcTerms.get(0) + ")");
//                            }
//                        }
//                    }
//                }
//            }
            results.addAll(meddraTerms);
        }
        results = RankBasedSorterUtils.sort(results, text, new Serializer<String>() {
            public String serialize(String object) {
                return object;
            }
        });
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
                return proCtcTerm.getCtcTerm().getTerm(SupportedLanguageEnum.ENGLISH);
            }
        }
        LowLevelTerm meddraTerm = submitFormCommand.findMeddraTermBySymptom(text);
        if (meddraTerm != null) {
            List<CtcTerm> ctcTerms = meddraRepository.findCtcTermForMeddraTerm(meddraTerm.getMeddraTerm(SupportedLanguageEnum.ENGLISH));
            if (submitFormCommand.ctcTermAlreadyExistsInForm(ctcTerms)) {
                return ctcTerms.get(0).getTerm(SupportedLanguageEnum.ENGLISH);
            }
        }
        return "";
    }

    public String checkIfSymptomMapsToProctc(HttpServletRequest request, String text) {
        SubmitFormCommand submitFormCommand = (SubmitFormCommand)
                request.getSession().getAttribute(SubmitFormController.class.getName() + ".FORM." + "command");
        LowLevelTerm meddraTerm = submitFormCommand.findMeddraTermBySymptom(text);
        if (meddraTerm != null) {
            List<CtcTerm> ctcTerms = meddraRepository.findCtcTermForMeddraTerm(meddraTerm.getMeddraTerm(SupportedLanguageEnum.ENGLISH));
            if (ctcTerms != null && ctcTerms.size()>0){
               return ctcTerms.get(0).getProCtcTerms().get(0).getTermEnglish(SupportedLanguageEnum.ENGLISH);
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