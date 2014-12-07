package com.bus.services.util;

import java.util.*;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.StringUtils;

/**
 * Collection Utils.
 * <p>User: Nick Heudecker</p>
 * <p>Date: Jun 18, 2008</p>
 * <p>Time: 10:52:29 PM</p>
 */
public final class CollectionUtil extends org.apache.commons.collections.CollectionUtils {
    public static List<String> asStrings(Collection c) {
        List<String> strings = new ArrayList<String>();
        if (isNotEmpty(c)) {
            for (Object o : c) {
                if (o != null) {
                    strings.add(o.toString());
                }
            }
        }
        return strings;
    }

    public static <T> List<T> clean(List<T> items) {
        List<T> cleanedList = new ArrayList<T>();
        if (isNotEmpty(items)) {
            for (T item : items) {
                if (item != null && StringUtils.isNotBlank(String.valueOf(item))) {
                    cleanedList.add(item);
                }
            }
        }
        return cleanedList;
    }

    public static <T> Set<T> clean(Set<T> items) {
        Set<T> cleanedSet = new HashSet<T>();
        if (isNotEmpty(items)) {
            for (T item : items) {
                if (item != null && StringUtils.isNotBlank(String.valueOf(item))) {
                    cleanedSet.add(item);
                }
            }
        }
        return cleanedSet;
    }

    /**
     * Remove first {@param count} items from {@param collection}
     * @param collection The collection
     * @param count the count of items to remove
     */
    public static void remove(Collection collection, int count) {
        if (count > collection.size()) {
            collection.clear();
        }
        for (Iterator i = collection.iterator(); i.hasNext(); ) {
            if (count == 0) { break; }
            i.next();
            i.remove();
            count--;
        }
    }

    public static String concat(Collection<String> collection, String concatStr, String defaultValue) {
        if (isEmpty(collection)) {
            return defaultValue;
        }
        return StringUtils.join(collection.toArray(new String[collection.size()]), concatStr);
    }

    public static <T> List<T> filter(List<T> collection, Predicate predicate) {
        List<T> c = new ArrayList<T>();
        if (isNotEmpty(collection)) {
            for (T o : collection) {
                if (predicate.evaluate(o)) {
                    c.add(o);
                }
            }
        }
        return c;
    }

    public static <T> T first(Collection<T> c) {
        return (T) itemAt(c, 0);
    }

    public static boolean containsAnyIgnoreCase(Collection<String> set1, Collection<String> set2) {
        for (String s1 : set1) {
            for (String s2 : set2) {
                if (StringUtils.equalsIgnoreCase(s1, s2)) { return true; }
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public static <T> T itemAt(Collection<T> c, int index) {
        if (c == null || c.isEmpty() || index < 0 || c.size() < index + 1) { return null; }
        return (T) c.toArray()[index];
    }

    public static <T> T pop(List<T> list) {
        if (isEmpty(list)) { return null; }
        Iterator<T> i = list.iterator();
        T object = i.next();
        i.remove();
        return object;
    }

    public static int size(Collection<?> collection) {
        return isEmpty(collection) ? 0 : collection.size();
    }

    public static List<String> split(String s, String separators) {
        String[] items = StringUtils.split(s, separators);
        if (items == null || items.length == 0) {
            return new ArrayList<String>();
        }
        return Arrays.asList(items);
    }

    public static boolean isEmpty(Map map) {
        return map == null || map.size() == 0;
    }

    public static boolean isNotEmpty(Map map) {
        return !isEmpty(map);
    }

    public static <T> T getLast(List<T> list) {
        T last = null;
        if (!isEmpty(list)) {
            last = list.get(list.size() - 1);
        }
        return last;
    }
}
