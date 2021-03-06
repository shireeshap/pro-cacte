package gov.nih.nci.ctcae.core.query;

import gov.nih.nci.ctcae.core.domain.AppMode;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.IvrsCallStatus;

import java.util.Date;
import java.util.Set;

/**
 * @author Vinay Gangoli
 */

public class IvrsScheduleQuery extends AbstractQuery {

    private static String queryString = "SELECT ischd from IvrsSchedule ischd order by ischd.preferredCallTime";
    
    public static final String STATUSES = "Statuses";
    public static final String SP_CRF_STATUSES = "SP_CRF_Statuses";

    public static final String APP_MODE = "appMode";
    public static final String TRUE_FLAG = "True_Flag";
    

    public IvrsScheduleQuery() {
        super(queryString);
    }
    
    public void filterByStudyParticipantAssignmentMode(AppMode appMode) {
    	leftJoin("ischd.studyParticipantAssignment.studyParticipantModes as spm ");
        andWhere("spm.mode =:" + APP_MODE);
        andWhere("spm.mode.call =:" + TRUE_FLAG);
        setParameter(APP_MODE, appMode);
        setParameter(TRUE_FLAG, Boolean.TRUE);
    }

    public void filterByStudyParticipantAssignment(Integer id) {
        andWhere("ischd.studyParticipantAssignment.id =:spaId");
        setParameter("spaId", id);
    }
    
    public void filterByStudyParticipantCrfScheduleStatus(Set<CrfStatus> spCrfStatuses) {
        andWhere("ischd.studyParticipantCrfSchedule.status in (:" + SP_CRF_STATUSES + ")");
        setParameterList(SP_CRF_STATUSES, spCrfStatuses);
    }

    public void filterByStudyParticipantCrfSchedule(Integer id) {
        andWhere("ischd.studyParticipantCrfSchedule.id =:crfId");
        setParameter("crfId", id);
    }

    public void filterByDate(Date startDate, Date endDate) {
        andWhere("ischd.nextCallTime between :startDate and :endDate");
        setParameter("startDate", startDate);
        setParameter("endDate", endDate);
    }
    
    public void filterByStatuses(Set<IvrsCallStatus> statuses) {
        andWhere("ischd.callStatus in (:" + STATUSES + ")");
        setParameterList(STATUSES, statuses);
    }


    public void filterByStatus(IvrsCallStatus status) {
        andWhere("ischd.callStatus =:status");
        setParameter("status", status);
    }
    
    public void filterByCallCountGreaterThan(int callCount) {
        andWhere("ischd.callCount >:callCount");
        setParameter("callCount", callCount);
    }
}