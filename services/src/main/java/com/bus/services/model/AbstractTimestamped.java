package com.bus.services.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
public abstract class AbstractTimestamped<T> extends AbstractPersistent<T> {

    @Column(name = "inserted", updatable = false)
    private java.util.Date inserted = new Date();
    @Column(name = "updated")
    private Date updated = new Date();

    public AbstractTimestamped() {
        super();
    }

    public AbstractTimestamped(Long id) {
        super(id);
    }

    public Date getInserted() {
        return inserted;
    }

    public T setInserted(Date value) {
        this.inserted = value;
        return (T) this;
    }

    public Date getUpdated() {
        return updated;
    }

    public T setUpdated(Date value) {
        this.updated = value;
        return (T) this;
    }
}
