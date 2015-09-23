package gov.nih.nci.ctcae.core.query;

import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.RoleStatus;
import gov.nih.nci.ctcae.core.domain.StudyOrganization;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.security.ApplicationSecurityManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//

/**
 * @author mehul
 */

public class StudyParticipantCrfScheduleQuery extends AbstractQuery {

    private static String queryString = "SELECT distinct spcs from StudyParticipantCrfSchedule spcs order by spcs.startDate";
    private static String queryString1 = "SELECT count(distinct spcs) from StudyParticipantCrfSchedule spcs";
    private static String CRF_IDS = "ids";
    private static String SITE_IDS = "ids";
    private static String SP_CRF_IDS = "sp_crf_ids";
    private static String USERNAME = "username";
    private static String MARK_DELETE = "markDelete";
    private static String HIDDEN = "hidden";
    private static String STATUSES = "statuses";
    private static String OBJECT_IDS = "objectIds";


    public StudyParticipantCrfScheduleQuery() {
        super(queryString);
    }

    public StudyParticipantCrfScheduleQuery(boolean secured) {
        super(queryString1);
        if(secured){
        	User currentLoggedInUser = ApplicationSecurityManager.getCurrentLoggedInUser();
        	List<Integer> objectIds = new ArrayList<Integer>();
            objectIds = currentLoggedInUser.findAccessibleObjectIds(StudyOrganization.class);
            filterByObjectIds(objectIds);
        }
    }
    
    public void filterByStudy(Integer id) {
        andWhere("spcs.studyParticipantCrf.crf.study.id =:studyId");
        setParameter("studyId", id);
    }

    public void filterByCrf(Integer id) {
        andWhere("spcs.studyParticipantCrf.crf.id =:crfId");
        setParameter("crfId", id);
    }

    public void filterByStudyParticipantCRFId(Integer id) {
        andWhere("spcs.studyParticipantCrf.id =:id");
        setParameter("id", id);
    }

    public void filterByStudyParticipantCRFIds(List<Integer> spcrfIds) {
        andWhere("spcs.studyParticipantCrf.id in (:" + SP_CRF_IDS + ")");
        setParameterList(SP_CRF_IDS, spcrfIds);
    }

    public void filterByCRFIds(List<Integer> crfIds) {
        andWhere("spcs.studyParticipantCrf.crf.id in (:" + CRF_IDS + ")");
        setParameterList(CRF_IDS, crfIds);
    }

    public void filterByStudySite(Integer id) {
        andWhere("spcs.studyParticipantCrf.studyParticipantAssignment.studySite.id =:studySiteId");
        setParameter("studySiteId", id);
    }

    public void filterBySiteIds(List<Integer> siteIds) {
        andWhere("spcs.studyParticipantCrf.studyParticipantAssignment.studySite.organization.id in (:" + SITE_IDS + ")");
        setParameterList(SITE_IDS, siteIds);
    }

    public void filterByParticipant(Integer id) {
        andWhere("spcs.studyParticipantCrf.studyParticipantAssignment.participant.id =:participantId");
        setParameter("participantId", id);
    }

    public void filterByDate(Date startDate, Date endDate) {
        andWhere("spcs.startDate between :startDate and :endDate");
        setParameter("startDate", startDate);
        setParameter("endDate", endDate);
    }

    public void filterByGreaterDate(Date current) {
        andWhere(":current < spcs.startDate");
        setParameter("current",current);
    }

    public void filterByDate(Date current) {
            andWhere(":current between spcs.startDate and spcs.dueDate");
            setParameter("current",current);
        }
    public void filterByStartEndDate(Date startDate, Date  endDate) {
            andWhere(" spcs.startDate >= :startDate and spcs.startDate <=:endDate");
            setParameter("startDate",startDate);
            setParameter("endDate",endDate);
        }

    public void filterByStatus(CrfStatus status) {
        andWhere("spcs.status =:status");
        setParameter("status", status);
    }
    
    public void filterByStatuses(List<CrfStatus> statuses) {
        andWhere("spcs.status in (:" + STATUSES + ")");
        setParameterList(STATUSES, statuses);
    }

    //Prevent hidden forms to be populated in Upcoming Forms section for a clinical staff login
    public void filterByHiddenForms(){
    	andWhere(" spcs.studyParticipantCrf.crf.hidden = false ");
    }
    
    public void filterByMarkDelete() {
        andWhere("spcs.markDelete = :" + MARK_DELETE);
        setParameter(MARK_DELETE, false);
    }

    public void setLeftJoin() {
        leftJoin("spcs.studyParticipantCrf as spc " +
                "left outer join spc.crf as crf " +
                "left outer join crf.study as study " +
                "left outer join study.studyOrganizations as sso " +
                "left outer join sso.studyOrganizationClinicalStaffs as socs " +
                "left outer join socs.organizationClinicalStaff as oc " +
                "left outer join oc.clinicalStaff as cs " +
                "left outer join cs.user as user");
    }

    public void filterByObjectIds(List<Integer> objectIds) {
        if (!objectIds.isEmpty()) {
            andWhere(String.format("%s in (:%s )", getObjectIdQueryString(), OBJECT_IDS));
            setParameterList(OBJECT_IDS, objectIds);
        }
    }
    
    protected String getObjectIdQueryString() {
        return "spcs.studyParticipantCrf.studyParticipantAssignment.studySite.id";
    }
    
    public void setLeftJoinForSite() {
        leftJoin("spcs.studyParticipantCrf as spc " +
                "left outer join spc.studyParticipantAssignment as spa " +
                "left outer join spa.studySite as ss " +
                "left outer join ss.organization as org");
    }

    public void setLeftJoinForCrf() {
        leftJoin("spcs.studyParticipantCrf as spc " +
                "left outer join spc.crf as crf ");
    }

    public void filterByHidden(boolean hidden) {
        andWhere("crf.hidden = :" + HIDDEN);
        setParameter(HIDDEN, hidden);
    }

    public void filterByParticipantStatusNot(RoleStatus status) {
         andWhere("spc.studyParticipantAssignment.status <> :status");
        setParameter("status", status);
    }

    public void filterByUsername(final String userName) {
        setLeftJoin();
        andWhere("user.username = :" + USERNAME);

        setParameter(USERNAME, userName);
//        setParameter("status1", CrfStatus.INPROGRESS);

    }

    public void filterByUsernameOnly(final String userName) {
        setLeftJoin();
        andWhere("user.username = :" + USERNAME);
        setParameter(USERNAME, userName);
      }
}