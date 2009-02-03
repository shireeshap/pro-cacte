package gov.nih.nci.ctcae.web.editor;

import gov.nih.nci.ctcae.core.domain.Persistable;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import org.apache.commons.lang.StringUtils;

import java.beans.PropertyEditorSupport;

//
/**
 * A {@link java.beans.PropertyEditor} that supports binding domain objects by their IDs.
 *
 * @author
 */
public class RepositoryBasedEditor extends PropertyEditorSupport {

    /**
     * The null for blanks.
     */
    private boolean strictIdChecking, nullForBlanks;

    /**
     * The finder repository.
     */
    private FinderRepository finderRepository;

    /**
     * The class arg.
     */
    private Class<? extends Persistable> classArg;

    /**
     * Same as <code>{@link #RepositoryBasedEditor(gov.nih.nci.ctcae.core.repository.FinderRepository , boolean, boolean,Class<? extends Persistable> )}(dao, false, true)</code>
     *
     * @param finderRepository the finder repository
     * @param classArg         the class arg
     */
    public RepositoryBasedEditor(FinderRepository finderRepository, Class<? extends Persistable> classArg) {
        this(finderRepository, false, true, classArg);
    }

    /**
     * The Constructor.
     *
     * @param finderRepository The dao against which to resolve provided IDs
     * @param strictIdChecking Whether or not to allow {@link #setValue} where the value has no ID.
     *                         Due to the sometimes-odd way spring uses PEs, you probably want this to be false.
     * @param nullForBlanks    Whether to treat a blank string as "no object".  If false, you'll get a
     *                         NumberFormatException for blank strings.
     * @param classArg         the class arg
     */
    public RepositoryBasedEditor(FinderRepository finderRepository, boolean strictIdChecking, boolean nullForBlanks,
                                 Class<? extends Persistable> classArg) {
        this.finderRepository = finderRepository;
        this.strictIdChecking = strictIdChecking;
        this.nullForBlanks = nullForBlanks;
        this.classArg = classArg;
    }

    /**
     * Sets the strict id checking.
     *
     * @param strictIdChecking the new strict id checking
     */
    public void setStrictIdChecking(boolean strictIdChecking) {
        this.strictIdChecking = strictIdChecking;
    }

    /**
     * Sets the null for blanks.
     *
     * @param nullForBlanks the new null for blanks
     */
    public void setNullForBlanks(boolean nullForBlanks) {
        this.nullForBlanks = nullForBlanks;
    }

    /* (non-Javadoc)
     * @see java.beans.PropertyEditorSupport#setValue(java.lang.Object)
     */
    @Override
    public void setValue(Object value) {
        if (value != null && !(classArg.isAssignableFrom(value.getClass()))) {
            throw new IllegalArgumentException("This editor only handles instances of " + classArg.getName());
        }
        setValue((Persistable) value);
    }

    /**
     * Sets the value.
     *
     * @param value the new value
     */
    private void setValue(Persistable value) {
        if (value != null && value.getId() == null && strictIdChecking) {
            throw new IllegalArgumentException("This editor can't handle values without IDs");
        }
        super.setValue(value);
    }

    /* (non-Javadoc)
     * @see java.beans.PropertyEditorSupport#getAsText()
     */
    @Override
    public String getAsText() {
        Persistable domainObj = (Persistable) getValue();
        if (domainObj == null) {
            return null;
        } else {
            Integer id = domainObj.getId();
            return id == null ? null : id.toString();
        }
    }

    /* (non-Javadoc)
     * @see java.beans.PropertyEditorSupport#setAsText(java.lang.String)
     */
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        Persistable newValue;
        if (text == null) {
            newValue = null;
        } else if (nullForBlanks && StringUtils.isBlank(text)) {
            newValue = null;
        } else {
            Integer id = new Integer(text);
            newValue = finderRepository.findById(classArg, id);
            if (newValue == null) {
                throw new IllegalArgumentException("There is no " + classArg.getSimpleName() + " with id=" + id);
            }
        }
        setValue(newValue);
    }
}
