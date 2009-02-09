package gov.nih.nci.ctcae.core.validation;

import edu.nwu.bioinformatics.commons.CollectionUtils;
import gov.nih.nci.ctcae.core.domain.Persistable;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import org.springframework.beans.BeanWrapperImpl;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Vinay Kumar
 * @crated Feb 9, 2009
 */
public class BeanValidator {

    List<Class> domainClasses = new ArrayList<Class>();

    private Map<Class, List<String>> nonNullableProperties = new HashMap<Class, List<String>>();


    public BeanValidator(List<Class> domainClasses) {
        this.domainClasses = domainClasses;
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
                    String propertyName = field.getName();
                    CollectionUtils.putInMappedList(nonNullableProperties, domainClass, propertyName);
                }
            }
        }


    }

    public void validate(final Persistable persistable) {

        List<String> propertyNames = nonNullableProperties.get(persistable.getClass());
        BeanWrapperImpl beanWrapperImpl = new BeanWrapperImpl(persistable);
        for (String propertyName : propertyNames) {
            Object propertyValue = beanWrapperImpl.getPropertyValue(propertyName);
            if (propertyValue == null) {
                throw new CtcAeSystemException(String.format("non nullable property %s of object %s must not be null", propertyName, persistable.getClass().getName()));
            }

        }
    }

}
