package gov.nih.nci.ctcae.web.spcSchedule;

import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.query.StudyParticipantCrfScheduleQuery;
import gov.nih.nci.ctcae.core.repository.secured.StudyParticipantCrfScheduleRepository;
import org.springframework.security.context.SecurityContextHolder;

import java.util.List;

/**
 * @author mehul
 *         Date: 1/24/12
 */
public class StudyParticipantCrfScheduleAjaxFacade {
    private StudyParticipantCrfScheduleRepository spcsRepository;

    public List<StudyParticipantCrfSchedule> searchSchedules(Integer startIndex, Integer results, String sortField, String direction, CrfStatus status) {
        StudyParticipantCrfScheduleQuery spcsQuery = new StudyParticipantCrfScheduleQuery();
        spcsQuery.setFirstResult(startIndex);
        spcsQuery.setMaximumResults(results);
        spcsQuery.setSortBy("spcs." + sortField);
        spcsQuery.setSortDirection(direction);
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (status.equals(CrfStatus.PASTDUE)) {
            spcsQuery.filterByUsernameOnly(userName);
        } else {
            spcsQuery.filterByUsername(userName);
        }
        spcsQuery.filterByStatus(status);
        List<StudyParticipantCrfSchedule> schedules = (List<StudyParticipantCrfSchedule>) spcsRepository.find(spcsQuery);
        return schedules;
    }

    public Long resultCount(CrfStatus status) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        StudyParticipantCrfScheduleQuery spcsQuery = new StudyParticipantCrfScheduleQuery(true);
        spcsQuery.filterByUsername(userName);
        spcsQuery.filterByStatus(status);
        return spcsRepository.findWithCount(spcsQuery);
    }

    public void setSpcsRepository(StudyParticipantCrfScheduleRepository spcsRepository) {
        this.spcsRepository = spcsRepository;
    }
}