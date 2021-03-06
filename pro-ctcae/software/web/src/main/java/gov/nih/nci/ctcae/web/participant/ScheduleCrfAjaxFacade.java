package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.domain.CtcTerm;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.domain.UserRole;
import gov.nih.nci.ctcae.core.domain.meddra.LowLevelTerm;
import gov.nih.nci.ctcae.core.jdbc.MeddraAutoCompleterDao;
import gov.nih.nci.ctcae.core.jdbc.support.MeddraAutoCompleterWrapper;
import gov.nih.nci.ctcae.core.query.*;
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
import java.util.Objects;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;
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
    private MeddraAutoCompleterDao meddraAutoCompleterDao;

    private Log logger = LogFactory.getLog(ScheduleCrfAjaxFacade.class);
    private static final String ENGLISH = "en";
    private static final String SPANISH = "es";

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
            participantQuery = new ParticipantQuery(false);
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
     * Removes the diacritics. Convert the accented o to o so that the regular 
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
    

    private Integer determineLevenshteinDistance(String input) {

        double distance = 3;

        if(StringUtils.length(input) < distance) {
            return 2; // only when input is of length 3 or less.
        }

        String[] split = StringUtils.split(input);

        for (String string : split)
            distance *= 1.3;


        return (int) Math.floor(distance);
    }

    private String preprocessText(String text) {

        text = StringUtils.lowerCase(text);
        text = StringUtils.replace(text, "in the", "");
        text = StringUtils.replace(text, "in my", "");
        text = StringUtils.replace(text, "on the", "");
        text = StringUtils.replace(text, "on my", "");
        text = StringUtils.replace(text, "of my", "");
        text = StringUtils.replace(text, "of the", "");
        text = StringUtils.replace(text, "around me", "");
        text = StringUtils.replace(text, "around the", "");
        return text;

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
        if (language == null || Objects.equals(language, "")) {
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


        text = preprocessText(text);

        Integer distance = determineLevenshteinDistance(text);

        List<MeddraAutoCompleterWrapper> en = meddraAutoCompleterDao.getMatchingMeddraTerms(text, language, distance, 4);
        for(MeddraAutoCompleterWrapper wrapper : en ) {
            results.add(wrapper.getMeddraTerm());
        }
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
                    return proCtcTerm.getProCtcTermVocab().getTermEnglish();
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
                	if(meddraRepository.isMappedToProCtcTerm(ctcTerms.get(0))){
                		return ctcTerms.get(0).getProCtcTerms().get(0).getProCtcTermVocab().getTermEnglish();
                	}
                } else {
                	if(meddraRepository.isMappedToProCtcTerm(ctcTerms.get(0))){
                		return ctcTerms.get(0).getProCtcTerms().get(0).getProCtcTermVocab().getTermSpanish();
                	}
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
        
        ProCtcTerm proCtcTerm;
        if (language.equals("en")) {
            proCtcTerm = proCtcTermRepository.findProCtcTermBySymptom(text);
        } else {
            proCtcTerm = proCtcTermRepository.findSpanishProTermBySymptom(text);
        }
        
        if(proCtcTerm != null) {
        	if(ENGLISH.equals(language)) {
        		return proCtcTerm.getProCtcTermVocab().getTermEnglish();
        	} else {
        		return proCtcTerm.getProCtcTermVocab().getTermSpanish();
        	}
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
           
            if (ctcTerms != null && ctcTerms.size() > 0 && 
            		meddraRepository.isMappedToProCtcTerm(ctcTerms.get(0)) && "Y".equalsIgnoreCase(ctcTerms.get(0).getProCtcTerms().get(0).getCurrency())) {
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

    @Required
    public void setMeddraAutoCompleterDao(MeddraAutoCompleterDao meddraAutoCompleterDao) {
        this.meddraAutoCompleterDao = meddraAutoCompleterDao;
    }
}