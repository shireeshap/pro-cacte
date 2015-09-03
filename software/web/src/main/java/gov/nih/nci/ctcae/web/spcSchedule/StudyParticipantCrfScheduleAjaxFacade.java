package gov.nih.nci.ctcae.web.spcSchedule;

import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.RoleStatus;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.query.StudyParticipantCrfScheduleQuery;
import gov.nih.nci.ctcae.core.repository.secured.StudyParticipantCrfScheduleRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.security.context.SecurityContextHolder;

/**
 * @author mehul
 *         Date: 1/24/12
 */
public class StudyParticipantCrfScheduleAjaxFacade {
    private StudyParticipantCrfScheduleRepository spcsRepository;

    public List<StudyParticipantCrfSchedule> searchSchedules(Integer startIndex, Integer results, String sortField, String direction, CrfStatus status, Date current, Long totalRecordsWithoutSecurity, Long filteredRecordsWithSecurity) {
        StudyParticipantCrfScheduleQuery spcsQuery = new StudyParticipantCrfScheduleQuery();
        List<CrfStatus> statuses = new ArrayList<CrfStatus>();
        spcsQuery.setFirstResult(startIndex);
        spcsQuery.setMaximumResults(results);
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        spcsQuery.filterByUsername(userName);
        spcsQuery.filterByParticipantStatusNot(RoleStatus.OFFSTUDY);
        if (status.equals(CrfStatus.PASTDUE)) {
            spcsQuery.filterByMarkDelete();
            spcsQuery.filterByStatus(status);
        } else if (status.equals(CrfStatus.INPROGRESS)){
        	statuses.add(CrfStatus.INPROGRESS);
        	statuses.add(CrfStatus.SCHEDULED);
        	spcsQuery.filterByStatuses(statuses);
            spcsQuery.filterByDate(current);
        } else {
            spcsQuery.filterByStatus(CrfStatus.SCHEDULED);
            spcsQuery.filterByGreaterDate(current);
            //Prevent hidden forms to be populated in Upcoming Forms section for a clinical staff login
            spcsQuery.filterByHiddenForms();
        }

        List<StudyParticipantCrfSchedule> spcrfsList = new ArrayList<StudyParticipantCrfSchedule>();
        spcrfsList = fetchRecords(spcrfsList, spcsQuery);
        
        while(!isSelectedPageSizeFilled(spcrfsList, results, startIndex, totalRecordsWithoutSecurity, filteredRecordsWithSecurity)){
        	startIndex = startIndex + results;
        	spcsQuery.setFirstResult(startIndex);
        	spcrfsList = fetchRecords(spcrfsList, spcsQuery);
        }
        
        return spcrfsList;
    }
    
    public List<StudyParticipantCrfSchedule> fetchRecords(List<StudyParticipantCrfSchedule> spcrfsList, StudyParticipantCrfScheduleQuery query){
    	List<StudyParticipantCrfSchedule> l = spcsRepository.find(query);
    	spcrfsList.addAll(l);
    	return spcrfsList;
    }
    
    public boolean isSelectedPageSizeFilled(List<StudyParticipantCrfSchedule> spcrfsList, Integer results, Integer startIndex, Long totalRecordsWithoutSecurity, Long filteredRecordsWithSecurity){
    	if(spcrfsList.size() < results && spcrfsList.size() < filteredRecordsWithSecurity 
        		&& startIndex <= totalRecordsWithoutSecurity){
    		return false;
    	}
    	return true;
    }
    
    public Long resultCount(CrfStatus status, Date current, boolean secured) {
    	String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        StudyParticipantCrfScheduleQuery spcsQuery = new StudyParticipantCrfScheduleQuery(secured);
        List<CrfStatus> statuses = new ArrayList<CrfStatus>();
        spcsQuery.filterByUsername(userName);
        spcsQuery.filterByParticipantStatusNot(RoleStatus.OFFSTUDY);
       
        if (status.equals(CrfStatus.PASTDUE)) {
        	spcsQuery.filterByMarkDelete();
            spcsQuery.filterByStatus(status);
        } else if (status.equals(CrfStatus.INPROGRESS)){
        	statuses.add(CrfStatus.INPROGRESS);
        	statuses.add(CrfStatus.SCHEDULED);
        	spcsQuery.filterByStatuses(statuses);
            spcsQuery.filterByDate(current);
        } else {
            spcsQuery.filterByStatus(CrfStatus.SCHEDULED);
            spcsQuery.filterByGreaterDate(current);
            //Prevent hidden forms to be populated in Upcoming Forms section for a clinical staff login
            spcsQuery.filterByHiddenForms();
        }
        
        return spcsRepository.findWithCount(spcsQuery);
    }

    public void setSpcsRepository(StudyParticipantCrfScheduleRepository spcsRepository) {
        this.spcsRepository = spcsRepository;
    }
}
