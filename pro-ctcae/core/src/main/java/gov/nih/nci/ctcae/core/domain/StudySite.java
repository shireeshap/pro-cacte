package gov.nih.nci.ctcae.core.domain;

import javax.persistence.*;

/**
 * @author
 * @crated Oct 7, 2008
 */

@Entity
@DiscriminatorValue(value = "SST")
public class StudySite extends StudyOrganization {


}