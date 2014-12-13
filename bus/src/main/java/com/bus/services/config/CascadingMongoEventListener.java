package com.bus.services.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mapping.model.MappingException;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Iterator;

public class CascadingMongoEventListener extends AbstractMongoEventListener {
    @Autowired
    private org.springframework.data.mongodb.core.MongoOperations mongoOperations;

    @Override
    public void onBeforeConvert(final Object source) {
        ReflectionUtils.doWithFields(source.getClass(), new ReflectionUtils.FieldCallback() {

            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                ReflectionUtils.makeAccessible(field);

                if (field.isAnnotationPresent(DBRef.class) && field.isAnnotationPresent(CascadeSave.class)) {
                    final Object fieldValue = field.get(source);

                    if(fieldValue == null)
                        return;

                    if(fieldValue instanceof Iterable){
                        Iterable obj = (Iterable)fieldValue;
                        Iterator it = obj.iterator();
                        while(it.hasNext()){
                            saveFiledToCollection(it.next());
                        }
                    }else{
                        saveFiledToCollection(fieldValue);
                    }
                }
            }
        });
    }

    private void saveFiledToCollection(final Object fieldValue){
        DbRefFieldCallback callback = new DbRefFieldCallback();
        ReflectionUtils.doWithFields(fieldValue.getClass(), callback);
        if (!callback.isIdFound()) {
            throw new MappingException("Cannot perform cascade save on child object without id set: " + fieldValue.getClass().getName());
        }
        mongoOperations.save(fieldValue);
    }

    private static class DbRefFieldCallback implements ReflectionUtils.FieldCallback {
        private boolean idFound;

        public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
            ReflectionUtils.makeAccessible(field);

            if (field.isAnnotationPresent(Id.class)) {
                idFound = true;
            }
        }

        public boolean isIdFound() {
            return idFound;
        }
    }
}
