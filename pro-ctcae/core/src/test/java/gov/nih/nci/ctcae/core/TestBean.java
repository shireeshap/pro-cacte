package gov.nih.nci.ctcae.core;

import gov.nih.nci.ctcae.core.domain.BasePersistable;

import java.util.Date;

/**
 * @author
 * @crated Oct 6, 2008
 */
public class TestBean extends BasePersistable {

    private Integer id;
    private Date date;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
