package gov.nih.nci.ctcae.web.spcSchedule;

import gov.nih.nci.ctcae.core.domain.CrfStatus;
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

    public List<StudyParticipantCrfSchedule> searchSchedules(Integer startIndex, Integer results, String sortField, String direction, CrfStatus status, Date current) {
        StudyParticipantCrfScheduleQuery spcsQuery = new StudyParticipantCrfScheduleQuery();
        List<CrfStatus> statuses = new ArrayList<CrfStatus>();
        spcsQuery.setFirstResult(startIndex);
        spcsQuery.setMaximumResults(results);
//        spcsQuery.setSortBy("spcs." + sortField);
//        spcsQuery.setSortDirection(direction);
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        spcsQuery.filterByUsername(userName);
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
        }

        return spcsRepository.find(spcsQuery);
    }

    public Long resultCount(CrfStatus status, Date current) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        StudyParticipantCrfScheduleQuery spcsQuery = new StudyParticipantCrfScheduleQuery(true);
        spcsQuery.filterByUsername(userName);
        if (status.equals(CrfStatus.PASTDUE)) {
        	spcsQuery.filterByMarkDelete();
            spcsQuery.filterByStatus(status);
        } else if (status.equals(CrfStatus.INPROGRESS)){
            spcsQuery.filterByStatus(status);
        } else {
            spcsQuery.filterByStatus(CrfStatus.SCHEDULED);
            spcsQuery.filterByGreaterDate(current);
        }
        return spcsRepository.findWithCount(spcsQuery);
    }

    public void setSpcsRepository(StudyParticipantCrfScheduleRepository spcsRepository) {
        this.spcsRepository = spcsRepository;
    }
}
