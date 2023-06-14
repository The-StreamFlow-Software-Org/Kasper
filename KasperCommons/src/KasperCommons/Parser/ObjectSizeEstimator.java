package KasperCommons.Parser;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.IdentityHashMap;
import java.util.Map;

public class ObjectSizeEstimator {
    private static final int OBJECT_HEADER_SIZE = 8; // Object header size assumed to be 8 bytes

    public static long estimateSize(Object object) throws IllegalAccessException {
        Map<Object, Object> visited = new IdentityHashMap<>();
        return estimateSize(object, visited);
    }

    private static long estimateSize(Object object, Map<Object, Object> visited) throws IllegalAccessException {
        if (object == null || visited.containsKey(object)) {
            return 0;
        }

        visited.put(object, null);

        Class<?> clazz = object.getClass();
        if (clazz.isArray()) {
            return estimateArraySize(object, visited);
        }

        long size = OBJECT_HEADER_SIZE;
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                if (!Modifier.isStatic(field.getModifiers())) {
                    field.setAccessible(true); // Set the field accessible
                    Object fieldValue = field.get(object);
                    if (fieldValue != null) {
                        size += estimateSize(fieldValue, visited);
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }

        return size;
    }

    private static long estimateArraySize(Object array, Map<Object, Object> visited) throws IllegalAccessException {
        long size = OBJECT_HEADER_SIZE;

        int length = java.lang.reflect.Array.getLength(array);
        for (int i = 0; i < length; i++) {
            Object element = java.lang.reflect.Array.get(array, i);
            if (element != null) {
                size += estimateSize(element, visited);
            }
        }

        return size;
    }
}
