package gov.nih.nci.ctcae.core.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author
 * @crated Oct 7, 2008
 */

@Entity
@DiscriminatorValue(value = "SFS")
public class StudyFundingSponsor extends StudyOrganization {


}