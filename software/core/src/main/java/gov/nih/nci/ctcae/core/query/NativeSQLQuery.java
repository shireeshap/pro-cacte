package gov.nih.nci.ctcae.core.query;

import org.hibernate.type.NullableType;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *  User: Suneel Allareddy
 * Date: Jan 24, 2011
 * Time: 2:49:28 PM
 */
public class NativeSQLQuery extends AbstractQuery{
    private Map<String, NullableType> scalarMap = new HashMap<String, NullableType>();

    public NativeSQLQuery(String sql){
        super(sql);
    }

    public void setScalar(String key, NullableType type){
        scalarMap.put(key, type);
    }

    public Set<String> getScalarNames(){
        return scalarMap.keySet();
    }

    public NullableType getScalarType(String key){
        return scalarMap.get(key);
    }

    public Map<String, NullableType> getScalarMap() {
        return scalarMap;
    }
}
