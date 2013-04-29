package gov.nih.nci.ctcae.web.reports.graphical;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.CRFQuery;
import gov.nih.nci.ctcae.core.query.reports.AbstractReportQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.*;

public class ReportResultsHelper {

    private static GenericRepository genericRepository;

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        ReportResultsHelper.genericRepository = genericRepository;
    }

    public static HashSet<Integer> getSelectedArms(HttpServletRequest request) {
        String arms = request.getParameter("arm");
        if (StringUtils.isBlank(arms) || "-1".equals(arms)) {
            arms = request.getParameter("arms");
        }
        HashSet<Integer> selectedArms = new HashSet<Integer>();
        if (!StringUtils.isBlank(arms)) {
            String[] armArr = arms.split("_");
            for (String arm : armArr) {
                if (!StringUtils.isBlank(arm)) {
                    selectedArms.add(Integer.parseInt(arm));
                }
            }
        }
        if (selectedArms.size() == 0) {
            selectedArms.add(-1);
        }
        return selectedArms;
    }

    public static void parseRequestParametersAndFormQuery(HttpServletRequest request, AbstractReportQuery query) throws ParseException {
        String studySiteId = request.getParameter("studySite");
        String crfIdParam = request.getParameter("crf");
        String studyParam = request.getParameter("study");
        if(!StringUtils.isBlank(studyParam) && StringUtils.isBlank(crfIdParam)){
        	Integer studyId = Integer.parseInt(studyParam);
        	List<CRF> crfList = getReducedCrfs(studyId);
        	List<Integer> crfIds = new ArrayList<Integer>(); 
        	for(CRF crf : crfList){
        		crfIds.add(crf.getId());
                if (crf.getParentCrf() != null) {
                    crfIds.add(crf.getParentCrf().getId());
                }
        	}
        	query.filterByCRFIds(crfIds);
        }
        
		if(!StringUtils.isBlank(crfIdParam)){
        	Integer crfId = Integer.parseInt(crfIdParam);
            CRF crf = genericRepository.findById(CRF.class, crfId);
            List<Integer> crfIds = new ArrayList();
            crfIds.add(crf.getId());
            if (crf.getParentCrf() != null) {
                crfIds.add(crf.getParentCrf().getId());
            }
            query.filterByCRFIds(crfIds);
        }
        query.filterByAttributes(getSelectedAttributes(request));
        if (!StringUtils.isBlank(studySiteId)) {
            query.filterByStudySite(Integer.parseInt(studySiteId));
        }
        applyPeriodFilterToQuery(request, query);
        applySymptomFilterToQuery(request, query);
        applyFilterToQuery(request, query);
        applyArmFilterToQuery(request, query);
        applyDateFilterToQuery(request, query);

    }
    
    public static List<CRF> getReducedCrfs(Integer studyId){
    	CRFQuery crfQuery = new CRFQuery();
    	crfQuery.filterByStudyId(studyId);
    	crfQuery.filterByNullNextVersionId();
    	
    	List<CRF> crfList = genericRepository.find(crfQuery);
    	List<CRF> releasedCrfs = new ArrayList<CRF>();
    	for(CRF crf :crfList){
    		if(crf.getStatus().equals(CrfStatus.RELEASED)){
    			releasedCrfs.add(crf);
    		}
    	}
    	
    	return releasedCrfs;
    }

    private static void applyDateFilterToQuery(HttpServletRequest request, AbstractReportQuery query) throws ParseException {
        String visitRange = request.getParameter("visitRange");
        if (!StringUtils.isBlank(visitRange) && "dateRange".equals(visitRange)) {
            Date startDate = DateUtils.parseDate(request.getParameter("startDate"));
            Date endDate = DateUtils.parseDate(request.getParameter("endDate"));
            query.filterByScheduleStartDate(startDate, endDate);
        }
    }

    private static void applyPeriodFilterToQuery(HttpServletRequest request, AbstractReportQuery query) {
        String period = request.getParameter("period");
        if (!StringUtils.isBlank(period)) {
            String periodType = period.substring(0, period.indexOf(' '));
            String periodValue = period.substring(period.indexOf(' ') + 1);
            if (periodValue.indexOf("[") > -1) {
                periodValue = periodValue.substring(0, periodValue.indexOf("[")).trim();
            }
            Integer colInt = Integer.parseInt(periodValue);
            query.filterByPeriod(periodType, colInt);
        }
    }

    private static void applySymptomFilterToQuery(HttpServletRequest request, AbstractReportQuery query) {
        String symptom = request.getParameter("symptom");
        if (!StringUtils.isBlank(symptom)) {
            int symptomId = Integer.parseInt(symptom);
            query.filterBySymptomId(symptomId);
        }
    }

    private static void applyArmFilterToQuery(HttpServletRequest request, AbstractReportQuery query) {
        HashSet<Integer> armsSet = getSelectedArms(request);
        if (armsSet.size() == 1) {
            Integer armId = (Integer) armsSet.toArray()[0];
            Arm arm = genericRepository.findById(Arm.class, armId);
            query.filterByArm(arm);
        }
    }

    private static HashSet<ProCtcQuestionType> getSelectedAttributes(HttpServletRequest request) {
        String attributes = request.getParameter("attributes");
        String attribute = request.getParameter("att");
        if (!StringUtils.isBlank(attribute)) {
            attributes = attribute;
        }
        HashSet<ProCtcQuestionType> qT = new HashSet<ProCtcQuestionType>();
        if (!StringUtils.isBlank(attributes)) {
            StringTokenizer st = new StringTokenizer(attributes, "_");
            while (st.hasMoreTokens()) {
                String a = st.nextToken();
                if (!StringUtils.isBlank(a)) {
                    qT.add(ProCtcQuestionType.getByDisplayName(a));
                }
            }
        }
        if (qT.size() == 0) {
            qT = getAllAttributes(request);
        }
        return qT;
    }

    public static HashSet<String> getSelectedAttributesNames(HttpServletRequest request) {
        HashSet<ProCtcQuestionType> hashSet = getSelectedAttributes(request);
        HashSet<String> attributeNames = new HashSet<String>();
        for (ProCtcQuestionType proCtcQuestionType : hashSet) {
            attributeNames.add(proCtcQuestionType.getDisplayName());
        }
        return attributeNames;
    }

    public static void applyFilterToQuery(HttpServletRequest request, AbstractReportQuery query) {
        try {
            String filter = request.getParameter("filter");
            if (!StringUtils.isBlank(filter)) {
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static HashSet<ProCtcQuestionType> getAllAttributes(HttpServletRequest request) {
        HashSet<ProCtcQuestionType> allAttributes = new HashSet<ProCtcQuestionType>();
        String symptom = request.getParameter("symptom");
        if (!StringUtils.isBlank(symptom)) {
            ProCtcTerm proCtcTerm = genericRepository.findById(ProCtcTerm.class, Integer.parseInt(symptom));
            for (ProCtcQuestion question : proCtcTerm.getProCtcQuestions()) {
                allAttributes.add(question.getProCtcQuestionType());
            }
        }

        return allAttributes;
    }

    public static ProCtcValidValue getValidValueResponseCode(ProCtcQuestion proQuestion, StudyParticipantCrfItem firstQuestion) {
        ProCtcValidValue myProCtcValidValue = new ProCtcValidValue();
        if (proQuestion != null && proQuestion.getDisplayOrder() == 1) {
//                ProCtcValidValue myProCtcValidValue = new ProCtcValidValue();
            myProCtcValidValue.setValue(ResponseCode.MANUALSKIP.getDisplayName());
            myProCtcValidValue.setResponseCode(-55);
        } else {
            if (firstQuestion != null && firstQuestion.getProCtcValidValue() != null && firstQuestion.getProCtcValidValue().getDisplayOrder() == 0) {
                myProCtcValidValue.setValue(ResponseCode.FORCEDSKIP.getDisplayName());
                myProCtcValidValue.setResponseCode(-99);
//                    myProCtcValidValue.setDisplayOrder(-99);
            } else {
                myProCtcValidValue.setValue(ResponseCode.MANUALSKIP.getDisplayName());
                myProCtcValidValue.setResponseCode(-55);
//                    myProCtcValidValue.setDisplayOrder(-55);
            }
        }
        return myProCtcValidValue;
    }

}