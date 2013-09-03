package gov.nih.nci.ctcae.core.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;


/**
 * @author AmeyS
 * Class ProctcaeGradeMappingVersion
 *
 */
@Entity
@Table(name = "proctcae_grade_mapping_versions")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_grade_mapping_versions_id")})
public class ProctcaeGradeMappingVersion extends BasePersistable {
    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;

    @Column(name = "version", nullable = false, unique = true)
    private String version;


	@Column(name = "name", nullable = false, unique = true)
    private String name;
    

    public ProctcaeGradeMappingVersion() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVersion() {
    	return version;
    }
    
    public void setVersion(String version) {
    	this.version = version;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProctcaeGradeMappingVersion)) return false;

        ProctcaeGradeMappingVersion that = (ProctcaeGradeMappingVersion) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (version != null ? !version.equals(that.version) : that.version != null) return false;

        return true;
    }

    public int hashCode() {
    	int result = (name != null ? name.hashCode() : 0);
    	result = 31 * result + (version != null? version.hashCode() : 0);
        return result; 
    }
}