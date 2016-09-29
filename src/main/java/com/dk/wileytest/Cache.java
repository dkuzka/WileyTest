/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dk.wileytest;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dkuz
 */
public class Cache {

    private final MemoryStorageImpl cacheLevel1;
    private final FileStorageImpl cacheLevel2;

    public Cache() throws DuplicateKeyInCacheException, Exception {
        cacheLevel1 = new MemoryStorageImpl(1);
        cacheLevel2 = new FileStorageImpl(1);
        cacheLevel1.setNotifier((Object key, Object value) -> {
            try {
                cacheLevel2.put(key, value);
            } catch (Exception ex) {
                Logger.getLogger(Cache.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

    }

    public void setLevel1Limit(int limit) {
        cacheLevel1.setSize(limit);
    }

    public void setLevel2Limit(int limit) {
        cacheLevel2.setSize(limit);
    }

    public void put(Object key, Object value) throws DuplicateKeyInCacheException, Exception {
        cacheLevel1.put(key, value);
    }

    public Object get(Object key) throws KeyNotFoundInCacheException, Exception {
        Object value;
        try {
            value = cacheLevel1.get(key);
        } catch (KeyNotFoundInCacheException ex) {
            value = cacheLevel2.get(key);
        }
        return value;
    }

    public void setLevel1LiveTime(int ms) {
        cacheLevel1.setLiveTime(ms);
    }

    public void setLevel2LiveTime(int ms) {
        cacheLevel2.setLiveTime(ms);
    }

}
