package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

//
/**
 * @author Vinay Kumar
 */

@Entity
@Table(name = "PRIVILEGES")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {
        @Parameter(name = "sequence", value = "seq_privileges_id")})
public class Privilege extends BasePersistable {


    /**
     * The id.
     */
    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;

    @Column(name = "privilege_name", nullable = false, unique = true)
    private String privilegeName;

    @Column(name = "display_name", nullable = false)
    private String displayName;


    /* (non-Javadoc)
    * @see gov.nih.nci.ctcae.core.domain.Persistable#getId()
    */
    public Integer getId() {
        return id;
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.core.domain.Persistable#setId(java.lang.Integer)
     */
    public void setId(Integer id) {
        this.id = id;
    }

    public String getPrivilegeName() {
        return privilegeName;
    }

    public void setPrivilegeName(String privilegeName) {
        this.privilegeName = privilegeName;
    }


    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Privilege roles = (Privilege) o;

        if (privilegeName != null ? !privilegeName.equals(roles.privilegeName) : roles.privilegeName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = privilegeName != null ? privilegeName.hashCode() : 0;
        return result;
    }
}