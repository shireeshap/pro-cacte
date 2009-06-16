package gov.nih.nci.ctcae.core.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * The Class StudyCoordinatingCenter.
 *
 * @author
 * @since Oct 7, 2008
 */

@Entity
@DiscriminatorValue(value = "DCC")
public class DataCoordinatingCenter extends StudyOrganization {


}