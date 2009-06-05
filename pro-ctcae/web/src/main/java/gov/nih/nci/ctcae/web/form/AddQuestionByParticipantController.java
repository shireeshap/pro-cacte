package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.domain.meddra.LowLevelTerm;
import gov.nih.nci.ctcae.core.query.*;
import gov.nih.nci.ctcae.core.repository.*;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Hashtable;
import java.util.List;
import java.util.Collection;
import java.util.ArrayList;

//
/**
 * Author: Harsh Agarwal
 * Date: Dec 10, 2008
 * Time: 1:36:54 PM.
 */
public class AddQuestionByParticipantController extends CtcAeSimpleFormController {

    private StudyParticipantCrfRepository studyParticipantCrfRepository;
    private StudyParticipantCrfAddedQuestionRepository studyParticipantCrfAddedQuestionRepository;
    private StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository;
    private MeddraRepository meddraRepository;
    private CtcTermRepository ctcTermRepository;
    private ProCtcTermRepository proCtcTermRepository;
    private GenericRepository genericRepository;

    private StudyParticipantCrfScheduleAddedQuestionRepository studyParticipantCrfScheduleAddedQuestionRepository;


    /**
     * The review view.
     */
    private String reviewView;

    /**
     * Instantiates a new adds the question by participant controller.
     */
    public AddQuestionByParticipantController() {
        super();
        setFormView("form/addQuestionForParticipant");
        setSuccessView("form/confirmFormSubmission");
        setReviewView("form/reviewFormSubmission");
    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
     */
    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        StudyParticipantCrfSchedule studyParticipantCrfSchedule = ((SubmitFormCommand) command).getStudyParticipantCrfSchedule();
        studyParticipantCrfSchedule = studyParticipantCrfScheduleRepository.findById(studyParticipantCrfSchedule.getId());
        StudyParticipantCrf studyParticipantCrf = studyParticipantCrfRepository.findById(studyParticipantCrfSchedule.getStudyParticipantCrf().getId());
        int pageNumber = ((SubmitFormCommand) command).getTotalPages();
        request.getSession().setAttribute("questionstobedeletedlist", ((SubmitFormCommand) command).getQuestionsToBeDeleted());
        if ("continue".equals(((SubmitFormCommand) command).getDirection())) {
            request.getSession().setAttribute("gotopage", "" + (pageNumber + 1));
            request.getSession().setAttribute("skipaddquestion", "true");

            String[] selectedSymptoms = request.getParameterValues("symptomsByParticipants");

            if (selectedSymptoms != null) {
                for (String symptom : selectedSymptoms) {

                    CtcQuery ctcQuery = new CtcQuery();
                    MeddraQuery meddraQuery = new MeddraQuery();
                    ProCtcTermQuery proCtcTermQuery = new ProCtcTermQuery();
                    proCtcTermQuery.filterByTerm(symptom);
                    ProCtcTerm proCtcTerm = proCtcTermRepository.findSingle(proCtcTermQuery);
                    if (proCtcTerm != null) {
                        addProCtcQuestion(proCtcTerm, pageNumber, studyParticipantCrf, studyParticipantCrfSchedule);
                    } else {
                        meddraQuery.filterByMeddraTerm(symptom);
                        LowLevelTerm lowLevelTerm = meddraRepository.findSingle(meddraQuery);
                        String meddraCode = lowLevelTerm.getMeddraCode();
                        ctcQuery.filterByCtepCode(meddraCode);
                        CtcTerm ctcTerm = ctcTermRepository.findSingle(ctcQuery);
                        if (ctcTerm == null) {
                            addMeddraQuestion(lowLevelTerm, pageNumber, studyParticipantCrf, studyParticipantCrfSchedule);
                        } else {
                            Integer ctcTermId = ctcTerm.getId();
                            proCtcTermQuery = new ProCtcTermQuery();
                            proCtcTermQuery.filterByCtcTermId(ctcTermId);
                            proCtcTerm = proCtcTermRepository.findSingle(proCtcTermQuery);
                            if (proCtcTerm == null) {
                                addMeddraQuestion(lowLevelTerm, pageNumber, studyParticipantCrf, studyParticipantCrfSchedule);
                            } else {
                                addProCtcQuestion(proCtcTerm, pageNumber, studyParticipantCrf, studyParticipantCrfSchedule);
                            }
                        }
                    }
                    pageNumber++;
                }
            }
        } else {
            request.getSession().setAttribute("gotopage", "" + pageNumber);
        }
        return new ModelAndView(new RedirectView("submit?id=" + ((SubmitFormCommand) command).getStudyParticipantCrfSchedule().getId()));
    }

    public void addProCtcQuestion(ProCtcTerm proCtcTerm, int pageNumber, StudyParticipantCrf studyParticipantCrf, StudyParticipantCrfSchedule studyParticipantCrfSchedule) {
        List<ProCtcQuestion> questions = proCtcTerm.getProCtcQuestions();
        for (ProCtcQuestion question : questions) {

            StudyParticipantCrfAddedQuestion studyParticipantCrfAddedQuestion = new StudyParticipantCrfAddedQuestion();
            studyParticipantCrfAddedQuestion.setProCtcQuestion(question);
            studyParticipantCrfAddedQuestion.setPageNumber(pageNumber);
            studyParticipantCrf.addStudyParticipantCrfAddedQuestion(studyParticipantCrfAddedQuestion);
            studyParticipantCrfAddedQuestionRepository.save(studyParticipantCrfAddedQuestion);

            StudyParticipantCrfScheduleAddedQuestion studyParticipantCrfScheduleAddedQuestion = new StudyParticipantCrfScheduleAddedQuestion();
            studyParticipantCrfScheduleAddedQuestion.setProCtcQuestion(question);
            studyParticipantCrfScheduleAddedQuestion.setPageNumber(pageNumber);
            studyParticipantCrfScheduleAddedQuestion.setStudyParticipantCrfAddedQuestion(studyParticipantCrfAddedQuestion);
            studyParticipantCrfSchedule.addStudyParticipantCrfScheduleAddedQuestion(studyParticipantCrfScheduleAddedQuestion);
            studyParticipantCrfScheduleAddedQuestionRepository.save(studyParticipantCrfScheduleAddedQuestion);
        }
    }

    public void addMeddraQuestion(LowLevelTerm lowLevelTerm, int pageNumber, StudyParticipantCrf studyParticipantCrf, StudyParticipantCrfSchedule studyParticipantCrfSchedule) {
        List<MeddraQuestion> meddraQuestions = lowLevelTerm.getMeddraQuestions();
        MeddraQuestion meddraQuestion;
        if (meddraQuestions.size() > 0) {
            meddraQuestion = meddraQuestions.get(0);
        } else {
            meddraQuestion = createMeddraQuestion(lowLevelTerm);
        }
        StudyParticipantCrfAddedQuestion studyParticipantCrfAddedQuestion = new StudyParticipantCrfAddedQuestion();
        studyParticipantCrfAddedQuestion.setMeddraQuestion(meddraQuestion);
        studyParticipantCrfAddedQuestion.setPageNumber(pageNumber);
        studyParticipantCrf.addStudyParticipantCrfAddedQuestion(studyParticipantCrfAddedQuestion);
        studyParticipantCrfAddedQuestionRepository.save(studyParticipantCrfAddedQuestion);

        StudyParticipantCrfScheduleAddedQuestion studyParticipantCrfScheduleAddedQuestion = new StudyParticipantCrfScheduleAddedQuestion();
        studyParticipantCrfScheduleAddedQuestion.setMeddraQuestion(meddraQuestion);
        studyParticipantCrfScheduleAddedQuestion.setPageNumber(pageNumber);
        studyParticipantCrfScheduleAddedQuestion.setStudyParticipantCrfAddedQuestion(studyParticipantCrfAddedQuestion);
        studyParticipantCrfSchedule.addStudyParticipantCrfScheduleAddedQuestion(studyParticipantCrfScheduleAddedQuestion);
        studyParticipantCrfScheduleAddedQuestionRepository.save(studyParticipantCrfScheduleAddedQuestion);

    }

    private MeddraQuestion createMeddraQuestion(LowLevelTerm lowLevelTerm) {
        MeddraQuestion meddraQuestion = new MeddraQuestion();
        MeddraValidValue meddraValidValue = new MeddraValidValue();
        meddraQuestion.setLowLevelTerm(lowLevelTerm);
        meddraQuestion.setProCtcQuestionType(ProCtcQuestionType.PRESENT);
        meddraValidValue.setValue("Yes");
        meddraValidValue.setDisplayOrder(0);
        MeddraValidValue meddraValidValue1 = new MeddraValidValue();
        meddraValidValue1.setValue("No");
        meddraValidValue1.setDisplayOrder(1);
        meddraQuestion.addValidValue(meddraValidValue);
        meddraQuestion.addValidValue(meddraValidValue1);
        meddraQuestion.setQuestionText("Did you have any " + lowLevelTerm.getMeddraTerm() + "?");
        meddraQuestion.setDisplayOrder(0);
        genericRepository.save(meddraQuestion);
        return meddraQuestion;
    }


    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        SubmitFormCommand submitFormCommand = (SubmitFormCommand)
                request.getSession().getAttribute(SubmitFormController.class.getName() + ".FORM." + "command");

        ProCtcTermQuery query = new ProCtcTermQuery();
        ArrayList<ProCtcTerm> proCtcTerms = (ArrayList<ProCtcTerm>) proCtcTermRepository.find(query);
        submitFormCommand.computeAdditionalSymptoms(proCtcTerms);

        return submitFormCommand;
    }


    /**
     * Gets the review view.
     *
     * @return the review view
     */
    public String getReviewView() {
        return reviewView;
    }

    /**
     * Sets the review view.
     *
     * @param reviewView the new review view
     */
    public void setReviewView(String reviewView) {
        this.reviewView = reviewView;
    }


    @Required

    public void setStudyParticipantCrfRepository(StudyParticipantCrfRepository studyParticipantCrfRepository) {
        this.studyParticipantCrfRepository = studyParticipantCrfRepository;
    }

    @Required

    public void setStudyParticipantCrfAddedQuestionRepository(StudyParticipantCrfAddedQuestionRepository studyParticipantCrfAddedQuestionRepository) {
        this.studyParticipantCrfAddedQuestionRepository = studyParticipantCrfAddedQuestionRepository;
    }

    @Required

    public void setStudyParticipantCrfScheduleRepository(StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository) {
        this.studyParticipantCrfScheduleRepository = studyParticipantCrfScheduleRepository;
    }

    @Required

    public void setStudyParticipantCrfScheduleAddedQuestionRepository(StudyParticipantCrfScheduleAddedQuestionRepository studyParticipantCrfScheduleAddedQuestionRepository) {
        this.studyParticipantCrfScheduleAddedQuestionRepository = studyParticipantCrfScheduleAddedQuestionRepository;
    }

    public MeddraRepository getMeddraRepository() {
        return meddraRepository;
    }

    public void setMeddraRepository(MeddraRepository meddraRepository) {
        this.meddraRepository = meddraRepository;
    }

    public CtcTermRepository getCtcTermRepository() {
        return ctcTermRepository;
    }

    public void setCtcTermRepository(CtcTermRepository ctcTermRepository) {
        this.ctcTermRepository = ctcTermRepository;
    }

    public ProCtcTermRepository getProCtcTermRepository() {
        return proCtcTermRepository;
    }

    public void setProCtcTermRepository(ProCtcTermRepository proCtcTermRepository) {
        this.proCtcTermRepository = proCtcTermRepository;
    }

    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}