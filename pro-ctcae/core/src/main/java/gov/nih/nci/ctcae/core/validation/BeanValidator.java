package gov.nih.nci.ctcae.core.validation;

import edu.nwu.bioinformatics.commons.CollectionUtils;
import gov.nih.nci.ctcae.core.domain.Persistable;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import org.springframework.beans.BeanWrapperImpl;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author Vinay Kumar
 * @crated Feb 9, 2009
 */
public class BeanValidator {

    List<Class> domainClasses = new ArrayList<Class>();
    List<Class> domainClassesInOrder = new LinkedList<Class>();

    private Map<Class, List<String>> nonNullableProperties = new HashMap<Class, List<String>>();

    private Class<?> findParentClasses(Class domainClass) {

        BeanWrapperImpl beanWrapperImpl = new BeanWrapperImpl(domainClass);
        Field[] fields = domainClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ManyToOne.class)) {
                String propertyName = field.getName();
                Class<?> returnType = getReturnType(beanWrapperImpl, propertyName);
                return findParentClasses(returnType);
            }
        }
        return domainClass;
    }

    public Class<?> getReturnType(BeanWrapperImpl beanWrapperImpl, String propertyName) {
        PropertyDescriptor propertyDescriptor = beanWrapperImpl.getPropertyDescriptor(propertyName);
        Method readMethod = propertyDescriptor.getReadMethod();
        Class<?> returnType = readMethod.getReturnType();
        return returnType;
    }

    public BeanValidator(List<Class> domainClasses) {
        this.domainClasses = domainClasses;

//        for (Class domainClass : this.domainClasses) {
//
//            Class<?> parentClass = findParentClasses(domainClass);
//            if (!domainClassesInOrder.contains(parentClass)) {
//                domainClassesInOrder.add(parentClass);
//            }
//            if (!domainClassesInOrder.contains(domainClass)) {
//                domainClassesInOrder.add(domainClass);
//            }
//        }

        for (Class domainClass : this.domainClasses) {

            Field[] fields = domainClass.getDeclaredFields();
            for (Field field : fields) {

                if (field.isAnnotationPresent(Column.class)) {
                    Column column = field.getAnnotation(Column.class);
                    boolean nullable = column.nullable();
                    if (!nullable) {
                        String propertyName = field.getName();
                        CollectionUtils.putInMappedList(nonNullableProperties, domainClass, propertyName);
                    }
                }
                if (field.isAnnotationPresent(ManyToOne.class)) {
                    if (field.isAnnotationPresent(JoinColumn.class)) {
                        JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
                        if (!joinColumn.nullable()) {
                            String propertyName = field.getName();
                            CollectionUtils.putInMappedList(nonNullableProperties, domainClass, propertyName);
                        }
                    }
                }
            }
        }


    }

    public void validate(final Persistable persistable) {

        List<String> propertyNames = nonNullableProperties.get(persistable.getClass());
        if (propertyNames != null) {
            BeanWrapperImpl beanWrapperImpl = new BeanWrapperImpl(persistable);
            for (String propertyName : propertyNames) {
                Object propertyValue = beanWrapperImpl.getPropertyValue(propertyName);
                if (propertyValue == null) {
                    throw new CtcAeSystemException(String.format("non nullable property %s of object %s must not be null", propertyName, persistable.getClass().getName()));
                }

            }
        }
    }

    public void insertStatement(final Persistable persistable) {

        List<String> propertyNames = nonNullableProperties.get(persistable.getClass());
        BeanWrapperImpl beanWrapperImpl = new BeanWrapperImpl(persistable);
        for (String propertyName : propertyNames) {
            Object propertyValue = beanWrapperImpl.getPropertyValue(propertyName);
            if (propertyValue == null) {
                throw new CtcAeSystemException(String.format("non nullable property %s of object %s must not be null", propertyName, persistable.getClass().getName()));
            }

        }
    }

    public List<Class> getDomainClasses() {
        return domainClasses;
    }

    public List<Class> getDomainClassesInOrder() {
        return domainClassesInOrder;
    }
}
