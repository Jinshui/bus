package com.bus.services.util;

import com.mongodb.ServerAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.bson.BSONObject;
import org.bson.types.ObjectId;

/**
 * <p>MongoUtils class.</p>
 * @author <a href="mailto:matt@thistech.com">Matt Narrell</a>
 *         Created on: 1/3/13
 * @version $Id: $Id
 * @since 2.2.0
 */
public class MongoUtils {
    private MongoUtils() {}

    /**
     * Convenience method to parse a comma separated list of MongoDB servers (Replica Set notation) into a List of {@link com.mongodb.ServerAddress} objects.
     * @param addressString a {@link java.lang.String} object.  This must follow the <code>server1:port,server2:port</code> syntax.
     * @return a {@link java.util.List} of {@link com.mongodb.ServerAddress} objects.
     * @throws java.net.UnknownHostException if unable to parse the given addressString.
     */
    public static List<ServerAddress> getServerAddresses(String addressString) throws UnknownHostException {
        if (StringUtils.isBlank(addressString)) { throw new UnknownHostException("addressString cannot be empty"); }
        List<ServerAddress> addresses = new ArrayList<ServerAddress>();
        String[] hosts = addressString.split(",");
        for (String host : hosts) {
            ServerAddress address = new ServerAddress(host);
            addresses.add(address);
        }
        return addresses;
    }

    public static Collection<Object> toObjectIdsIfValid(Collection<String> values) {
        if (values == null) { return Collections.emptyList(); }
        List<Object> ids = new ArrayList<>(values.size());
        for (String value : values) {
            ids.add(toObjectIdIfValid(value));
        }
        return ids;
    }

    public static Object toObjectIdIfValid(String value) {
        return ObjectId.isValid(value) ? new ObjectId(value) : value;
    }

    @SuppressWarnings("unchecked")
    public static <T> T safeTraverse(final Object object, final String propertyName, final Class<T> expectedType) {
        final T value;
        if ((object == null) || (expectedType == null) || !(object instanceof BSONObject)) {
            value = (T)null;
        }
        else {
            final Object propertyValue = ((BSONObject)object).get(propertyName);
            if (propertyValue == null) {
                value = (T)null;
            }
            else {
                final Class actualType = propertyValue.getClass();
                if (!expectedType.isAssignableFrom(actualType)) {
                    value = (T)null;
                }
                else {
                    value = (T)propertyValue;
                }
            }
        }
        return value;
    }

    @SuppressWarnings("unchecked")
    public static <T> T safeTraverse(final Object object, final int index, final Class<T> expectedType) {
        final T value;
        if ((object == null) || (expectedType == null) || !(object instanceof List)) {
            value = (T)null;
        }
        else {
            final List<T> list = (List<T>)object;
            final Object element = list.get(index);
            if (element == null) {
                value = (T)null;
            }
            else {
                final Class actualType = element.getClass();
                if (!expectedType.isAssignableFrom(actualType)) {
                    value = (T)null;
                }
                else {
                    value = (T)element;
                }
            }
        }
        return value;
    }

}