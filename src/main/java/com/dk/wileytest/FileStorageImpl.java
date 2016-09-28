/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dk.wileytest;

// Under construnction

public final class FileStorageImpl extends AbstractStorage {

//    private RemoveFromCacheEvent notifier;

//    public void setNotifier(RemoveFromCacheEvent ev) {
//        notifier = ev;
//    }

    public FileStorageImpl(int size) {
        init(size);
    }

    @Override
    public void put(Object key, Object value) throws DuplicateKeyInCacheException {
        super.put(key, value);
        // do serialization
    }

    @Override
    public Object get(Object key) throws KeyNotFoundInCacheException {
        super.get(key);
        // do deserialization
        return null;
    }

    @Override
    public AccessAttr removeExpired() {
        AccessAttr a = super.removeExpired();

        if (a != null) {
            Object key = a.key;

//            // save for notify
//            Object value = cache.get(key);

            // remove
            // do: remove file

//            // notify
//            if (notifier != null) {
//                notifier.fire(key, value);
//            }
        }

        return a;
    }

}
