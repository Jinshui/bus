package com.bus.services.repositories;

import com.bus.services.exceptions.ApplicationException;
import com.bus.services.model.MongoPersistent;
import com.bus.services.util.BooleanUtil;
import com.bus.services.util.CollectionUtil;
import com.bus.services.util.MongoUtils;
import com.bus.services.util.Page;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.IndexOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.*;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

public abstract class AbstractMongoRepository<T extends MongoPersistent> implements MongoRepository<T> {
    private static final Logger log = LoggerFactory.getLogger(AbstractMongoRepository.class);
    private final Class<T> clazz;

    /**
     * <p>Constructor for AbstractMongoRepository.</p>
     * @param clazz a {@link java.lang.Class} object.
     */
    public AbstractMongoRepository(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long count() {
        return getMongoTemplate().findAll(clazz).size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(T entity) {
        if (entity != null) {
            getMongoTemplate().remove(entity);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(String id) {
        T entity = findOne(id);
        delete(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(List<T> deletes) {
        for (T t : deletes) {
            delete(t);
        }
    }

    @Override
    public void deleteAll() {
        getMongoTemplate().remove(query(where("_id").exists(true)), clazz);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean exists(String id) {
        return findOne(id) != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T findOne(String id) {
        T found = getMongoTemplate().findById(MongoUtils.toObjectIdIfValid(id), clazz);
        if (found == null) {
            log.warn(new ApplicationException("Found no entity for " + clazz.getSimpleName() + " with ID: " + id).toString());
        }
        return found;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<T> findAll() {
        return this.getMongoTemplate().findAll(clazz);
    }

    @Override
    public List<T> findAll(List<String> ids) {
        List<T> found = new ArrayList<>();
        for (String id : ids) {
            T one = this.findOne(id);
            if (one != null) {
                found.add(one);
            }
        }
        return found;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T save(T entity) throws ConstraintViolationException, DuplicateKeyException {
        entity.onBeforeSave();
        Set<ConstraintViolation<?>> constraintViolations = new HashSet<>();
        if (getValidator() != null) { constraintViolations.addAll(getValidator().validate(entity)); }

        if (CollectionUtil.isEmpty(constraintViolations)) {
            entity.setLastUpdatedOn(new Date());
            this.getMongoTemplate().save(entity);
        }
        else {
            StringBuilder errorMsgBuilder = new StringBuilder();
            for (ConstraintViolation<?> constraintViolation : constraintViolations) {
                errorMsgBuilder.append("Constraint Violation: ").append(constraintViolation.getPropertyPath()).append(" ").append(constraintViolation.getMessage()) .append(".");
            }
            log.error(String.format("%s NOT saved due to errors below\n\t%s", entity.getClass().getSimpleName(), errorMsgBuilder.toString()));
            throw new ConstraintViolationException(constraintViolations);
        }
        return entity;
    }

    @Override
    public List<T> save(List<T> entities) {
        List<T> saved = new ArrayList<>();
        for (T t : entities) {
            saved.add(save(t));
        }
        return saved;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertBatch(List<T> batch) {
        getMongoTemplate().insert(batch, clazz);
    }

    // internals

    /**
     * Find a page worth of items.
     * @param criteria The base criteria
     * @param sortProperty The sort property
     * @param isDesc If true and sort property is not null, sort descending
     * @param pageIndex The page index
     * @param pageSize The page size. A page size of 0 will fetch all results
     * @return The page of results
     */
    protected Page<T> findPage(Criteria criteria, String sortProperty, Boolean isDesc, int pageIndex, int pageSize) {
        String[] sortProperties = StringUtils.isNotBlank(sortProperty) ? new String[] { sortProperty } : null;
        return findPage(criteria, sortProperties, isDesc, pageIndex, pageSize, null);
    }

    /**
     * Find a page worth of items.
     * @param criteria The base criteria
     * @param sortProperties The sort properties
     * @param isDesc If true and sort property is not null, sort descending
     * @param pageIndex The page index
     * @param pageSize The page size. A page size of 0 will fetch all results
     * @return The page of results
     */
    protected Page<T> findPage(Criteria criteria, String[] sortProperties, Boolean isDesc, int pageIndex, int pageSize) {
        return findPage(criteria, sortProperties, isDesc, pageIndex, pageSize, null);
    }

    /**
     * Find a page worth of items with limited fields specified by the <code>fields</code>. This method
     * requires a <code>totalCount</code> for the criteria so that it doesn't need count the items if
     * the <code>totalCount</code> >= 0.
     * @param criteria The base criteria
     * @param sortProperties The sort property
     * @param isDesc If true and sort property is not null, sort descending
     * @param pageIndex The page index
     * @param pageSize The page size. If 0, all results are returned
     * @param fields The fields to be returned， passing null or empty will return all fields.
     * The fields must be in the exact json used in Mongo query projection,
     * @return The page of results
     */
    @SuppressWarnings("unchecked")
    protected Page<T> findPage(Criteria criteria, String[] sortProperties, Boolean isDesc, int pageIndex, int pageSize, String fields) {
        Query query = query(criteria);
        addReturnFieldsToQuery(fields, query);

        Sort sort = null;
        if (sortProperties != null && sortProperties.length > 0) {
            sort = new Sort(BooleanUtil.isTrue(isDesc) ? Sort.Direction.DESC : Sort.Direction.ASC, sortProperties);
        }

        // if fetching everything, dont execute a secondary "count" query
        if (pageSize <= 0) {
            if (sort != null) {
                query = query.with(sort);
            }
            return new Page<T>(getMongoTemplate().find(query, getClazz()));
        }
        else {
            Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
            return new Page<T>(getMongoTemplate().find(query.with(pageable), getClazz()), pageable, getMongoTemplate().count(query, getClazz()));
        }
    }

    //Limit the return fields for a query to improve the query performance
    private void addReturnFieldsToQuery(String fieldsInJson, Query query) {
        if (StringUtils.isBlank(fieldsInJson)) { return; }
        BasicDBObject fieldsObj = (BasicDBObject) JSON.parse(fieldsInJson);
        Set<String> fieldKeys = fieldsObj.keySet();
        for (String fieldKey : fieldKeys) {
            Object fieldValue = fieldsObj.get(fieldKey);
            if (fieldValue instanceof BasicDBObject) {
                BasicDBObject fieldObject = (BasicDBObject) fieldValue;
                if (fieldObject.containsField("$elemMatch")) {
                    Object elementObject = fieldObject.get("$elemMatch");
                    if (elementObject instanceof BasicDBObject) {
                        BasicDBObject arrayElement = (BasicDBObject) elementObject;
                        Criteria elemMatchCriteria = new Criteria();
                        for (String elementKey : arrayElement.keySet()) {
                            elemMatchCriteria.and(elementKey).is(arrayElement.get(elementKey));
                        }
                        query.fields().elemMatch(fieldKey, elemMatchCriteria);
                    }
                }
            }
            else if (fieldValue instanceof Integer) {
                query.fields().include(fieldKey);
            }
        }
    }

    /**
     * <p>Getter for the field <code>mongoTemplate</code>.</p>
     * @return a {@link org.springframework.data.mongodb.core.MongoTemplate} object.
     * @since 1.3.0
     */
    protected abstract MongoTemplate getMongoTemplate();

    protected abstract Validator getValidator();

    protected Class<T> getClazz() { return clazz; }

    protected void setRange(Query query, Integer startIndex, Integer count) {
        if (startIndex != null) {
            query.skip(startIndex);
        }
        if (count != null) {
            query.limit(count);
        }
    }

    protected void setSort(Query query, Boolean desc, String sortString, String... defaultsSorts) {
        String[] sorts;
        if (StringUtils.isBlank(sortString)) {
            sorts = defaultsSorts.clone();
        }
        else {
            sorts = new String[defaultsSorts.length + 1];
            sorts[0] = sortString;
            System.arraycopy(defaultsSorts, 0, sorts, 1, defaultsSorts.length);
        }
        query.with(new Sort(BooleanUtil.isTrue(desc) ? Sort.Direction.DESC : Sort.Direction.ASC, sorts));
    }

    protected String toRegex(String attribute) {
        return StringUtils.replace(attribute, ".", "\\.") + ".*";
    }

    protected void ensureIndexes(Index... indexes) {
        this.ensureIndexes(getClazz(), indexes);
    }

    protected void ensureIndexes(Class clazz, Index ... indexes) {
        this.ensureIndexes(getMongoTemplate().indexOps(clazz), indexes);
    }

    protected void ensureIndexes(String collection, Index ... indexes) {
        this.ensureIndexes(getMongoTemplate().indexOps(collection), indexes);
    }

    private void ensureIndexes(IndexOperations indexOps, Index ... indexes) {
        for (Index index : indexes) {
            try {
                indexOps.ensureIndex(index);
            }
            catch (Exception e) {
                // something happened creating the index... re-save the models and attempt to recreate the index
                for (T obj : findAll()) {
                    save(obj);
                }

                // if this index was named, then its probably a keyspec change... try to drop the previous index
                String name = getIndexName(index);
                if (StringUtils.isNotBlank(name)) {
                    try {
                        indexOps.dropIndex(name);
                    }
                    catch (Exception e2) { }
                }

                // attempt to recreate the index
                indexOps.ensureIndex(index);
            }
        }
    }

    private String getIndexName(Index index) {
        DBObject dbo = index.getIndexOptions();
        return (String) dbo.get("name");
    }
}
