package gov.nih.nci.ctcae.core.domain;

import edu.nwu.bioinformatics.commons.CollectionUtils;
import gov.nih.nci.ctcae.core.helper.TestDataManager;
import gov.nih.nci.ctcae.core.validation.BeanValidator;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.util.ClassUtils;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.*;

public class PersistableValidatorIntegrationTestCase extends TestDataManager {


    private BeanValidator beanValidator;
    private Map<Class, List<String>> nonNullableProperties = new HashMap<Class, List<String>>();
    private Map<Class, String> tableNameVsClassNameMap = new HashMap<Class, String>();
    private static final String DEFAULT_STRING = "default string value" + UUID.randomUUID().toString();
    protected List<String> errorMessages = new ArrayList<String>();
    List<Class> domainClassesInOrder = new LinkedList<Class>();
    
    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();

        domainClassesInOrder.add(Study.class);
        domainClassesInOrder.add(CRF.class);

    }

    public void testInsertForStudies() throws Exception {


        for (Class domainClass : domainClassesInOrder) {
            Table table = (Table) domainClass.getAnnotation(Table.class);
            String tableName = table.name();

            tableNameVsClassNameMap.put(domainClass, tableName);
        }
        insertObjects();

        for (String errorMessage : errorMessages) {
            System.out.println(errorMessage);

        }


    }


    private void insertObjects() throws InstantiationException, IllegalAccessException {
        for (Class domainClass : this.domainClassesInOrder) {
            Map<String, Object> columnNameValueMap = new HashMap();
            Table table = (Table) domainClass.getAnnotation(Table.class);
            String tableName = table.name();

            BeanWrapperImpl beanWrapperImpl = new BeanWrapperImpl(domainClass);


            Field[] fields = domainClass.getDeclaredFields();
            for (Field field : fields) {
                String propertyName = field.getName();


                if (field.isAnnotationPresent(Column.class)) {
                    Column column = field.getAnnotation(Column.class);

                    String columnName = column.name();
                    boolean nullable = column.nullable();
                    if (!nullable) {

                        Class<?> returnType = beanValidator.getReturnType(beanWrapperImpl, propertyName);

                        Object columnValue = getColumnValue(returnType);

                        columnNameValueMap.put(columnName, columnValue);
                        CollectionUtils.putInMappedList(nonNullableProperties, domainClass, propertyName);
                    }
                }
                if (field.isAnnotationPresent(ManyToOne.class)) {
                    JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
                    String columnName = joinColumn.name();

                    Class<?> returnType = beanValidator.getReturnType(beanWrapperImpl, propertyName);

                    String parentTableName = tableNameVsClassNameMap.get(returnType);
                    Integer columnValue = jdbcTemplate.queryForInt(String.format("select max(id) from %s ", parentTableName));

                    columnNameValueMap.put(columnName, columnValue);

                    CollectionUtils.putInMappedList(nonNullableProperties, domainClass, propertyName);
                }
            }


            StringBuilder queryString = new StringBuilder(String.format("INSERT INTO %s", tableName));
            StringBuilder columnNameString = new StringBuilder(String.format(" ("));
            StringBuilder columnValueString = new StringBuilder(String.format(" values("));
            StringBuilder columnValues = new StringBuilder(String.format(" values("));
            Set<String> columnNames = columnNameValueMap.keySet();
            int i = 0;
            Object[] columnValuesArray = new Object[columnNames.size()];
            for (String columnName : columnNames) {
                Object columnValue = columnNameValueMap.get(columnName);

                if (i == 0) {
                    columnNameString.append(String.format("%s ", columnName));
                    columnValueString.append(String.format(" ? "));
                    columnValues.append(String.format(String.format(" '%s' ", columnValue.toString())));
                } else {
                    columnNameString.append("," + columnName + " ");
                    columnValueString.append(String.format(" ,? "));
                    columnValues.append(String.format(String.format(" ,'%s' ", columnValue.toString())));

                }
                columnValuesArray[i] = columnValue;
                i++;
            }


            System.out.println(String.format("insert formatted query is -:  %s", queryString.toString() + columnNameString + ") " + columnValues + ")"));

            queryString.append(columnNameString);
            queryString.append(") ");
            queryString.append(columnValueString);
            queryString.append(") ");

            String sqlQueryString = queryString.toString();
            System.out.println(String.format("insert query is -:  %s", sqlQueryString));


            jdbcTemplate.update(sqlQueryString, columnValuesArray);
            commit();
            // deleteFromTables(new String[]{tableName});

            i = 0;

            for (String columnName : columnNames) {
                Object columnValue = columnNameValueMap.get(columnName);
                columnValuesArray[i] = null;
                try {
                    jdbcTemplate.update(sqlQueryString, columnValuesArray);
                    String errorMessage = String.format("foreign key or not null constraints is not specified for column  '%s' of table  '%s'", columnName, tableName);
                    errorMessages.add(errorMessage);
                } catch (DataIntegrityViolationException e) {
                    //expecting
                    commit();

                }
                columnValuesArray[i] = columnValue;
                i++;
            }


            //must be able to insert data if all constraints have been specified

        }
    }


    private void commit() {
        setComplete();
        endTransaction();
        startNewTransaction();
    }

    private Object getColumnValue(Class<?> returnType) throws InstantiationException, IllegalAccessException {
    	Object columnValue;
    	
        if (ClassUtils.isAssignable(String.class, returnType)) {
            return DEFAULT_STRING;
        }
        if (returnType.isEnum()) {
            Object[] enumConstants = returnType.getEnumConstants();
            return enumConstants[0].toString();
        }
        
        
        if(returnType.getName().equals("java.lang.Boolean")){
        	columnValue = Boolean.valueOf(false);
        }else{
        	columnValue = returnType.newInstance();
        }
        

        return columnValue;
    }

    public void setBeanValidator(BeanValidator beanValidator) {
        this.beanValidator = beanValidator;
    }

    //    public void setDomainClasses(List<Class> domainClasses) {
//        this.domainClasses = domainClasses;
//    }


}