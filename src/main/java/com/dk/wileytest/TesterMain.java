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
public class TesterMain {

    public static void main(final String[] args) {
        System.out.println("Hello Wiley");
        System.out.println("Revision 2 (plus FileCache)");

        MemoryStorageImpl m = new MemoryStorageImpl(2);
        //m.setSize(0); // check range
        m.setNotifier((Object key, Object value) -> {
            System.out.println("DEBUG: remove from cache: key=" + key + " value=" + value);
        });
        try {
            m.put("k1", "value1");
            m.put("k2", "value2");
            m.put("k3", "value3");
            m.put("k2", "value dup"); // exception
        } catch (DuplicateKeyInCacheException ex) {
            System.err.println(ex);
        }

        try {
            System.out.println("==> " + m.get("k2"));
            System.out.println("==> " + m.get("k2"));
            System.out.println("==> " + m.get("k3"));
            System.out.println("==> " + m.get("k1"));
        } catch (KeyNotFoundInCacheException ex) {
            System.err.println(ex);
        }
    }

}
