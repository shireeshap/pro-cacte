package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.domain.StudyOrganization;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.core.domain.StudySite;
import gov.nih.nci.ctcae.core.repository.secured.StudyOrganizationRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyRepository;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: Harsh
 * Date: Jun 2, 2009
 * Time: 8:01:32 AM
 */
public class EnrollmentReportResultsController extends AbstractController {
    StudyRepository studyRepository;
    StudyOrganizationRepository studyOrganizationRepository;

    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("reports/enrollmentReport");

        String studyId = request.getParameter("study");
        List<StudyOrganization> studySites = studyOrganizationRepository.findByStudyId("%", Integer.parseInt(studyId));
        List<EnrollmentReportLine> enrollmentReport = new ArrayList<EnrollmentReportLine>();
        for (StudyOrganization studyOrganization : studySites) {
            EnrollmentReportLine enrollmentReportLine = new EnrollmentReportLine();
            enrollmentReportLine.setStudySite((StudySite) studyOrganization);
            List<StudyParticipantAssignment> participantAssignmentList = studyOrganization.getStudyParticipantAssignments();
            Collections.sort(participantAssignmentList, new StudyParticipantAssignmentComparator());
            enrollmentReportLine.setNumberOfParticipants(participantAssignmentList.size());
            if (participantAssignmentList.size() > 0) {
                enrollmentReportLine.setLastEnrollment(participantAssignmentList.get(participantAssignmentList.size()-1).getParticipant().getCreationDate());
            }
            enrollmentReport.add(enrollmentReportLine);
        }
        modelAndView.addObject("results", enrollmentReport);
        return modelAndView;
    }

    @Required
    public void setStudyRepository(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }

    @Required
    public void setStudyOrganizationRepository(StudyOrganizationRepository studyOrganizationRepository) {
        this.studyOrganizationRepository = studyOrganizationRepository;
    }
}

