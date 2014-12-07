package com.bus.services.repositories.base;

import com.bus.services.model.base.MongoPersistent;

import java.util.List;

public interface MongoRepository<T extends MongoPersistent> {

    void insertBatch(List<T> batch);

    void delete(String id);

    void delete(List<T> deletes);

    void deleteAll();

    boolean exists(String id);

    T findOne(String id);

    List<T> findAll();

    List<T> findAll(List<String> ids);

    T save(T entity);

    List<T> save(List<T> entities);

    long count();

    void delete(T entity);
}
