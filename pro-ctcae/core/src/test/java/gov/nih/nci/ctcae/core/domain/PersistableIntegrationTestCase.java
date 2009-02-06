package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.AbstractHibernateIntegrationTestCase;
import org.hibernate.SessionFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.ClassUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class PersistableIntegrationTestCase extends AbstractHibernateIntegrationTestCase {

    protected SessionFactory sessionFactory;
    protected String className;

    /**
     * Classes that do not overrride equals and hash code. These classes also include Interfaces and Abstract classes.
     */
    protected List<String> classesToIgnore = new ArrayList<String>();

    /**
     * Classes to ignore typically because either they are abstract or interface. For ex :StudyOrganization. 
     */
    protected List<String> collectionObjectClassesToIgnore = new ArrayList<String>();

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();

        classesToIgnore.add(StudyOrganization.class.getName());
        classesToIgnore.add(CRFPage.class.getName());

        collectionObjectClassesToIgnore.add(StudyOrganization.class.getName());

    }

    public void testEqualsAndHashCodeOfAllDomainObject() {
        sessionFactory = (SessionFactory) getApplicationContext().getBean("sessionFactory");
        Map classMetadata = sessionFactory.getAllClassMetadata();
        for (Object object : classMetadata.keySet()) {
            className = (String) object;
            if (!classesToIgnore.contains(className)) {
                logger.debug("validating equals and hash code method for class:" + className);
                Persistable firstPersistable = null;
                Persistable secondPersistable = null;
                try {
                    firstPersistable = (Persistable) Class.forName(className).newInstance();

                    secondPersistable = (Persistable) Class.forName(className).newInstance();
                    assertEquals(String.format("%s  must implement equals and hash code method", className), firstPersistable, secondPersistable);
                    assertEquals(String.format("%s  must implement equals and hash code method", className), firstPersistable.hashCode(), secondPersistable.hashCode());

                    firstPersistable.setId(1);
                    secondPersistable.setId(2);

                    assertEquals(String.format("%s  must not consider id for equals and hash code method", className), firstPersistable, secondPersistable);
                    assertEquals(String.format("%s  must not consider id for equals and hash code method", className), firstPersistable.hashCode(), secondPersistable.hashCode());


                } catch (Exception e) {
                    fail("failed for the class:" + className);

                    e.printStackTrace();
                }
            } else {
                logger.debug("skipping equals and hash code method implementation for class:" + className);

            }
        }

    }

    public void testEqualsAndHashCodeMustNotConsiderCollections() {
        sessionFactory = (SessionFactory) getApplicationContext().getBean("sessionFactory");
        Map classMetadata = sessionFactory.getAllClassMetadata();
        for (Object object : classMetadata.keySet()) {
            className = (String) object;
            if (!classesToIgnore.contains(className)) {
                logger.debug("validating equals and hash code method for class:" + className);
                try {
                    Persistable firstPersistable = (Persistable) Class.forName(className).newInstance();
                    Persistable secondPersistable = (Persistable) Class.forName(className).newInstance();
                    BeanWrapperImpl firstBeanWrapper = new BeanWrapperImpl(firstPersistable);

                    PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(firstPersistable.getClass());
                    for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                        Method readMethod = propertyDescriptor.getReadMethod();

                        if (ClassUtils.isAssignable(Collection.class, readMethod.getReturnType())) {
                            logger.debug(String.format("found collection return type for method %s in class %s", readMethod.getName(), className));

                            Collection collection = (Collection) firstBeanWrapper.getPropertyValue(propertyDescriptor.getName());

                            Class collectionObjectClass = getCollectionObjectClassName(readMethod);

                            if (!collectionObjectClassesToIgnore.contains(collectionObjectClass.getName())) {

                                try {
                                    collection.add(collectionObjectClass.newInstance());
                                } catch (Exception e) {
                                    fail("failed for the collection class:" + collectionObjectClass);
                                }
                            }
                            assertEquals(String.format("%s  must not consider collection property %s for equals and hash code method", className, readMethod.getName()),
                                    firstPersistable, secondPersistable);

                        }


                    }


                } catch (Exception e) {

                    fail("failed for the class:" + className + ". Exceptiopn is: " + e.getMessage());
                }
            } else {
                logger.debug("skipping equals and hash code method implementation for class:" + className);

            }
        }

    }

    private Class getCollectionObjectClassName(Method readMethod) {
        Type type = readMethod.getGenericReturnType();

        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type[] typeArguments = parameterizedType.getActualTypeArguments();
            for (Type typeArgument : typeArguments) {
                return (Class) typeArgument;
            }
        }
        return null;
    }


}