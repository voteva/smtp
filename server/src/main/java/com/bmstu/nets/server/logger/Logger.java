/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bmstu.nets.server.logger;

/**
 *
 * @author patutinaam
 */
public class Logger {
 
    public Logger() {
        
    }
         
    public boolean info(String msg) {
        System.out.println(msg);
        return true;
    }
    
    public boolean error(String msg) {
        System.err.println(msg);
        return true;
    }
}
