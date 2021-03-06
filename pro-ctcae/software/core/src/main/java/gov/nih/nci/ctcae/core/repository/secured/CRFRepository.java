package gov.nih.nci.ctcae.core.repository.secured;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.CRFCalendar;
import gov.nih.nci.ctcae.core.domain.CRFCycleDefinition;
import gov.nih.nci.ctcae.core.domain.CRFPage;
import gov.nih.nci.ctcae.core.domain.CrfPageItem;
import gov.nih.nci.ctcae.core.domain.CrfPageItemDisplayRule;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.FormArmSchedule;
import gov.nih.nci.ctcae.core.domain.MeddraQuestion;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.domain.Question;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrf;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfAddedQuestion;
import gov.nih.nci.ctcae.core.domain.StudySite;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.query.CRFQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.Repository;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

//

/**
 * The Class CRFRepository.
 *
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class CRFRepository implements Repository<CRF, CRFQuery> {

    /**
     * The study repository.
     */
    private StudyRepository studyRepository;

    private GenericRepository genericRepository;


    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public CRF findById(Integer crfId) {
        CRF crf = genericRepository.findById(CRF.class, crfId);
        initializeCollections(crf);
        return crf;
    }

    /**
     * Update status to released.
     *
     * @param
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public CRF updateStatusToReleased(Integer crfId, Date effectiveStartDate) throws ParseException {
        CRF crf = findById(crfId);
        crf.setEffectiveStartDate(effectiveStartDate);
            Study study = studyRepository.findById(crf.getStudy().getId());
            for (StudySite studySite : study.getStudySites()) {
                for (StudyParticipantAssignment studyParticipantAssignment : studySite.getStudyParticipantAssignments()) {
                    StudyParticipantCrf studyParticipantCrf = removeOldStudyParticipantCrfsAndCreateNew(crf, studyParticipantAssignment);
            }
        }
        crf.setStatus(CrfStatus.RELEASED);
        return save(crf);
    }


    private StudyParticipantCrf removeOldStudyParticipantCrfsAndCreateNew(CRF crf, StudyParticipantAssignment studyParticipantAssignment) {
        StudyParticipantCrf newStudyParticipantCrf = new StudyParticipantCrf();
        newStudyParticipantCrf.setStartDate(studyParticipantAssignment.getStudyStartDate());
        newStudyParticipantCrf.setStudyParticipantAssignment(studyParticipantAssignment);
        newStudyParticipantCrf.setArm(studyParticipantAssignment.getArm());
        crf.addStudyParticipantCrf(newStudyParticipantCrf);

        for (StudyParticipantCrf studyParticipantCrf : studyParticipantAssignment.getStudyParticipantCrfs()) {
            if (isOlderVersionStudyParticipantCrf(crf, studyParticipantCrf)) {
                studyParticipantCrf.removeCrfSchedules(CrfStatus.SCHEDULED, crf.getEffectiveStartDate());
                addParticipantAddedQuestionsToStudyParticipantCrf(crf, studyParticipantCrf, newStudyParticipantCrf);
                genericRepository.create(studyParticipantCrf);
            }
        }
        return newStudyParticipantCrf;
    }

    private void addParticipantAddedQuestionsToStudyParticipantCrf(CRF crf, StudyParticipantCrf oldStudyParticipantCrf, StudyParticipantCrf newStudyParticipantCrf) {
        Hashtable<String, Integer> symptomPage = new Hashtable<String, Integer>();
        int i = 0;
        for (StudyParticipantCrfAddedQuestion studyParticipantCrfAddedQuestion : oldStudyParticipantCrf.getStudyParticipantCrfAddedQuestions()) {
            boolean isAlreadyPresent = false;
            for (CRFPage crfPage : crf.getCrfPagesSortedByPageNumber()) {
                for (CrfPageItem crfPageItem : crfPage.getCrfPageItems()) {
                    if (crfPageItem.getProCtcQuestion().equals(studyParticipantCrfAddedQuestion.getProCtcQuestion())) {
                        isAlreadyPresent = true;
                        break;
                    }
                }
            }
            if (!isAlreadyPresent) {
                ProCtcQuestion proCtcQuestion = studyParticipantCrfAddedQuestion.getProCtcQuestion();
                MeddraQuestion meddraQuestion = studyParticipantCrfAddedQuestion.getMeddraQuestion();
                if (proCtcQuestion != null) {
                	String englishVocabTerm = proCtcQuestion.getProCtcTerm().getProCtcTermVocab().getTermEnglish();
                	i = addQuestion(proCtcQuestion, newStudyParticipantCrf, englishVocabTerm, symptomPage, crf, i);
                }else if(meddraQuestion != null){
                	String englishVocabTerm = meddraQuestion.getLowLevelTerm().getLowLevelTermVocab().getMeddraTermEnglish();
                	i = addQuestion(meddraQuestion, newStudyParticipantCrf, englishVocabTerm, symptomPage, crf, i);
                }
            }
        }
    }
    
    
	private int addQuestion(Question question, StudyParticipantCrf newStudyParticipantCrf, String englishVocabTerm, 
			Hashtable<String, Integer> symptomPage, CRF crf, int i) {
		int myPageNumber;
		
		/*
		 * All the questions (i.e questions of each question types:
		 * severity/frequency/presentAbsent and so on) corresponding to a single
		 * proCtcTerm or ctcTerm should be displayed on one page. 
		 * SymptomPage map used to get correct page number for displaying question based on the proCtc/ctc Term.
		 */
		if (symptomPage.containsKey(englishVocabTerm)){
             myPageNumber = symptomPage.get(englishVocabTerm);
         } else {
             myPageNumber = crf.getCrfPagesSortedByPageNumber().size() + i;
             symptomPage.put(englishVocabTerm, myPageNumber);
         }
         i++;
         newStudyParticipantCrf.addStudyParticipantCrfAddedQuestion(question, myPageNumber);
         return i;
    }

    private boolean isOlderVersionStudyParticipantCrf(CRF crf, StudyParticipantCrf studyParticipantCrf) {
        CRF parent = crf;
        if (parent.getParentCrf() != null) {
            parent = parent.getParentCrf();
            if (studyParticipantCrf.getCrf().equals(parent)) {
                return true;
            }
        }
        return false;
    }

    /* (non-Javadoc)
    * @see gov.nih.nci.ctcae.core.repository.AbstractRepository#save(gov.nih.nci.ctcae.core.domain.Persistable)
    */

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public CRF save(CRF crf) {
        crf.setActivityDate(new Date());
        crf = genericRepository.save(crf);
        initializeCollections(crf);
        return crf;

    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(CRF crf) {
        if (crf != null) {
            if (!crf.isReleased()) {
                CRF parentCrf = crf.getParentCrf();
                if (parentCrf != null) {
                    parentCrf.setChildCrf(crf.getChildCrf());
                    genericRepository.save(parentCrf);
                    parentCrf = findById(parentCrf.getId());
                }

                CRF childCrf = crf.getChildCrf();
                if (childCrf != null) {
                    childCrf.setParentCrf(crf.getParentCrf());
                    genericRepository.save(childCrf);
                    childCrf = findById(childCrf.getId());
                }
                crf.setParentCrf(null);
                crf.setChildCrf(null);
                crf.getStudyParticipantCrfs().clear();
                crf = genericRepository.save(crf);
                crf = findById(crf.getId());
                genericRepository.delete(crf);
            } else {
                throw new CtcAeSystemException("Released CRF can not be deleted");
            }
        }
    }

    public Collection<CRF> find(CRFQuery query) {
        return genericRepository.find(query);


    }

    public CRF findSingle(CRFQuery query) {
        return genericRepository.findSingle(query);


    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public CRF
    versionCrf(CRF crf) {
        crf = findById(crf.getId());
        String newVersion = "" + (new Float(crf.getCrfVersion()) + 1);
        CRF copiedCRF = crf.copy();
        copiedCRF.setTitle(crf.getTitle());
        copiedCRF.setCrfVersion(newVersion);
        copiedCRF.setParentCrf(crf);
        copiedCRF.setEq5d(crf.getEq5d());
        copiedCRF = save(copiedCRF);
        crf.setChildCrf(copiedCRF);
        crf = save(crf);
        return copiedCRF;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public CRF copy(CRF crf) {
        CRF copiedCrf = crf.copy();
        copiedCrf.setCrfVersion("1.0");
        return save(copiedCrf);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    private void initializeCollections(CRF crf) {
        for (StudyParticipantCrf studyParticipantCrf : crf.getStudyParticipantCrfs()) {
            studyParticipantCrf.getCrf();
        }

        List<CRFPage> crfPageList = crf.getCrfPagesSortedByPageNumber();
        for (CRFPage crfPage : crfPageList) {
            crfPage.getDescription();
            for (CrfPageItem crfPageItem : crfPage.getCrfPageItems()) {
                crfPageItem.getDisplayOrder();
                for (CrfPageItemDisplayRule crfPageItemDisplayRule : crfPageItem.getCrfPageItemDisplayRules()) {
                    crfPageItemDisplayRule.getProCtcValidValue();
                }
            }
        }

        List<CrfPageItem> allCrfPageItems = crf.getAllCrfPageItems();
        for (CrfPageItem crfPageItem : allCrfPageItems) {
            crfPageItem.getProCtcQuestion().getProCtcTerm().getProCtcQuestions();
        }


        for (FormArmSchedule formArmSchedule : crf.getFormArmSchedules()) {
            for (CRFCalendar crfCalendar : formArmSchedule.getCrfCalendars()) {
                crfCalendar.getDueDateValue();
            }
            for (CRFCycleDefinition crfCycleDefinition : formArmSchedule.getCrfCycleDefinitions()) {
                crfCycleDefinition.getCrfCycles();
            }
        }
    }

    public Long findWithCount(CRFQuery query) {
        return genericRepository.findWithCount(query);
    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }

    /**
     * Sets the study repository.
     *
     * @param studyRepository the new study repository
     */
    @Required
    public void setStudyRepository(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }


}
