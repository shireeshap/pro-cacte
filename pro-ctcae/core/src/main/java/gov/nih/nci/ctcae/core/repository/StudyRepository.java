package gov.nih.nci.ctcae.core.repository;

import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.query.StudyQuery;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Vinay Kumar
 * @crated Oct 7, 2008
 */

@Transactional(readOnly = true,propagation = Propagation.REQUIRED)
public class StudyRepository extends AbstractRepository<Study, StudyQuery> {

    @Override
    protected Class<Study> getPersistableClass() {
        return Study.class;

    }
}