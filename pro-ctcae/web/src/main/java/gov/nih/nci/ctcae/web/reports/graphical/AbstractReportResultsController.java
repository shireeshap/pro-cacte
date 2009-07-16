package gov.nih.nci.ctcae.web.reports.graphical;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.query.reports.AbstractReportQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.*;


/**
 * User: Harsh
 * Date: May 19, 2009
 * Time: 9:34:01 AM
 */
public abstract class AbstractReportResultsController extends AbstractController {

    protected GenericRepository genericRepository;

    protected void parseRequestParametersAndFormQuery(HttpServletRequest request, AbstractReportQuery query) throws ParseException {
        int crfId = Integer.parseInt(request.getParameter("crf"));
        String symptom = request.getParameter("symptom");
        int symptomId = -1;
        if (!StringUtils.isBlank(symptom)) {
            symptomId = Integer.parseInt(symptom);
        }
        String attributes = request.getParameter("attributes");
        String studySiteId = request.getParameter("studySite");
        String visitRange = request.getParameter("visitRange");
        String attribute = request.getParameter("att");
        String period = request.getParameter("period");
        String filter = request.getParameter("filter");

        if (!StringUtils.isBlank(attribute)) {
            attributes = attribute;
        }
        query.filterByCrf(crfId);
        if (symptomId != -1) {
            query.filterBySymptomId(symptomId);
        }
        if (!StringUtils.isBlank(attributes)) {
            HashSet<ProCtcQuestionType> qT = new HashSet<ProCtcQuestionType>();
            StringTokenizer st = new StringTokenizer(attributes, ",");
            while (st.hasMoreTokens()) {
                String a = st.nextToken();
                if (!StringUtils.isBlank(a)) {
                    qT.add(ProCtcQuestionType.getByDisplayName(a));
                }
            }
            query.filterByAttributes(qT);
        }
        if (!StringUtils.isBlank(studySiteId)) {
            query.filterByStudySite(Integer.parseInt(studySiteId));
        }

        if (!StringUtils.isBlank(visitRange) && "dateRange".equals(visitRange)) {
            Date startDate = DateUtils.parseDate(request.getParameter("startDate"));
            Date endDate = DateUtils.parseDate(request.getParameter("endDate"));
            query.filterByScheduleStartDate(startDate, endDate);
        }

        if (!StringUtils.isBlank(period)) {
            String periodType = period.substring(0, period.indexOf(' '));
            Integer colInt = Integer.parseInt(period.substring(period.indexOf(' ') + 1));
            query.filterByPeriod(periodType, colInt);
        }
        if (!StringUtils.isBlank(filter)) {
            try {
                String filterValue = request.getParameter("filterVal");
                filterValue = StringUtils.deleteWhitespace(filterValue);
                ArrayList<Integer> l = new ArrayList<Integer>();
                if (filterValue.indexOf('-') > 0) {
                    Integer start = Integer.parseInt(filterValue.substring(0, filterValue.indexOf('-')));
                    Integer end = Integer.parseInt(filterValue.substring(filterValue.indexOf('-') + 1));
                    for (int i = start; i <= end; i++) {
                        l.add(i);
                    }
                } else {
                    if (filterValue.indexOf(',') > 0) {
                        StringTokenizer st = new StringTokenizer(filterValue, ",");
                        while (st.hasMoreTokens()) {
                            Integer a = Integer.valueOf(st.nextToken());
                            l.add(a);
                        }
                    } else {
                        if (StringUtils.isNumeric(filterValue)) {
                            Integer a = Integer.valueOf(filterValue);
                            l.add(a);
                        }
                    }
                }
                if (l.size() > 0) {
                    query.filterByPeriod(filter, l);
                }
            } catch (Exception
                    e) {
                logger.error(e);
            }
        }


    }

    @Required
    public void setGenericRepository
            (GenericRepository
                    genericRepository) {
        this.genericRepository = genericRepository;
    }

    protected String getTitle(HttpServletRequest request) {
        StringBuffer title = new StringBuffer();
        int symptomId = Integer.parseInt(request.getParameter("symptom"));
        ProCtcTerm proCtcTerm = genericRepository.findById(ProCtcTerm.class, symptomId);

        String attribute = request.getParameter("att");
        title.append("Worst responses for ").append(proCtcTerm.getTerm()).append(" ").append(attribute);
        return title.toString();
    }

}