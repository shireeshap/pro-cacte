package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.query.ParticipantAddedQuestionsReportQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyOrganizationRepository;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyOrganization;
import gov.nih.nci.ctcae.core.domain.StudySite;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.List;
import java.util.Date;
import java.util.ArrayList;
import java.util.Collections;

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
        Study study = studyRepository.findById(Integer.parseInt(studyId));
        List<EnrollmentReportLine> enrollmentReport = new ArrayList<EnrollmentReportLine>();
        for (StudySite studySite : study.getStudySites()) {
            StudyOrganization studyOrganization = studyOrganizationRepository.findById(studySite.getId());
            EnrollmentReportLine enrollmentReportLine = new EnrollmentReportLine();
            enrollmentReportLine.setStudySite((StudySite)studyOrganization);
            List<StudyParticipantAssignment> participantAssignmentList = studySite.getStudyParticipantAssignments();
            Collections.sort(participantAssignmentList, new StudyParticipantAssignmentComparator());
            enrollmentReportLine.setNumberOfParticipants(participantAssignmentList.size());
            enrollmentReportLine.setLastEnrollment(participantAssignmentList.get(0).getParticipant().getCreationDate());
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

