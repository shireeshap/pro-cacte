package gov.nih.nci.ctcae.core.security;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.Persistable;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.query.CRFQuery;
import gov.nih.nci.ctcae.core.query.Query;
import gov.nih.nci.ctcae.core.repository.CRFRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.security.AccessDeniedException;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.util.ClassUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vinay Kumar
 * @crated Mar 3, 2009
 */
public class ODCMethodAuthorizationIntegrationTest extends MethodAuthorizationIntegrationTestCase {
    private User user;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        user = defaultStudy.getOverallDataCoordinator().getOrganizationClinicalStaff().getClinicalStaff().getUser();


    }

    public void testAuthorizeMethods() throws Exception {
        login(user);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        crfRepository.save(new CRF());

//        authorizeUser(user);
    }

    public void testUnAuthorizeMethods() throws Exception {
        login(user);

        Method[] methods = CRFRepository.class.getDeclaredMethods();
        List<String> ignoreMethods = new ArrayList<String>();


        for (Method method : Object.class.getDeclaredMethods()) {
            ignoreMethods.add(method.getName());

        }
        PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(CRFRepository.class);
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            Method writeMethod = propertyDescriptor.getWriteMethod();
            if (writeMethod != null) {
                System.out.println(String.format("found setter method %s", writeMethod.getName()));
                ignoreMethods.add(writeMethod.getName());
            }
        }

        for (Method method : methods) {
            try {
                if (!ignoreMethods.contains(method.getName())) {
                    Class<?>[] classes = method.getParameterTypes();
                    System.out.println(String.format("found method %s of class %s with parameter %s", CRFRepository.class.getName(), method.getName(), classes));

                    Object[] parameters = new Object[classes.length];
                    for (int i = 0; i < classes.length; i++) {
                        Class parameterTypeClass = classes[i];
                        if (ClassUtils.isAssignable(Class.forName(parameterTypeClass.getName()), Integer.class)) {
                            parameters[i] = Integer.valueOf(1);
                        } else if (ClassUtils.isAssignable(Class.forName(parameterTypeClass.getName()), Persistable.class)) {
                            parameters[i] = new CRF();
                        } else if (ClassUtils.isAssignable(Class.forName(parameterTypeClass.getName()), Query.class)) {
                            parameters[i] = new CRFQuery();
                        } else {
                            parameters[i] = Class.forName(parameterTypeClass.getName()).newInstance();
                        }
                    }
                    System.out.println(String.format("invoking method %s of class %s with parameter %s", CRFRepository.class.getName(), method.getName(), parameters));
                    method.invoke(crfRepository, parameters);
                    fail("must throw access denied exception.");
                }
            } catch (InvocationTargetException e) {
                // e.printStackTrace();
                assertEquals("must fail becaue of access denied", e.getTargetException().getClass(), AccessDeniedException.class);
                assertTrue("must fail becaue of access denied", e.getTargetException() instanceof AccessDeniedException);
            }
        }


    }

}
