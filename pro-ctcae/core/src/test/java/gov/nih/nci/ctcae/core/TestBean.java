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

    private String firstName;
    private String lastName;
    private String title;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmailId() {
        return null;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
