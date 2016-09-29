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
public class AccessAttr {

    long timeAccessed;
    int count;
    Object key;

    AccessAttr(Object key) {
        //timeAccessed = System.currentTimeMillis();
        this.count = -1;
        this.key = key;
        inc();
    }

    final void inc() {
        count++;
        timeAccessed = System.currentTimeMillis();
    }
}
