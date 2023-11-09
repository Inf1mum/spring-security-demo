package com.example.demospringsecurity.repository;

import com.example.demospringsecurity.entity.Identifiable;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class BaseRepository<T extends Identifiable> {
    protected final MongoTemplate mongoTemplate;
    protected final Class<T> type;

    public T findById(String id) {
        Query query = Query.query(Criteria.where("_id").is(id));
        return mongoTemplate.findOne(query, type);
    }

    public T save(T entity) {
        mongoTemplate.insert(entity);
        return findById(entity.getId());
    }

    public T update(T transientEntity) {
        mongoTemplate.updateFirst(
            Query.query(Criteria.where("_id").is(transientEntity.getId())), getUpdate(transientEntity), type
        );
        return findById(transientEntity.getId());
    }

    public List<T> findByIds(List<T> entities) {
        List<String> ids = entities.stream().map(T::getId).toList();
        Query query = Query.query(Criteria.where("_id").in(ids));
        return mongoTemplate.find(query, type);
    }

    private Update getUpdate(T transientEntity) {
        Document document = new Document();
        mongoTemplate.getConverter().write(transientEntity, document);
        Update update = new Update();
        for (String field : document.keySet()) {
            update.set(field, document.get(field));
        }
        unsetNullAttributes(transientEntity, update);
        return update;
    }

    private void unsetNullAttributes(T entity, Update update) {
        for (Field field : getAllFields(new ArrayList<>(), entity.getClass())) {
            try {
                field.setAccessible(true);
                if (field.get(entity) == null && !field.isAnnotationPresent(Transient.class))
                    update.unset(field.getName());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private List<Field> getAllFields(List<Field> fields, Class<?> type) {
        fields.addAll(Arrays.asList(type.getDeclaredFields()));
        if (type.getSuperclass() != null) {
            getAllFields(fields, type.getSuperclass());
        }
        return fields;
    }
}
