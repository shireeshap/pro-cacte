package gov.nih.nci.ctcae.core;

import edu.nwu.bioinformatics.commons.testing.CoreTestCase;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.CRFPage;
import gov.nih.nci.ctcae.core.domain.CrfPageItem;
import gov.nih.nci.ctcae.core.domain.Organization;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.easymock.classextension.EasyMock;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Vinay Kumar
 */
public abstract class AbstractTestCase extends CoreTestCase {

    protected Organization nci, duke;

    private Log log = LogFactory.getLog(getClass());

    @Override
    protected void setUp() throws Exception {
        log.debug("---- Begin test " + getName() + " ----");
        super.setUp();


        nci = Fixture.createOrganization("National Cancer Institute", "NCI");
        duke = Fixture.createOrganization("DUKE", "DUKE");


    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        log.debug("----  End  test " + getName() + " ----");
    }


    public static void assertEqualsAndNotSame(Object expected, Object actual) {
        assertEqualsAndNotSame(null, expected, actual);
    }

    protected Set<Object> mocks = new HashSet<Object>();

    ////// MOCK REGISTRATION AND HANDLING

    public <T> T registerMockFor(Class<T> forClass) {
        return registered(EasyMock.createMock(forClass));
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

    private void validateCrfPageItemDisplayOrder(final CRF crf) {

        int j = CrfPageItem.INITIAL_ORDER;
        for (CRFPage crfPage : crf.getCrfPages()) {

            for (int i = 0; i < crfPage.getCrfItemsSortedByDislayOrder().size(); i++) {
                assertEquals("must preserve order no", Integer.valueOf(i + j), crfPage.getCrfItemsSortedByDislayOrder().get(i).getDisplayOrder());

            }
            j = j + crfPage.getCrfPageItems().size();
        }
    }

    public void validateCrfPageAndCrfPageItemOrder(final CRF crf) {
        if (crf != null) {
            validateCrfPageItemDisplayOrder(crf);
            verifyCrfPageNumber(crf);
        }
    }


    private void verifyCrfPageNumber(final CRF crf) {
        for (int i = 0; i < crf.getCrfPages().size(); i++) {
            CRFPage crfPage = crf.getCrfPages().get(i);
            assertEquals("must preserve crf page number", Integer.valueOf(i), crfPage.getPageNumber());


        }
    }

}
