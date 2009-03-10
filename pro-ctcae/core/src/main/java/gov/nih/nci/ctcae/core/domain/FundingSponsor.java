package gov.nih.nci.ctcae.core.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * The Class StudyFundingSponsor.
 *
 * @author
 * @crated Oct 7, 2008
 */

@Entity
@DiscriminatorValue(value = "FSP")
public class FundingSponsor extends StudyOrganization {


}