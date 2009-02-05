package gov.nih.nci.ctcae.core.security;

import org.springframework.security.acls.IdentityUnavailableException;
import org.springframework.util.Assert;

import java.io.Serializable;

/**
 * @author Vinay Kumar
 * @crated Feb 5, 2009
 */
public class ObjectGroupIdentityImpl implements ObjectGroupIdentity {
    //~ Instance fields ================================================================================================

    private Class javaType;
    private Serializable identifier;

    //~ Constructors ===================================================================================================


    /**
     * Creates the <code>ObjectGroupIdentityImpl</code> based on the passed
     * object instance. The passed object must provide a <code>getId()</code>
     * method, otherwise an exception will be thrown. The object passed will
     * be considered the {@link #javaType}, so if more control is required,
     * an alternate constructor should be used instead.
     *
     * @param object the domain object instance to create an identity for
     * @throws org.springframework.security.acls.IdentityUnavailableException
     *          if identity could not be extracted
     */
    public ObjectGroupIdentityImpl(Object object) throws IdentityUnavailableException {
        Assert.notNull(object, "object cannot be null");

        //this.javaType = ClassUtils.getUserClass(object.getClass());

        this.javaType = object.getClass();

        this.identifier = this.javaType.getName();
    }

    //~ Methods ========================================================================================================

    /**
     * Important so caching operates properly.<P>Considers an object of the same class equal if it has the same
     * <code>classname</code> and <code>id</code> properties.</p>
     * <p/>
     * <p>
     * Note that this class uses string equality for the identifier field, which ensures it better supports
     * differences between {@link org.springframework.security.acls.jdbc.LookupStrategy} requirements and the domain object represented by this
     * <code>ObjectGroupIdentityImpl</code>.
     * </p>
     *
     * @param arg0 object to compare
     * @return <code>true</code> if the presented object matches this object
     */
    public boolean equals(Object arg0) {
        if (arg0 == null) {
            return false;
        }

        if (!(arg0 instanceof ObjectGroupIdentityImpl)) {
            return false;
        }

        ObjectGroupIdentityImpl other = (ObjectGroupIdentityImpl) arg0;

        if (this.getIdentifier().toString().equals(other.getIdentifier().toString()) && this.getJavaType().equals(other.getJavaType())) {
            return true;
        }

        return false;
    }

    public Serializable getIdentifier() {
        return identifier;
    }

    public Class getJavaType() {
        return javaType;
    }

    /**
     * Important so caching operates properly.
     *
     * @return the hash
     */
    public int hashCode() {
        int code = 31;
        code ^= this.javaType.hashCode();
        code ^= this.identifier.hashCode();

        return code;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getClass().getName()).append("[");
        sb.append("Java Type: ").append(this.javaType.getName());
        sb.append("; Identifier: ").append(this.identifier).append("]");

        return sb.toString();
    }

}
