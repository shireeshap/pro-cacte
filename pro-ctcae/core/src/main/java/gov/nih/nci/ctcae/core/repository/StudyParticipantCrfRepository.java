package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrf;
import gov.nih.nci.ctcae.core.query.StudyQuery;
import gov.nih.nci.ctcae.core.query.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Harsh Agarwal
 * @crated Nov 6, 2008
 */

@Transactional(readOnly = true,propagation = Propagation.REQUIRED)
public class StudyParticipantCrfRepository extends AbstractRepository<StudyParticipantCrf, Query> {

    @Override
    protected Class<StudyParticipantCrf> getPersistableClass() {
        return StudyParticipantCrf.class;

    }
}