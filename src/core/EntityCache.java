package core;

import java.lang.ref.SoftReference;
import java.util.HashMap;

public class EntityCache {

    static private HashMap<String, SoftReference<Object>> entityCache = new HashMap<>();


    public static void saveToCache(long id, Object obj) {
        String key = obj.getClass().getSimpleName() + id;

        SoftReference<Object> sr = new SoftReference<>(obj);
        entityCache.put(key, sr);
    }

    public static Object loadFromCache(Object obj, long id) {
        String key = obj.getClass().getSimpleName() + id;

        return entityCache.get(key);
    }
}