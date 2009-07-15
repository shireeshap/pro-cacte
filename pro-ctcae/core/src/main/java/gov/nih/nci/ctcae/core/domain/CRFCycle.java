package gov.nih.nci.ctcae.core.domain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

//
/**
 * The Class CRFCycle.
 *
 * @author Harsh Agarwal
 * @created Feb 16, 2008
 */

@Entity
@Table(name = "CRF_CYCLES")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_crf_cycles_id")})

public class CRFCycle extends BasePersistable {

    /**
     * The Constant logger.
     */
    private static final Log logger = LogFactory.getLog(CRFCycle.class);

    /**
     * The id.
     */
    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;


    @Column(name = "cycle_days")
    private String cycleDays;

    @Column(name="cycle_order", nullable = false)
    private Integer order;
    /**
     * The crf definition.
     */
    @JoinColumn(name = "cycle_definition_id", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private CRFCycleDefinition crfCycleDefinition;



    /**
     * Instantiates a new cRF calendar.
     */
    public CRFCycle() {
        super();
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getCycleDays() {
        return cycleDays;
    }

    public void setCycleDays(String cycleDays) {
        this.cycleDays = cycleDays;
    }

    public CRFCycleDefinition getCrfDefinition() {
        return crfCycleDefinition;
    }

    public void setCrfDefinition(CRFCycleDefinition crfCycleDefinition) {
        this.crfCycleDefinition = crfCycleDefinition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CRFCycle)) return false;

        CRFCycle crfCycle = (CRFCycle) o;

        if (crfCycleDefinition != null ? !crfCycleDefinition.equals(crfCycle.crfCycleDefinition) : crfCycle.crfCycleDefinition != null)
            return false;
        if (cycleDays != null ? !cycleDays.equals(crfCycle.cycleDays) : crfCycle.cycleDays != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = cycleDays != null ? cycleDays.hashCode() : 0;
        result = 31 * result + (crfCycleDefinition != null ? crfCycleDefinition.hashCode() : 0);
        return result;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

}