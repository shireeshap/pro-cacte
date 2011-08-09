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

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.util.WebUtils;

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
//        ParticipantQuery participantQuery = new ParticipantQuery();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        boolean siteStaff = false;
        boolean leadStaff = false;
        for (UserRole userRole : user.getUserRoles()) {
            if (userRole.getRole().equals(Role.SITE_PI) || userRole.getRole().equals(Role.SITE_CRA) || userRole.getRole().equals(Role.NURSE) || userRole.getRole().equals(Role.TREATING_PHYSICIAN)) {
                siteStaff = true;
            } else {
                leadStaff = true;
            }
        }
        ParticipantQuery participantQuery;
        if (leadStaff) {
            participantQuery = new ParticipantQuery();
        } else {
            participantQuery = new ParticipantQuery(true);
        }
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
     * Removes the diacritics. Convert the — to o so that the regular 
     * keyboard can be used to fetch Spanish chars from the auto-completer.
     *
     * @param str the str
     * @return the string
     */
    public String removeDiacritics(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD); 
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
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
        Locale l = (Locale) WebUtils.getSessionAttribute(request, org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
        String language = l.getLanguage();
        if (language == null || language == "") {
            language = "en";
        }
        List<ProCtcTerm> symptoms = submitFormCommand.getSortedSymptoms();
        List<String> results = new ArrayList<String>();
        for (ProCtcTerm symptom : symptoms) {
            if (language.equals("en")) {
            	 if (symptom.getProCtcTermVocab().getTermEnglish().toLowerCase().contains(text.toLowerCase())) {
            		 results.add(symptom.getProCtcTermVocab().getTermEnglish());
            	 }
            } else {
            	 if (symptom.getProCtcTermVocab().getTermSpanish().toLowerCase().contains(text.toLowerCase())) {
            		 results.add(symptom.getProCtcTermVocab().getTermSpanish());
            	 } else if(removeDiacritics(symptom.getProCtcTermVocab().getTermSpanish().toLowerCase()).contains(text.toLowerCase())){
            		 results.add(symptom.getProCtcTermVocab().getTermSpanish());
            	 }
            }
        }
        MeddraQuery meddraQuery;
        if (language.equals("en")) {
            meddraQuery = new MeddraQuery(true);
        } else {
            meddraQuery = new MeddraQuery(language);
        }
        if (text != null) {
            if (language.equals("en")) {
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
        Locale l = (Locale) WebUtils.getSessionAttribute(request, org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
        String language = l.getLanguage();
        if (language == null || language == "") {
            language = "en";
        }
        submitFormCommand.setLanguage(language);
        ProCtcTerm proCtcTerm;
        if (language.equals("en")) {
            proCtcTerm = proCtcTermRepository.findProCtcTermBySymptom(text);
        } else {
            proCtcTerm = proCtcTermRepository.findSpanishProTermBySymptom(text);
        }
        if (proCtcTerm != null) {
            List<CtcTerm> ctcTerms = new ArrayList<CtcTerm>();
            ctcTerms.add(proCtcTerm.getCtcTerm());
            if (submitFormCommand.ctcTermAlreadyExistsInForm(ctcTerms)) {
                if (language.equals("en")) {
                    return proCtcTerm.getCtcTerm().getTerm(SupportedLanguageEnum.ENGLISH);
                } else {
                    return proCtcTerm.getProCtcTermVocab().getTermSpanish();
                }
            }
        }
        LowLevelTerm meddraTerm = submitFormCommand.findMeddraTermBySymptom(text);
        if (meddraTerm != null) {
            List<CtcTerm> ctcTerms;
            if (language.equals("en")) {
                ctcTerms = meddraRepository.findCtcTermForMeddraTerm(meddraTerm.getMeddraTerm(SupportedLanguageEnum.ENGLISH));
            } else {
                ctcTerms = meddraRepository.findCtcTermForSpanishMeddraTerm(meddraTerm.getMeddraTerm(SupportedLanguageEnum.SPANISH));
            }
            if (submitFormCommand.ctcTermAlreadyExistsInForm(ctcTerms)) {
                if (language.equals("en")) {
                    return ctcTerms.get(0).getTerm(SupportedLanguageEnum.ENGLISH);
                } else {
                    return ctcTerms.get(0).getProCtcTerms().get(0).getProCtcTermVocab().getTermSpanish();
                }
            }
        }
        return "";
    }

    public String checkIfSymptomMapsToProctc(HttpServletRequest request, String text) {

        SubmitFormCommand submitFormCommand = (SubmitFormCommand)
                request.getSession().getAttribute(SubmitFormController.class.getName() + ".FORM." + "command");

        Locale l = (Locale) WebUtils.getSessionAttribute(request, org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
        String language = l.getLanguage();
        if (language == null || language == "") {
            language = "en";
        }
        LowLevelTerm meddraTerm = submitFormCommand.findMeddraTermBySymptom(text);
        submitFormCommand.setLanguage(language);

        if (meddraTerm != null) {
            List<CtcTerm> ctcTerms;
            if (language.equals("en")) {
                ctcTerms = meddraRepository.findCtcTermForMeddraTerm(meddraTerm.getMeddraTerm(SupportedLanguageEnum.ENGLISH));
            } else {
                ctcTerms = meddraRepository.findCtcTermForSpanishMeddraTerm(meddraTerm.getMeddraTerm(SupportedLanguageEnum.SPANISH));
            }
            if (ctcTerms != null && ctcTerms.size() > 0) {
                if (language.equals("en")) {
                    return ctcTerms.get(0).getProCtcTerms().get(0).getTermEnglish(SupportedLanguageEnum.ENGLISH);
                } else {
                    return ctcTerms.get(0).getProCtcTerms().get(0).getProCtcTermVocab().getTermSpanish();
                }
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