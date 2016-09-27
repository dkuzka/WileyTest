/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dk.wileytest;

public interface Storage {

    void put(Object key, Object value);

    Object get(Object key);

    Object findExpired();

    Object removeExpired();
}
