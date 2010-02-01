package gov.nih.nci.ctcae.core.domain;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class ValidValue extends BasePersistable {
    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    protected Integer id;

    @Column(name = "value", nullable = false)
    protected String value;

    @Column(name = "display_order", nullable = true)
    protected Integer displayOrder;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Integer getCode() {
        return displayOrder;
    }

    public String getDesc() {
        return value;
    }
}