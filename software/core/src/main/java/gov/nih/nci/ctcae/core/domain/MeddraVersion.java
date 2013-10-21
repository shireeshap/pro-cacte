package gov.nih.nci.ctcae.core.domain;

/**
 * Created by IntelliJ IDEA.
 * User: tsneed
 * Date: May 18, 2009
 * Time: 9:04:46 AM
 * To change this template use File | Settings | File Templates.
 */

import gov.nih.nci.cabig.ctms.domain.AbstractImmutableDomainObject;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * This class represents the MeddraVersion domain object associated with the Adverse event report.
 *
 * @author Krikor Krumlian
 */

// No Direct conncection to Meddra tables implemented yet - Will need to be
// If or when we start supporting multiple versions of MedDRA
@Entity
@Table(name = "meddra_versions")
public class MeddraVersion extends AbstractImmutableDomainObject {
    private String name;

    // //// BEAN PROPERTIES

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MeddraVersion that = (MeddraVersion) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}

