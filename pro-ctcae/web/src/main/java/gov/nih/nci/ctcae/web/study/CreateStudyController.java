package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.cabig.ctms.web.tabs.Flow;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Vinay Kumar
 * @crated Nov 3, 2008
 */
public class CreateStudyController extends StudyController<StudyCommand> {


    @Override
    protected void layoutTabs(final Flow<StudyCommand> flow) {
        flow.addTab(new StudyDetailsTab());
        flow.addTab(new SitesTab());
        flow.addTab(new EmptyStudyTab("Overview", "Overview", "study/study_reviewsummary"));

    }


    @Override
    protected Object formBackingObject(final HttpServletRequest request) throws ServletException {
        return new StudyCommand();
    }


}
