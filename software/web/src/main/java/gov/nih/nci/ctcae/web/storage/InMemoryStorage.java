package gov.nih.nci.ctcae.web.storage;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by royzuniga on 8/18/15.
 */
public class InMemoryStorage {

    private static Map<InMemoryStorageKey, Object> inMemoryStorage;

    private InMemoryStorage() {
    }

    public static void initiliazeInMemoryStorage() {
        if(inMemoryStorage == null) {
            inMemoryStorage = new LinkedHashMap<>();
        }
    }


    public static Object getStoredItemsByKey(InMemoryStorageKey storageKey) {
        return inMemoryStorage.get(storageKey);
    }



    public static void putIntoStorage(InMemoryStorageKey key, Object value) {
        inMemoryStorage.put(key, value);
    }

}
