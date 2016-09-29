/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dk.wileytest;

// Under construnction
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public final class FileStorageImpl extends AbstractStorage {

//    private RemoveFromCacheEvent notifier;
//    public void setNotifier(RemoveFromCacheEvent ev) {
//        notifier = ev;
//    }
    public FileStorageImpl(int size) {
        init(size);
    }

    @Override
    public void put(Object key, Object value) throws DuplicateKeyInCacheException, Exception {
        if (!(value instanceof Serializable)) {
            throw new IllegalArgumentException(value.getClass() + " is not serializable");
        }
        super.put(key, value);

        // do serialization
        try (FileOutputStream fos = new FileOutputStream(getFileName(key)); ObjectOutputStream out = new ObjectOutputStream(fos);) {
            out.writeObject(value);
        }
    }

    @Override
    public Object get(Object key) throws KeyNotFoundInCacheException, Exception {
        super.get(key);
        // do deserialization
        Object value;
        try (FileInputStream fis = new FileInputStream(getFileName(key)); ObjectInputStream in = new ObjectInputStream(fis);) {
            value = in.readObject();
        }
        return value;
    }

    @Override
    public AccessAttr removeExpired() {
        AccessAttr a = super.removeExpired();

        if (a != null) {
            Object key = a.key;

//            // save for notify
//            Object value = cache.get(key);

            // remove
            File f = new File(getFileName(key));
            f.delete();
            
//            // notify
//            if (notifier != null) {
//                notifier.fire(key, value);
//            }
        }

        return a;
    }

    private String getFileName(Object key) {
        return key.getClass().getSimpleName() + key.hashCode() + ".ser";
    }

}
