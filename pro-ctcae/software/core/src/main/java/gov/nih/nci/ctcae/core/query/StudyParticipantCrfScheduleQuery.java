package gov.nih.nci.ctcae.core.query;

import gov.nih.nci.ctcae.core.domain.CrfStatus;

import javax.net.ssl.SSLEngineResult;
import java.util.Date;
import java.util.List;

//

/**
 * @author mehul
 */

public class StudyParticipantCrfScheduleQuery extends AbstractQuery {

    private static String queryString = "SELECT spcs from StudyParticipantCrfSchedule spcs";
    private static String queryString1 = "SELECT count(distinct spcs) from StudyParticipantCrfSchedule spcs";
    private static String CRF_IDS = "ids";
    private static String SP_CRF_IDS = "sp_crf_ids";
    private static String USERNAME = "username";
    private static String MARK_DELETE = "markDelete";

    public StudyParticipantCrfScheduleQuery() {
        super(queryString);
    }

    public StudyParticipantCrfScheduleQuery(boolean count) {
        super(queryString1);
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

    public void filterByStatus(CrfStatus status) {
        andWhere("spcs.status =:status");
        setParameter("status", status);
    }

    public void filterByMarkDelete() {
        andWhere("spcs.markDelete = :" + MARK_DELETE);
        setParameter(MARK_DELETE, false);
    }

    public void setLeftJoin() {
        leftJoin("spcs.studyParticipantCrf as spc " +
                "left outer join spc.crf as crf " +
                "left outer join crf.study as study " +
                "left outer join study.studyOrganizations as so " +
                "left outer join so.studyOrganizationClinicalStaffs as socs " +
                "left outer join socs.organizationClinicalStaff as oc " +
                "left outer join oc.clinicalStaff as cs " +
                "left outer join cs.user as user");
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