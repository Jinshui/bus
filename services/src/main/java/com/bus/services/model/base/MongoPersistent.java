package com.bus.services.model.base;

import java.io.Serializable;
import java.util.Date;

/**
 * MongoPersistent interface
 * @author <a href="mailto:matt@thistech.com">Matt Narrell</a>
 * @since 1.2.8
 */
public interface MongoPersistent<T extends MongoPersistent> extends Serializable {

    /**
     * Get the unique identity for this entity
     * @return The unique identity for thie entity
     */
    String getId();

    /**
     * Set the creation date for this object
     * @param date The creation date.
     * @return This object (for chaining)
     */
    T setCreatedOn(Date date);

    /**
     * Get the date this object was created.
     * @return The date this object was created
     */
    Date getCreatedOn();

    /**
     * Set the date this object was last updated.
     * @param date The date.
     * @return This object (for chaining).
     */
    T setLastUpdatedOn(Date date);

    /**
     * Get the date this object was last updated.
     * @return The date this object was last updated.
     */
    Date getLastUpdatedOn();

    /**
     * get the last update user
     */
    String getLastUpdateUser();

    /**
     * set the last update user
     */
    T setLastUpdateUser(String user);



    /**
     * Perform any housekeeping on this object before it is saved to the data store
     */
    void onBeforeSave();
}

