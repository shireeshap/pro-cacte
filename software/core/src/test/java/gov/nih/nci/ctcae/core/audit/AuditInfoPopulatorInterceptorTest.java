package gov.nih.nci.ctcae.core.audit;


import gov.nih.nci.ctcae.core.helper.TestDataManager;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

/**
 * @author Suneel Allareddy
 */
public class AuditInfoPopulatorInterceptorTest extends TestDataManager {

    AuditInfoPopulatorInterceptor auditInterceptor;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(super.getConfigLocations());
        auditInterceptor = (AuditInfoPopulatorInterceptor) applicationContext.getBean("auditInfoPopulatorInterceptor");
    }

    public void testInvoke() throws Throwable {

        MethodInvocation method = new MethodInvocation() {

            public Method getMethod() {
                // TODO Auto-generated method stub
                return null;
            }

            public Object[] getArguments() {
                // TODO Auto-generated method stub
                return null;
            }

            public AccessibleObject getStaticPart() {
                // TODO Auto-generated method stub
                return null;
            }

            public Object getThis() {
                // TODO Auto-generated method stub
                return null;
            }

            public Object proceed() throws Throwable {
                return gov.nih.nci.cabig.ctms.audit.DataAuditInfo.getLocal() != null;
            }

        };


        Object retVal = auditInterceptor.invoke(method);
        assertNotNull(retVal);
        assertEquals(Boolean.TRUE, retVal);
    }

    public void testInvoke_ThrowingException() throws Throwable {
        final Exception e = new RuntimeException("test");
        MethodInvocation method = new MethodInvocation() {

            public Method getMethod() {
                // TODO Auto-generated method stub
                return null;
            }

            public Object[] getArguments() {
                // TODO Auto-generated method stub
                return null;
            }

            public AccessibleObject getStaticPart() {
                // TODO Auto-generated method stub
                return null;
            }

            public Object getThis() {
                // TODO Auto-generated method stub
                return null;
            }

            public Object proceed() throws Throwable {
                if (true) throw e;
                return gov.nih.nci.cabig.ctms.audit.DataAuditInfo.getLocal() != null;
            }

        };
        try {
            auditInterceptor.invoke(method);
            fail("must throw exception");
        } catch (Exception e1) {
            assertSame(e, e1);
        }

    }

}
