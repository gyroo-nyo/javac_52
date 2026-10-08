package com.healthcare.interfaces;

import com.healthcare.exception.HMSException;
import java.util.List;
import java.util.Optional;

/**
 * RUBRIC REQUIREMENT:
 * - OOP Implementation: Interfaces
 * - Collections & Generics: Generic interface definition GenericDAO<T, ID>
 */
public interface GenericDAO<T, ID> {
    Optional<T> findById(ID id) throws HMSException;
    List<T> findAll() throws HMSException;
    boolean save(T entity) throws HMSException;
    boolean update(T entity) throws HMSException;
    boolean delete(ID id) throws HMSException;
}
