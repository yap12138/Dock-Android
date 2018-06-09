package com.yaphets.dock.database;

public interface DataManipulationObject<T> {
    boolean insert(T Transient);
    boolean delete(T persistent);
    boolean update(T persistent);
    int updateAll(String where, Object...values);
}
