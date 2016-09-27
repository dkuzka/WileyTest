/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dk.wileytest;

import java.util.HashMap;

public final class MemoryStorageImpl extends AbstractStorage {

    private RemoveFromCacheEvent notifier;
    private HashMap cache;

    private void setNotifier(RemoveFromCacheEvent ev) {
        notifier = ev;
    }

    @Override
    void init(int size) {
        super.init(size);
        cache = new HashMap<>(size);
    }

//    public MemoryStorageImpl() {
//        init(1);
//    }
    public MemoryStorageImpl(int size) {
        init(size);
    }

    @Override
    public void put(Object key, Object value) {
        super.put(key, value);
        cache.put(key, value);
    }

    @Override
    public Object get(Object key) {
        ((AccessAttr) keysAttr.get(key)).inc();
        return cache.get(key);
    }

    @Override
    public Object removeExpired() {
        Object key = super.removeExpired();

        if (key != null) {
            Object value = cache.get(key);

            // remove
            cache.remove(key);

            // notify
            if (notifier != null) {
                notifier.fire(key, value);
            }
        }
        
        return key;
    }

}
