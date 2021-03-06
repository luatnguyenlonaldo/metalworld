/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metalworld.dao;

import java.util.List;

/**
 *
 * @author Lonaldo
 * @param <T>
 * @param <PK>
 */
public interface IGenericDAO<T, PK> {
    T create(T t);
    T findById(PK id);
    T update(T t);
    boolean delete(T t);
    List<T> getAll(String namedQuery);
}
