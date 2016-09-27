/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dk.wileytest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 *
 * @author dkuz
 */
public class AbstractStorage implements Storage {

    private static final int MIN_SIZE = 1;
    private static final int MAX_SIZE = 1024 * 1024;

    HashMap<Object, AccessAttr> keysAttr;
    int capacity;

    void init(int size) {
        keysAttr = new HashMap<>(size);
    }

    @Override
    public void put(Object key, Object value) {
        while (keysAttr.size() >= capacity) {
            removeExpired();
        }
        keysAttr.put(key, new AccessAttr());
        capacity++;
    }

    @Override
    public Object get(Object key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object removeExpired() {
        Object key = findExpired();

        if (key != null) {
            // remove from counter attr
            keysAttr.remove(key);
        }
        
        return key;
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
    public Object findExpired() {
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
