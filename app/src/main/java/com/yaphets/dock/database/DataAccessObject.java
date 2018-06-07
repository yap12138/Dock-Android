package com.yaphets.dock.database;

import java.util.List;

public interface DataAccessObject<T> {
    List<T> findAll();
    List<T> findByProperty(String where, Object...values);
    T findById(int...ids);
}
