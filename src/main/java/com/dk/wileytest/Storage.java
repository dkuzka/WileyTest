/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dk.wileytest;

public interface Storage {

    /**
     *
     * @param key
     * @param value
     * @throws DuplicateKeyInCacheException
     */
    void put(Object key, Object value) throws DuplicateKeyInCacheException, Exception;

    /**
     *
     * @param key
     * @return
     * @throws KeyNotFoundInCacheException
     */
    Object get(Object key) throws KeyNotFoundInCacheException, Exception;

    Object findExpired();

    Object removeExpired();
    
    void setLiveTime(int ms);
}
