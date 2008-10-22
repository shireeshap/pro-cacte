package gov.nih.nci.ctcae.core;

import edu.nwu.bioinformatics.commons.testing.CoreTestCase;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.easymock.IArgumentMatcher;
import org.easymock.classextension.EasyMock;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author
 */
public abstract class AbstractTestCase extends CoreTestCase {


    private Log log = LogFactory.getLog(getClass());

    @Override
	protected void setUp() throws Exception {
        log.debug("---- Begin test " + getName() + " ----");
        super.setUp();


    }

    @Override
	protected void tearDown() throws Exception {
        super.tearDown();
        log.debug("----  End  test " + getName() + " ----");
    }

    public static void assertNotEquals(Object expected, Object actual) {
        assertNotEquals(null, expected, actual);
    }

    public static void assertNotEquals(String message, Object expected, Object actual) {
        assertFalse(prependMessage(message) + expected + " is equal to " + actual,
                expected == null ? actual == null : expected.equals(actual));
    }

    public static void assertEqualsAndNotSame(Object expected, Object actual) {
        assertEqualsAndNotSame(null, expected, actual);
    }

    protected Set<Object> mocks = new HashSet<Object>();

    ////// MOCK REGISTRATION AND HANDLING

    public <T> T registerMockFor(Class<T> forClass) {
        return registered(EasyMock.createMock(forClass));
    }

    public <T> T registerMockFor(Class<T> forClass, Method... methodsToMock) {
        return registered(EasyMock.createMock(forClass, methodsToMock));
    }

//    public <T extends AbstractRepository<?,?>> T registerRepositoryMockFor(Class<T> forClass) {
//        List<Method> methods = new LinkedList<Method>(Arrays.asList(forClass.getMethods()));
//        for (Iterator<Method> iterator = methods.iterator(); iterator.hasNext();) {
//            Method method = iterator.next();
//            if ("getPersistableClass".equals(method.getName())) {
//                iterator.remove();
//            }
//        }
//        return registerMockFor(forClass, methods.toArray(new Method[methods.size()]));
//    }

    public void replayMocks() {
        for (Object mock : mocks) EasyMock.replay(mock);
    }

    public void verifyMocks() {
        for (Object mock : mocks) EasyMock.verify(mock);
    }

    public void resetMocks() {
        for (Object mock : mocks) EasyMock.reset(mock);
    }

    private <T> T registered(T mock) {
        mocks.add(mock);
        return mock;
    }

    protected static <T> T matchByProperties(T template) {
        org.easymock.EasyMock.reportMatcher(new PropertyMatcher<T>(template));
        return null;
    }

    /**
     * Finds a file in the same module as the given class.  For example, say you have a package named
     * <kbd>web</kbd> and you need to refer to <kbd>web/src/main/foo/bar</kbd>.  Pass this method a
     * class that's in that module (i.e., compiled into <kbd>web/target/test-classes</kbd>) and
     * <kbd>src/main/foo/bar</kbd> to get the path to the local copy of
     * <kbd>web/src/main/foo/bar</kbd>.
     *
     * @param clazz    A class from the desired module
     * @param filename Path to the desired file, relative to the module root
     */
    public static File getModuleRelativeFile(Class<?> clazz, String filename) {
        try {
            File classDirectory = new File(clazz.getResource("/").toURI());
            return new File(new File(classDirectory, "../.."), filename);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Could not locate '/' relative to " + clazz.getName(), e);
        }
    }

    /**
     * Easymock matcher that compares two objects on their property values
     */
    @SuppressWarnings("unchecked")
    private static class PropertyMatcher<T> implements IArgumentMatcher {
        private T template;
        private Map<String, Object> templateProperties;

        public PropertyMatcher(T template) {
            this.template = template;
            try {
                templateProperties = PropertyUtils.describe(template);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

        public boolean matches(Object argument) {
            try {
                Map<String, Object> argumentProperties = PropertyUtils.describe(argument);
                for (Map.Entry<String, Object> entry : templateProperties.entrySet()) {
                    Object argProp = argumentProperties.get(entry.getKey());
                    Object templProp = entry.getValue();
//                    if (!ComparisonUtils.nullSafeEquals(templProp, argProp)) {
//                        throw new AssertionError("Argument's " + entry.getKey()
//                                + " property doesn't match template's: " + templProp + " != " + argProp);
//                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }

            return true;
        }


        public void appendTo(StringBuffer buffer) {
            buffer.append(template).append(" (by properties)");
        }
    }
}
