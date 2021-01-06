package com.foxminded.school.dao;

import java.util.List;

public interface DAO<T, ID> {

    void create(T t);

    List<T> readAll();

    T readByID(ID id);

    void update(T t);

    void delete(ID id);
}
