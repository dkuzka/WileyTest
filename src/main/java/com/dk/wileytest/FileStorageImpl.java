/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dk.wileytest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public final class FileStorageImpl implements Storage {

    private final int MIN_SIZE = 1;
    private final int MAX_SIZE = 1024 * 1024;

    private HashMap cache;
    private HashMap<Object, AccessAttr> keysAttr;
    private int capacity;

    private void init(int size) {
        cache = new HashMap<>(size);
        keysAttr = new HashMap<>(size);
    }

//    public MemoryStorageImpl() {
//        init(1);
//    }

    public FileStorageImpl(int size) {
        init(size);
    }

    public void setSize(int newSize) {
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
    public void put(Object key, Object value) {
        while (cache.size() >= capacity) {
            removeExpired();
        }
        cache.put(key, value);
        keysAttr.put(key, new AccessAttr());
        capacity++;
    }

    @Override
    public Object get(Object key) {
        ((AccessAttr) keysAttr.get(key)).inc();
        return cache.get(key);
    }

    @Override
    public void removeExpired() {
        Object key = findExpired();

        if (key == null) {
            return;
        }

        // remove
        cache.remove(key);

        // remove from counter
        keysAttr.remove(key);
    }

    private Object findExpired() {
        if (keysAttr.size() < capacity) {
            return null;
        }
        // step 1: find element with min access count
        int minCount = keysAttr.values().stream().mapToInt(e -> ((AccessAttr) e).count).min().getAsInt();
        ArrayList pretendent = (ArrayList) keysAttr.values().stream().filter(e -> ((AccessAttr) e).count == minCount).collect(Collectors.toList());
        // if list contains one element, return it
        if (pretendent.size() == 1) {
            return pretendent.get(0);
        } else {
            // step 2: find from prepeared list oldest element
            long tm = pretendent.stream().mapToLong(e -> ((AccessAttr) e).timeAccessed).min().getAsLong();
            return pretendent.stream().filter(e -> ((AccessAttr) e).timeAccessed == tm).findFirst();
        }
    }

}
