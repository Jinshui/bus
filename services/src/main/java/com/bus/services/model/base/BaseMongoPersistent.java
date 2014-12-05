package com.bus.services.model.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Date;

public class BaseMongoPersistent<T extends BaseMongoPersistent> implements MongoPersistent<T> {

    /** A unique identifier for this object */
    @Id
    private String id;
    /** The timestamp when this object was created */
    private Date createdOn = new Date();
    /** The timestamp when this object was last updated */
    @Indexed //This is often used as the default sort field by the WebUI
    private Date lastUpdatedOn = new Date();
    @Transient
    @JsonIgnore
    private String lastUpdateUser;

    public BaseMongoPersistent() {}

    public BaseMongoPersistent(String id) {
        this.id = id;
    }

    public String getId() { return id; }
    public T setId(String value) { this.id = value; return (T) this; }

    public Date getCreatedOn() { return createdOn; }
    public T setCreatedOn(Date value) { this.createdOn = value; return (T) this; }

    public Date getLastUpdatedOn() { return lastUpdatedOn; }
    public T setLastUpdatedOn(Date value) { this.lastUpdatedOn = value; return (T) this; }
    @Override
    public void onBeforeSave() { /* do nothing; */}

    public String getLastUpdateUser() {
        return lastUpdateUser;
    }
    public T setLastUpdateUser(String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
        return (T) this;
    }
}