/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dk.wileytest;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dkuz
 */
public class TesterMain {

    public static void main(final String[] args) {
        System.out.println("Hello Wiley");
        System.out.println("Revision 2 (plus FileCache)");

        try {

            Cache c = new Cache();
            c.setLevel1Limit(2);
            c.setLevel2Limit(2);
            c.setLevel1LiveTime(30_000); // 30 seconds
            c.setLevel2LiveTime(3600_000); // 1 hour

            c.put("k1", "value1");
            c.get("k1"); // increase counter
            c.put("k2", "value2");
            c.put("k3", "value3");
            c.put("k4", "value4");
            c.put("k5", "value5"); // remove k2 

            System.out.println("==> " + c.get("k1")); 
            System.out.println("==> " + c.get("k2")); // test for exception

        } catch (Exception ex) {
            Logger.getLogger(TesterMain.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
