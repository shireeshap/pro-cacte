package gov.nih.nci.ctcae.core;

import gov.nih.nci.ctcae.core.domain.BasePersistable;
import gov.nih.nci.ctcae.core.validation.annotation.NotEmpty;

import java.util.Date;

/**
 * @author
 * @crated Oct 6, 2008
 */
public class TestBean extends BasePersistable {

	private Integer id;
	private Date date;

	private String title;

	private String firstName;

	@gov.nih.nci.ctcae.core.validation.annotation.NotNull
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	@NotEmpty
	public String getTitle() {
		return title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

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
