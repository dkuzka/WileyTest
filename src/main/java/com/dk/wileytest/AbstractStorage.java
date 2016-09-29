/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dk.wileytest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private int liveTime = 3600000;

    final void init(int size) {
        //capacity = size;
        attr = new ArrayList<>(size);
        setSize(size);
    }

    public AbstractStorage() {
        init(1);
    }

    public AbstractStorage(int size) {
        init(size);
    }

    @Override
    public void put(Object key, Object value) throws DuplicateKeyInCacheException, Exception {
        if (attr.stream().filter(e -> e.key.equals(key)).count() > 0) {
            throw new DuplicateKeyInCacheException(key != null ? key.toString() : "null");
        }
        while (attr.size() >= capacity) {
            removeExpired();
        }
        attr.add(new AccessAttr(key));
        System.out.println("DEBUG: " + this + ".put(): key=" + key + " value=" + value);
    }

    @Override
    public Object get(Object key) throws KeyNotFoundInCacheException, Exception {
        System.out.println("DEBUG: " + this + ".get(): key=" + key);
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
            System.out.println("DEBUG: " + this + ".removeExpired(): key=" + a.key);
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

        long now = System.currentTimeMillis();
        Optional<AccessAttr> op;

        // step 1: find time expired elements
        op = attr.stream().filter(e -> (now >= e.timeAccessed + liveTime)).findFirst();
        if (op.isPresent()) {
            return op.get();
        }

        // step 2: find element with min access count and time
        int minCount = attr.stream().mapToInt(e -> e.count).min().getAsInt();
        op = attr.stream().filter(e -> e.count == minCount).sorted((e1, e2) -> Long.compare(e1.timeAccessed, e2.timeAccessed)).findFirst();
        return op.get();
    }

//    public void setEventNotifier(RemoveFromCacheEvent ev) {
//        notifier = ev;
//    }
    @Override
    public void setLiveTime(int ms) {
        this.liveTime = ms;
    }

}
