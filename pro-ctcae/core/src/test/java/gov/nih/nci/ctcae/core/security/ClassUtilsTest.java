package gov.nih.nci.ctcae.core.security;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.ParticipantSchedule;
import gov.nih.nci.ctcae.core.domain.Persistable;
import gov.nih.nci.ctcae.core.query.CRFQuery;
import gov.nih.nci.ctcae.core.query.Query;
import gov.nih.nci.ctcae.core.repository.ClinicalStaffRepository;
import gov.nih.nci.ctcae.core.repository.StudyRepository;
import junit.framework.TestCase;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ClassUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vinay Kumar
 * @crated Mar 3, 2009
 */
public class ClassUtilsTest extends TestCase {
    private ClinicalStaffRepository clinicalStaffRepository;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        clinicalStaffRepository = new ClinicalStaffRepository();


    }

    public void testMethods() throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        Class<StudyRepository> serviceClass = StudyRepository.class;
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
            Method readMethod = propertyDescriptor.getReadMethod();
            if (readMethod != null) {
                System.out.println(String.format("found gettter method %s", readMethod.getName()));
                ignoreMethods.add(readMethod.getName());
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
                    System.out.println("class type is:" + classType);
                    if (ClassUtils.isAssignable(classType, Integer.class)) {
                        parameters[i] = Integer.valueOf(1);
                    } else if (ClassUtils.isAssignable(classType, Persistable.class)) {
                        parameters[i] = new CRF();
                    } else if (ClassUtils.isAssignable(classType, Query.class)) {
                        parameters[i] = new CRFQuery();
                    } else if (ClassUtils.isAssignable(classType, ParticipantSchedule.ScheduleType.class)) {
                        parameters[i] = ParticipantSchedule.ScheduleType.CYCLE;
                    } else if (ClassUtils.isAssignable(classType, List.class)) {
                        parameters[i] = new ArrayList();
                    } else {
                        parameters[i] = classType.newInstance();
                    }
                }
                if (Integer.valueOf(Method.DECLARED).equals(method.getModifiers())) {
                    System.out.println(String.format("invoking method %s of class %s with parameter %s", method.getName(), serviceClass.getName(), parameters));


                }
            }

        }
    }

}
