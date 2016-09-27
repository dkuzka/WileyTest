/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dk.wileytest;

/**
 *
 * @author dkuz
 */
public class SimpleCache {

    private final int MIN_SIZE = 1;
    private final int MAX_SIZE = 1024 * 1024;
    private int size;

    public SimpleCache(int size) {
        this.setSize(size);
    }

    public final void setSize(int newSize) {
        if (newSize < MIN_SIZE || newSize > MAX_SIZE) {
            throw new IllegalArgumentException(String.format("Size %d not in range [%d .. %d]", newSize, MIN_SIZE, MAX_SIZE));
        }
        if (newSize > this.size) {
            this.size = newSize;
        } else {
            while (newSize < this.size) {
                removeTheWorst();
            }
            //this.size = size;
        }
    }

    private void removeTheWorst() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        //this.size--;
    }

    public interface PushOutEvent {

        public void fire(Object key, Object value);
    }

    public void put(Object key, Object value) {
    }

    public Object get(Object key) {
        return null;
    }

}
