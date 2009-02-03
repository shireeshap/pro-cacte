package gov.nih.nci.ctcae.web.tools;

import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.ArrayList;
import java.util.List;

//
/**
 * The Class ObjectTools.
 *
 * @author Saurabh Agrawal
 * @crated Nov 5, 2008
 */
public class ObjectTools {

    /**
     * Creates a copy of the given object with only the listed properties included.
     *
     * @param src         the src
     * @param initializer if supplied, the new instance will be passed to this object's
     *                    {@link Initializer#initialize} method before the properties are copied. This
     *                    provides an opportunity to initialize intermediate objects.
     * @param properties  the properties
     * @return a newly-created object of the same class as <code>src</code>
     */
    @SuppressWarnings("unchecked")
    public static <T> T reduce(T src, Initializer<T> initializer, String... properties) {
        T dst;
        try {
            // it doesn't seem like this cast should be necessary
            dst = (T) src.getClass().newInstance();
            if (initializer != null) initializer.initialize(dst);
        } catch (InstantiationException e) {
            throw new CtcAeSystemException("Failed to instantiate " + src.getClass().getName(), e);
        } catch (IllegalAccessException e) {
            throw new CtcAeSystemException("Failed to instantiate " + src.getClass().getName(), e);
        }

        BeanWrapper source = new BeanWrapperImpl(src);
        BeanWrapper destination = new BeanWrapperImpl(dst);
        for (String property : properties) {
            destination.setPropertyValue(property, source.getPropertyValue(property));
        }
        return dst;
    }

    /**
     * Reduce all.
     *
     * @param src         the src
     * @param initializer the initializer
     * @param properties  the properties
     * @return the list< t>
     */
    public static <T> List<T> reduceAll(List<T> src, Initializer<T> initializer,
                                        String... properties) {
        List<T> dst = new ArrayList<T>(src.size());
        for (T t : src) {
            dst.add(reduce(t, initializer, properties));
        }
        return dst;
    }

    /**
     * Creates a copy of the given object with only the listed properties included.
     *
     * @param src        the src
     * @param properties the properties
     * @return a newly-created object of the same class as <code>src</code>
     */
    public static <T> T reduce(T src, String... properties) {
        return reduce(src, null, properties);
    }

    /**
     * Reduce all.
     *
     * @param src        the src
     * @param properties the properties
     * @return the list< t>
     */
    public static <T> List<T> reduceAll(List<T> src, String... properties) {
        return reduceAll(src, null, properties);
    }

    // TODO: the initializer callback is a bit of a hack. Come up with a different way to
    // initialize intermediate subobjects.
    /**
     * The Interface Initializer.
     */
    public static interface Initializer<T> {

        /**
         * Initialize.
         *
         * @param instance the instance
         */
        void initialize(T instance);
    }

    // Static class
    /**
     * Instantiates a new object tools.
     */
    private ObjectTools() {
        super();
    }
}
