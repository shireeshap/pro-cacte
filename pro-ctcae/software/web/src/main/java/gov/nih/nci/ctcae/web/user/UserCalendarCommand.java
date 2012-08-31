package gov.nih.nci.ctcae.web.user;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.StudyParticipantCrfScheduleQuery;
import gov.nih.nci.ctcae.core.repository.secured.StudyParticipantCrfScheduleRepository;
import org.springframework.security.context.SecurityContextHolder;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author allareddy
 *         Date: 6/1/12
 */
public class UserCalendarCommand {

    private ProCtcAECalendar proCtcAECalendar = new ProCtcAECalendar();
    private TreeMap<Integer, List<StudyParticipantCrfSchedule>> scheduleDates;
    private StudyParticipantCrfScheduleRepository spcsRepository;
    private User user;
    private ClinicalStaff clinicalStaff;
    private List<Integer> organizationIds = new ArrayList<Integer>();
    private boolean isLcra = false;
    private boolean isScra = false;

    public void setSpcsRepository(StudyParticipantCrfScheduleRepository spcsRepository) {
        this.spcsRepository = spcsRepository;
    }

    public ProCtcAECalendar getProCtcAECalendar() {
        return proCtcAECalendar;
    }

    public void setProCtcAECalendar(ProCtcAECalendar proCtcAECalendar) {
        this.proCtcAECalendar = proCtcAECalendar;
    }

    public TreeMap<Integer, List<StudyParticipantCrfSchedule>> getScheduleDates() {
        return scheduleDates;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ClinicalStaff getClinicalStaff() {
        return clinicalStaff;
    }

    public void setClinicalStaff(ClinicalStaff clinicalStaff) {
        this.clinicalStaff = clinicalStaff;
    }

    public List<Integer> getOrganizationIds() {
        return organizationIds;
    }

    public void setOrganizationIds(List<Integer> organizationIds) {
        this.organizationIds = organizationIds;
    }

    public boolean isLcra() {
        return isLcra;
    }

    public void setLcra(boolean lcra) {
        isLcra = lcra;
    }

    public boolean isScra() {
        return isScra;
    }

    public void setScra(boolean scra) {
        isScra = scra;
    }

    public void createCurrentMonthScheduleMap() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        StudyParticipantCrfScheduleQuery spcsQuery = new StudyParticipantCrfScheduleQuery();
        List<CrfStatus> statuses = new ArrayList<CrfStatus>();
        statuses.add(CrfStatus.INPROGRESS);
        statuses.add(CrfStatus.SCHEDULED);
        statuses.add(CrfStatus.PASTDUE);
        statuses.add(CrfStatus.COMPLETED);
        statuses.add(CrfStatus.ONHOLD);
        if (isScra()) {
            spcsQuery.filterBySiteIds(organizationIds);
        } else {
            spcsQuery.filterByUsername(userName);
            spcsQuery.filterByHidden(false);
        }
//        Calendar c = new GregorianCalendar(proCtcAECalendar.getCalendar().getTime());
        Calendar c = (Calendar) proCtcAECalendar.getCalendar().clone();
        c.set(Calendar.DAY_OF_MONTH, 1);
        Date startDate = c.getTime();
        int daysInMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        c.set(Calendar.DAY_OF_MONTH, daysInMonth);
        Date endDate = c.getTime();

        spcsQuery.filterByStartEndDate(startDate, endDate);
        spcsQuery.filterByStatuses(statuses);
        List<StudyParticipantCrfSchedule> spcs = spcsRepository.find(spcsQuery);

        scheduleDates = new TreeMap();
        SimpleDateFormat sdf = new SimpleDateFormat("dd");

        for (StudyParticipantCrfSchedule scs : spcs) {
            List<StudyParticipantCrfSchedule> schedulesOnDate;
            int day = Integer.parseInt(sdf.format(scs.getStartDate()));

            if (scheduleDates.containsKey(day)) {
                schedulesOnDate = scheduleDates.get(day);
            } else {
                schedulesOnDate = new ArrayList<StudyParticipantCrfSchedule>();
                scheduleDates.put(day, schedulesOnDate);
            }
            schedulesOnDate.add(scs);
        }

    }
}

