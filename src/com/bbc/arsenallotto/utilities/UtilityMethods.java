/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bbc.arsenallotto.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

/**
 *
 * @author Soul
 */
public class UtilityMethods {

    public static HashMap<String, String> getConfigs() {
        HashMap<String, String> configs = new HashMap<>();

        Properties prop = new Properties();
        InputStream input = null;

        try {

            input = new FileInputStream("cfg/cfg.properties");

            // load a properties file
            prop.load(input);

            for (Object key : prop.keySet()) {
                configs.put((String) key, prop.getProperty((String) key));
            }
            // get the property value and print it out
            //System.out.println(prop.getProperty("database"));
            //System.out.println(prop.getProperty("dbuser"));
            //System.out.println(prop.getProperty("dbpassword"));

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return configs;
    }
    
}
