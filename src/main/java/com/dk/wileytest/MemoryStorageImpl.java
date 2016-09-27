/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dk.wileytest;

import java.util.HashMap;

public final class MemoryStorageImpl extends AbstractStorage {

    private RemoveFromCacheEvent notifier;
    private final HashMap cache;

    public void setNotifier(RemoveFromCacheEvent ev) {
        notifier = ev;
    }

    public MemoryStorageImpl(int size) {
        init(size);
        cache = new HashMap<>(size);
    }

    @Override
    public void put(Object key, Object value) throws DuplicateKeyInCacheException {
        super.put(key, value);
        cache.put(key, value);
    }

    @Override
    public Object get(Object key) throws KeyNotFoundInCacheException {
        super.get(key);
        return cache.get(key);
    }

    @Override
    public AccessAttr removeExpired() {
        AccessAttr a = super.removeExpired();

        if (a != null) {
            Object key = a.key;

            // save for notify
            Object value = cache.get(key);

            // remove
            cache.remove(key);

            // notify
            if (notifier != null) {
                notifier.fire(key, value);
            }
        }

        return a;
    }

}
