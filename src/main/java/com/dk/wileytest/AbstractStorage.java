/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dk.wileytest;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 *
 * @author dkuz
 */
public class AbstractStorage implements Storage {

    private static final int MIN_SIZE = 1;
    private static final int MAX_SIZE = 1024 * 1024;

    ArrayList<AccessAttr> attr;
    int capacity;
    //private RemoveFromCacheEvent notifier;
    

    final void init(int size) {
        capacity = size;
        attr = new ArrayList<>(size);
    }

    public AbstractStorage() {
        init(1);
    }

    public AbstractStorage(int size) {
        init(size);
    }

    @Override
    public void put(Object key, Object value) throws DuplicateKeyInCacheException {
        if (attr.stream().filter(e -> e.key.equals(key)).count() > 0) {
            throw new DuplicateKeyInCacheException(key != null ? key.toString() : "null");
        }
        while (attr.size() >= capacity) {
            removeExpired();
        }
        attr.add(new AccessAttr(key));
        System.out.println("DEBUG: com.dk.wileytest.AbstractStorage.put(): key=" + key + " value=" + value);
    }

    @Override
    public Object get(Object key) throws KeyNotFoundInCacheException {
        System.out.println("DEBUG: com.dk.wileytest.AbstractStorage.get(): key=" + key);
        try {
            attr.stream().filter(e -> e.key.equals(key)).findFirst().get().inc();
        } catch (NoSuchElementException ex) {
            throw new KeyNotFoundInCacheException(key != null ? key.toString() : null);
        }
        return key; // stub
    }

    @Override
    public AccessAttr removeExpired() {
        AccessAttr a = findExpired();

        if (a != null) {
            // remove from counter attr
            attr.remove(a);
        }

        return a;
    }

    void setSize(int newSize) {
        if (newSize < MIN_SIZE || newSize > MAX_SIZE) {
            throw new IllegalArgumentException(String.format("Size %d not in range [%d .. %d]", newSize, MIN_SIZE, MAX_SIZE));
        }
        if (newSize > this.capacity) {
            this.capacity = newSize;
        } else {
            while (newSize < this.capacity) {
                removeExpired();
            }
        }
    }

    @Override
    public AccessAttr findExpired() {
        if (attr.size() < capacity) {
            return null;
        }
        // step 1: find element with min access count
        int minCount = attr.stream().mapToInt(e -> e.count).min().getAsInt();
        ArrayList pretendents = (ArrayList) attr.stream().filter(e -> e.count == minCount).collect(Collectors.toList());
        // if list contains one element, return it
        if (pretendents.size() == 1) {
            return (AccessAttr) pretendents.get(0);
        } else {
            // step 2: find from prepeared list oldest element
            long tm = pretendents.stream().mapToLong(e -> ((AccessAttr) e).timeAccessed).min().getAsLong();
            return (AccessAttr) pretendents.stream().filter(e -> ((AccessAttr) e).timeAccessed == tm).findFirst().get();
        }
    }

//    public void setEventNotifier(RemoveFromCacheEvent ev) {
//        notifier = ev;
//    }

}
