package com.bus.services.model;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
public abstract class AbstractPersistent<T> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    protected long id;

    public AbstractPersistent() {
    }

    public AbstractPersistent(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public T setId(long id) {
        this.id = id;
        return (T) this;
    }
}
