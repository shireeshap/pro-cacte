package gov.nih.nci.ctcae.core.validation;

import gov.nih.nci.ctcae.core.AbstractTestCase;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfScheduleAddedQuestion;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vinay Kumar
 * @crated Feb 9, 2009
 */
public class BeanValidatorTest extends AbstractTestCase {

    private BeanValidator beanValidator;

    List<Class> domainClasses = new ArrayList<Class>();

    private Study study;

    private CRF crf;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        domainClasses.add(Study.class);
        domainClasses.add(CRF.class);
        domainClasses.add(StudyParticipantCrfScheduleAddedQuestion.class);
        beanValidator = new BeanValidator(domainClasses);
        study = new Study();
        crf = new CRF();

    }


    public void testForColumnAnnotation() {
        try {
            beanValidator.validate(study);
            fail("non nullable property shortTitle of object gov.nih.nci.ctcae.core.domain.Study must not be null");
        } catch (CtcAeSystemException e) {
            logger.debug(e.getMessage());
        }
    }

    public void testForManyToOneAnnotation() {
        crf = new CRF();
        crf.setId(1);
        crf.setStudy(study);
        crf.setTitle("CRF");
        crf.setCrfVersion("1.0");
        //must not throw exception because of other annotations
        beanValidator.validate(crf);

        try {
            crf.setStudy(null);
            beanValidator.validate(crf);
            fail("non nullable property study of object gov.nih.nci.ctcae.core.domain.CRF must not be null");
        } catch (CtcAeSystemException e) {
            logger.debug(e.getMessage());
        }
    }

    public void testForManyToOneAnnotationNullableColumn() {
        StudyParticipantCrfScheduleAddedQuestion studyParticipantCrfScheduleAddedQuestion = new StudyParticipantCrfScheduleAddedQuestion();
        studyParticipantCrfScheduleAddedQuestion.setId(1);

        try {
            beanValidator.validate(studyParticipantCrfScheduleAddedQuestion);
            fail("non nullable property StudyParticipantCrfSchedule of object gov.nih.nci.ctcae.core.domain.studyParticipantCrfScheduleAddedQuestion must not be null");
        } catch (CtcAeSystemException e) {
            logger.debug(e.getMessage());
        }
        studyParticipantCrfScheduleAddedQuestion.setStudyParticipantCrfSchedule(new StudyParticipantCrfSchedule());
        try{
        beanValidator.validate(studyParticipantCrfScheduleAddedQuestion);
        } catch (CtcAeSystemException e) {
               fail("object gov.nih.nci.ctcae.core.domain.studyParticipantCrfScheduleAddedQuestion should not give this error");
        }

    }
}
