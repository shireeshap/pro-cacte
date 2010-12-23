package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyOrganization;
import gov.nih.nci.ctcae.core.repository.secured.StudyRepository;
import gov.nih.nci.ctcae.web.form.CrfAjaxFacade;
import gov.nih.nci.ctcae.web.organization.OrganizationAjaxFacade;
import gov.nih.nci.ctcae.web.study.StudyAjaxFacade;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;


/**
 * @author Harsh Agarwal
 *         Date: July 02, 2009
 */
public class ReportSearchCriteriaController extends AbstractController {

    StudyAjaxFacade studyAjaxFacade;
    CrfAjaxFacade crfAjaxFacade;
    OrganizationAjaxFacade organizationAjaxFacade;
    StudyRepository studyRepository;

    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        cleanSession(request);
        String reportType = request.getParameter("rt");
        if (StringUtils.isBlank(reportType)) {
            ModelAndView rv = new ModelAndView(new RedirectView("report?rt=overallStudy"));
            return rv;
        }
        ModelAndView modelAndView = new ModelAndView("reports/searchCriteria");
        Study study = null;
        if (!StringUtils.isBlank(request.getParameter("studyId"))) {
            study = studyRepository.findById(Integer.parseInt(request.getParameter("studyId")));
        } else {
            List<Study> studies = studyAjaxFacade.matchStudy("%");
            if (studies.size() == 1) {
                study = studies.get(0);
            }
        }
        if (study != null) {

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

    private void cleanSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute("study");
        session.removeAttribute("crfs");
        session.removeAttribute("proctcterms");
        session.removeAttribute("studySite");
        session.removeAttribute("url");
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

    @Required
    public void setStudyRepository(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }

    private String getUrlForReportType(HttpServletRequest request) {
        String reportType = request.getParameter("rt");
        return request.getContextPath() + "/pages/reports/" + reportType;
    }
}