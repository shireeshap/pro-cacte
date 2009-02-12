package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.cabig.ctms.web.tabs.Flow;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

//
/**
 * The Class CreateStudyController.
 *
 * @author Vinay Kumar
 * @crated Nov 3, 2008
 */
public class CreateStudyController extends StudyController<StudyCommand> {


    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.web.study.StudyController#layoutTabs(gov.nih.nci.cabig.ctms.web.tabs.Flow)
     */
    @Override
    protected void layoutTabs(final Flow<StudyCommand> flow) {
        flow.addTab(new StudyDetailsTab());

        flow.addTab(new SitesTab());
//        flow.addTab(new StudyInvestigatorsTab());
        flow.addTab(new EmptyStudyTab("study.tab.overview", "study.tab.overview", "study/study_reviewsummary"));

    }


    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected Object formBackingObject(final HttpServletRequest request) throws ServletException {
        return new StudyCommand();
    }


}
