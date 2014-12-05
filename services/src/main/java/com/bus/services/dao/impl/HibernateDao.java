package com.bus.services.dao.impl;


import com.bus.services.dao.IDao;
import com.bus.services.model.AbstractPersistent;
import com.bus.services.model.AbstractTimestamped;
import org.codehaus.plexus.util.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Date;

@SuppressWarnings("unchecked")
@Transactional
public abstract class HibernateDao<E extends AbstractTimestamped, K extends Serializable> implements IDao<E, K> {
    private static final Logger log = LoggerFactory.getLogger(HibernateDao.class);

    @Resource
    private SessionFactory factory;
    @Resource
    private DataSource dataSource;

    private Class clazz;

    public HibernateDao() {
        try {
            Type genericSuperclass = getClass().getGenericSuperclass();
            String[] strings = genericSuperclass.toString().split("[<,>]");
            if (strings.length >= 2) {
                clazz = Class.forName(strings[1]);
            }
        }
        catch (ClassNotFoundException e) {
            log.error("Failed to initialize persistence module", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    public void delete(E o) {
        getSession().delete(o);
    }

    public E get(K id) {
        return (E) getSession().get(clazz, id);
    }

    public void update(E o) {
        o.setUpdated(new Date());
        getSession().update(o);
    }

    public void save(E o) {
        o.setUpdated(new Date());
        if (o instanceof AbstractTimestamped) {
            // dont muck with this unless you have good reason to.
            AbstractPersistent ap = (AbstractTimestamped) o;
            if (ap.getId() == 0) {
                getSession().persist(o);
            }
            else {
                getSession().merge(o);
            }
        }
        else {
            getSession().save(o);
        }
    }

    protected Criteria createCriteria() {
        return getSession().createCriteria(this.clazz);
    }

    protected Criteria createCriteria(Class clazz) {
        return getSession().createCriteria(clazz);
    }

    protected JdbcTemplate createJdbcTemplate() {
        return new JdbcTemplate(dataSource);
    }

    protected int getNumericResult(Criteria criteria) {
        return ((Number) criteria.uniqueResult()).intValue();
    }

    protected Session getSession() {
        return factory.getCurrentSession();
    }

    protected int rowCount(Criteria criteria) {
        return rowCount(criteria, null);
    }

    protected int rowCount(Criteria criteria, String distinctProperty) {
        if (StringUtils.isNotBlank(distinctProperty)) {
            criteria.setProjection(Projections.countDistinct(distinctProperty));
        }
        else {
            criteria.setProjection(Projections.rowCount());
        }
        return ((Number) criteria.uniqueResult()).intValue();
    }

    // convenience operations for adding restrictions to a criteria

    protected void eq(String alias, Object value, Criteria criteria) {
        if (value != null) {
            criteria.add(Restrictions.eq(alias, value));
        }
    }

    protected void ge(String alias, Number value, Criteria criteria) {
        if (value != null) {
            criteria.add(Restrictions.ge(alias, value));
        }
    }

    protected void le(String alias, Number value, Criteria criteria) {
        if (value != null) {
            criteria.add(Restrictions.le(alias, value));
        }
    }

    protected void like(String alias, String value, MatchMode matchMode, Criteria criteria) {
        like(new String[] {alias}, value, matchMode, criteria);
    }

    protected void like(String[] aliases, String value, MatchMode matchMode, Criteria criteria) {
        if (value == null || aliases == null) { return; }
        if (aliases.length == 1) {
            criteria.add(Restrictions.ilike(aliases[0], value, matchMode));
        }
        else {
            Disjunction disjunction = Restrictions.disjunction();
            for (String alias : aliases) {
                disjunction.add(Restrictions.ilike(alias, value, matchMode));
            }
            criteria.add(disjunction);
        }
    }

    protected void ne(String alias, Object value, Criteria criteria) {
        if (value != null) {
            criteria.add(Restrictions.ne(alias, value));
        }
    }
}

