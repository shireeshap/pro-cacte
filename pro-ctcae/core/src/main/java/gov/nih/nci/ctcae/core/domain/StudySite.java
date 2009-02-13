package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.Cascade;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class StudySite.
 *
 * @author Vinay Kumar
 * @crated Oct 7, 2008
 */

@Entity
@DiscriminatorValue(value = "SST")
public class StudySite extends StudyOrganization {




}