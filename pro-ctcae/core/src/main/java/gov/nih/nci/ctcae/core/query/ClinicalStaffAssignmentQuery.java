package gov.nih.nci.ctcae.core.query;

import gov.nih.nci.ctcae.core.domain.StudySite;

import java.util.List;

//
/**
 * User: Mehul Gulati
 * Date: Oct 15, 2008.
 */
public class ClinicalStaffAssignmentQuery extends AbstractQuery {

    /**
     * The query string.
     */
    private static String queryString = "SELECT csa from ClinicalStaffAssignment csa order by csa.id";

    private static String STUDY_SITE_IDS = "studySiteIds";
    private static String DOMAIN_OBJECT_CLASS = "domainObjectClass";


    /**
     * Instantiates a new clinical staff query.
     */
    public ClinicalStaffAssignmentQuery() {

        super(queryString);
    }

    public void filterByStudySiteIds(List<Integer> studySiteIds) {
        andWhere("csa.domainObjectId  in (:" + STUDY_SITE_IDS+")");
        setParameterList(STUDY_SITE_IDS, studySiteIds);

        andWhere("csa.domainObjectClass   =:" + DOMAIN_OBJECT_CLASS);
        setParameter(DOMAIN_OBJECT_CLASS, StudySite.class.getName());
    }




}