package gov.nih.nci.ctcae.core.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * The Class StudySite.
 *
 * @author Vinay Kumar
 * @since Oct 7, 2008
 */

@Entity
@DiscriminatorValue(value = "SST")
public class StudySite extends StudyOrganization {


}