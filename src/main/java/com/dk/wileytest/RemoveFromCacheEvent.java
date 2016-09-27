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
public interface RemoveFromCacheEvent {

        /**
         *
         * @param key
         * @param value
         */
        public void fire(Object key, Object value);
    
}
