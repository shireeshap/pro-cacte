package gov.nih.nci.ctcae.core.security;

import gov.nih.nci.ctcae.core.AbstractHibernateIntegrationTestCase;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.ParticipantSchedule;
import gov.nih.nci.ctcae.core.domain.Persistable;
import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.query.CRFQuery;
import gov.nih.nci.ctcae.core.query.Query;
import gov.nih.nci.ctcae.core.repository.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.AccessDeniedException;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.util.ClassUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Vinay Kumar
 * @crated Mar 3, 2009
 */
public abstract class MethodAuthorizationIntegrationTestCase extends AbstractHibernateIntegrationTestCase {

    protected PrivilegeAuthorizationCheck privilegeAuthorizationCheck;

    Map<Class<? extends Repository>, List<String>> allowedMethodsMap = new HashMap<Class<? extends Repository>, List<String>>();

    protected final String EDIT_STUDY_METHOD = "save";
    protected final String CREATE_STUDY_METHOD = "save";
    protected final String SEARCH_STUDY_METHOD = "find";
    protected final String FIND_SINGLE = "findSingle";
    protected final String SEARCH_STUDY_BY_ID_METHOD = "findById";
    protected final String ADD_STUDY_SITE_CLINICAL_STAFF_METHOD = "addStudyOrganizationClinicalStaff";
    protected final String ADD_STUDY_SITE_METHOD = "addStudySite";

    protected final String SUBMIT_FORM_METHOD = "/pages/form/submit";

    protected final String CREATE_FORM_METHOD = "save";
    protected final String EDIT_FORM_METHOD = "save";
    protected final String RELEASE_FORM_METHOD = "updateStatusToReleased";
    protected final String FIND_BY_ID_METHOD = "findById";
    protected final String FIND_METHOD = "find";
    protected final String SEARCH_SINGLE_FORM_METHOD = "findSingle";
    protected final String VERSION_FORM_METHOD = "versionCrf";


    protected final String COPY_FORM_METHOD = "copy";
    protected final String DELETE_METHOD = "delete";
    protected final String ADD_FORM_SCHEDULE_CYFLE_METHOD = "generateSchedulesFromCrfCalendar";

    protected final String ADD_ORGANIZATION_CLINICAL_STAFF_METHOD = "save";
    protected final String SEARCH_CLINICAL_STAFF_METHOD = "find";
    protected final String SEARCH_CLINICAL_STAFF_BY_SS_METHOD = "findByStudyOrganizationId";
    protected final String SEARCH_CLINICAL_STAFF_BY_SS_ROLE_METHOD = "findByStudyOrganizationIdAndRole";
    protected final String SEARCH_SINGLE_CLINICAL_STAFF_METHOD = "findById";
    protected final String SEARCH_CLINICAL_STAFF_METHOD_BY_ID = "findSingle";
    protected final String CREATE_CLINICAL_STAFF_METHOD = "save";
    protected final String EDIT_CLINICAL_STAFF_METHOD = "save";


    protected final String CREATE_PARTICIPANT_METHOD = "save";
    protected final String EDIT_PARTICIPANT_METHOD = "save";
    protected final String ADD_NOTIFICATION_CLINICAL_STAFF_METHOD = "save";
    protected final String PARTICIPANT_DISPLAY_STUDY_SITES_METHOD = "save";

    protected final String SEARCH_PARTICIPANT_METHOD = "find";
    protected final String SEARCH_PARTICIPANT_BY_ID_METHOD = "findById";
    protected final String SEARCH_SINGLE_PARTICIPANT_METHOD = "findSingle";
    protected final String SEARCH_PARTICIPANT_BY_STUDYSITEID_METHOD = "findByStudySiteId";
    protected final String SEARCH_PARTICIPANT_BY_STUDYID_METHOD = "findByStudyId";

    protected final String SCHEDULE_CRF_METHOD = "save";

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();

        insertDefaultUsers();

        allowedMethodsMap.put(StudyParticipantAssignmentRepository.class, new ArrayList());
        allowedMethodsMap.put(ParticipantRepository.class, new ArrayList());
        allowedMethodsMap.put(ClinicalStaffRepository.class, new ArrayList());
        allowedMethodsMap.put(CRFRepository.class, new ArrayList());
        allowedMethodsMap.put(StudyRepository.class, new ArrayList());

    }


    protected void authorizeAndUnAuthorizeMethods(final Repository repository,
                                                  final Class<? extends Repository> serviceClass, List<String> allowedMethods) throws Exception {


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
                try {
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
                        } else if (ClassUtils.isAssignable(classType, List.class)) {
                            parameters[i] = new ArrayList();
                        } else {
                            parameters[i] = classType.newInstance();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    fail("must  not fail." + e.getMessage());

                }
                if (Integer.valueOf(Method.DECLARED).equals(method.getModifiers())) {
                    try {
                        System.out.println(String.format("invoking method %s of class %s with parameter %s", method.getName(), serviceClass.getName(), parameters));
                        method.invoke(repository, parameters);
                        if (!allowedMethods.contains(method.getName())) {
                            fail(String.format("user %s must not allowed to access %s of class %s. Please make sure to configure security (or fix ur test cases) for this method.",
                                    SecurityContextHolder.getContext().getAuthentication().getName(), method.getName(), serviceClass.getName()));
                        }
                    } catch (InvocationTargetException e) {
                        if (!allowedMethods.contains(method.getName())) {
                            assertEquals("must fail becaue of access denied." + e.getCause(), e.getTargetException().getClass(), AccessDeniedException.class);
                            assertTrue("must fail becaue of access denied" + e.getMessage(), e.getTargetException() instanceof AccessDeniedException);
                        } else {
                            if (e.getTargetException() instanceof AccessDeniedException) {
                                if (e.getTargetException().getMessage() != null && e.getTargetException().getMessage().indexOf("InstanceLevelSecurity-") != -1) {
                                    logger.debug("access denied because of instance level security");
                                } else {
                                    fail("must  not fail becaue of access denied. Because  user must be able to access that method: " + method.getName() +
                                            ". Please check if you have configured security and given privilege to user for that method? " +
                                            "exception is:" + e.getTargetException());
                                }
                            }

                        }
                    }


                }
            }

        }

    }

    protected Role getRole() {
        return null;
    }

    @Required
    public void setPrivilegeAuthorizationCheck(PrivilegeAuthorizationCheck privilegeAuthorizationCheck) {
        this.privilegeAuthorizationCheck = privilegeAuthorizationCheck;
    }

}
