package gov.nih.nci.ctcae.core.security;

import gov.nih.nci.ctcae.core.AbstractHibernateIntegrationTestCase;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.ParticipantSchedule;
import gov.nih.nci.ctcae.core.domain.Persistable;
import gov.nih.nci.ctcae.core.query.CRFQuery;
import gov.nih.nci.ctcae.core.query.Query;
import gov.nih.nci.ctcae.core.repository.AbstractRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.AccessDeniedException;
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
public class MethodAuthorizationIntegrationTestCase extends AbstractHibernateIntegrationTestCase {

    protected PrivilegeAuthorizationCheck privilegeAuthorizationCheck;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();


    }


    protected void unauthorizeMethods(final AbstractRepository repository,
                                      final Class<? extends AbstractRepository> serviceClass, List<String> allowedMethods) throws Exception {


        Method[] methods = serviceClass.getDeclaredMethods();
        List<String> ignoreMethods = new ArrayList<String>();


        for (Method method : Object.class.getDeclaredMethods()) {
            ignoreMethods.add(method.getName());

        }
        PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(serviceClass);
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            Method writeMethod = propertyDescriptor.getWriteMethod();
            if (writeMethod != null) {
                System.out.println(String.format("found setter method %s", writeMethod.getName()));
                ignoreMethods.add(writeMethod.getName());
            }
        }


        for (Method method : methods) {
            if (!ignoreMethods.contains(method.getName())) {
                Class<?>[] classes = method.getParameterTypes();
                System.out.println(String.format("found %s method %s of class %s with parameter %s", method.getModifiers(), serviceClass.getName(), method.getName(), classes));

                Object[] parameters = new Object[classes.length];
                for (int i = 0; i < classes.length; i++) {
                    Class parameterTypeClass = classes[i];
                    Class<?> classType = Class.forName(parameterTypeClass.getName());
                    if (ClassUtils.isAssignable(classType, Integer.class)) {
                        parameters[i] = Integer.valueOf(1);
                    } else if (ClassUtils.isAssignable(classType, Persistable.class)) {
                        parameters[i] = new CRF();
                    } else if (ClassUtils.isAssignable(classType, Query.class)) {
                        parameters[i] = new CRFQuery();
                    } else if (ClassUtils.isAssignable(classType, ParticipantSchedule.ScheduleType.class)) {
                        parameters[i] = ParticipantSchedule.ScheduleType.CYCLE;
                    } else {
                        parameters[i] = classType.newInstance();
                    }
                }
                System.out.println(String.format("invoking method %s of class %s with parameter %s", method.getName(), serviceClass.getName(), parameters));
                if (Integer.valueOf(Method.DECLARED).equals(method.getModifiers())) {
                    try {
                        method.invoke(repository, parameters);
                        if (!allowedMethods.contains(method.getName())) {
                            fail("must throw access denied exception.");
                        } else {
                            System.out.println(String.format("user %s can access method %s of class %s ", SecurityContextHolder.getContext().getAuthentication().getCredentials(),
                                    method.getName(), serviceClass.getName()));

                        }
                    } catch (InvocationTargetException e) {
                        if (!allowedMethods.contains(method.getName())) {

                            assertEquals("must fail becaue of access denied." + e.getCause(), e.getTargetException().getClass(), AccessDeniedException.class);
                            assertTrue("must fail becaue of access denied" + e.getMessage(), e.getTargetException() instanceof AccessDeniedException);
                        } else {
                            assertFalse("must  not fail becaue of access denied. because user can access that method" + e.getMessage(), e.getTargetException() instanceof AccessDeniedException);

                        }
                    }
                } else {
                    System.out.println(String.format("method  %s of class %s is either private or volatile method. so dont invoke it", serviceClass.getName(), method.getName()));

                }
            }

        }
    }

    @Required
    public void setPrivilegeAuthorizationCheck(PrivilegeAuthorizationCheck privilegeAuthorizationCheck) {
        this.privilegeAuthorizationCheck = privilegeAuthorizationCheck;
    }

}
