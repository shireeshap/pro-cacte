package gov.nih.nci.ctcae.web.reports;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.beans.factory.annotation.Required;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gov.nih.nci.ctcae.web.study.StudyAjaxFacade;
import gov.nih.nci.ctcae.web.form.CrfAjaxFacade;
import gov.nih.nci.ctcae.web.organization.OrganizationAjaxFacade;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.domain.StudyOrganization;

import java.util.List;
import java.util.Enumeration;
import java.util.HashMap;


/**
 * @author Harsh Agarwal
 *         Date: July 02, 2009
 */
public class ReportSearchCriteriaController extends AbstractController {

    StudyAjaxFacade studyAjaxFacade;
    CrfAjaxFacade crfAjaxFacade;
    OrganizationAjaxFacade organizationAjaxFacade;

    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("reports/searchCriteria");
        List<Study> studies = studyAjaxFacade.matchStudy("%");
        if (studies.size() == 1) {
            Study study = studies.get(0);
            modelAndView.addObject("study", study);

            List<CRF> crfs = getCrfsForStudy(study);
            modelAndView.addObject("crfs", crfs);
            if (crfs.size() == 1) {
                CRF crf = crfs.get(0);
                List<ProCtcTerm> proCtcTerms = getSymptomsForCrf(crf);
                modelAndView.addObject("proctcterms", proCtcTerms);
            }

            List<StudyOrganization> studySites = getStudySitesForStudy(study);
            if (studySites.size() == 1) {
                modelAndView.addObject("studySite", studySites.get(0));
            }
        }
        modelAndView.addObject("url", getUrlForReportType(request));
        return modelAndView;
    }

    private List<StudyOrganization> getStudySitesForStudy(Study study) {
        return organizationAjaxFacade.matchOrganizationByStudyId("%", study.getId());
    }

    private List<ProCtcTerm> getSymptomsForCrf(CRF crf) {
        return crfAjaxFacade.getSymptomsForCrf(crf.getId());
    }

    private List<CRF> getCrfsForStudy(Study study) throws Exception {
        return crfAjaxFacade.getReducedCrfs(study.getId());
    }

    public ReportSearchCriteriaController() {
        super();
        setSupportedMethods(new String[]{"GET"});
    }

    @Required
    public void setStudyAjaxFacade(StudyAjaxFacade studyAjaxFacade) {
        this.studyAjaxFacade = studyAjaxFacade;
    }

    @Required
    public void setCrfAjaxFacade(CrfAjaxFacade crfAjaxFacade) {
        this.crfAjaxFacade = crfAjaxFacade;
    }

    @Required
    public void setOrganizationAjaxFacade(OrganizationAjaxFacade organizationAjaxFacade) {
        this.organizationAjaxFacade = organizationAjaxFacade;
    }

    private String getUrlForReportType(HttpServletRequest request) {
        String reportType = request.getParameter("rt");
        return request.getContextPath() + "/pages/reports/" + reportType;
    }
}