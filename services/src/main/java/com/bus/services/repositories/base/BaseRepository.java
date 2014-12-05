package com.bus.services.repositories.base;

import com.bus.services.model.base.MongoPersistent;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;

import javax.annotation.Resource;
import javax.validation.Validator;

public abstract class BaseRepository<T extends MongoPersistent> extends AbstractMongoRepository<T> {

    @Resource
    private MongoTemplate mongoTemplate;

    protected Criteria createCriteria() {
        return new Criteria();
    }

    public BaseRepository(Class<T> clazz) {
        super(clazz);
    }

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    public Validator getValidator() {
        return null;
    }
}
