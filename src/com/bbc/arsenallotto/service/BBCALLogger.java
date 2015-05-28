/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bbc.arsenallotto.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author Soul
 */
public class BBCALLogger {

    private static Logger logger = null;
    private static String logtime = null;
    private static FileHandler lastFh = null;

    public static Logger getLogger() {
        if (logger == null || !logtime.equals(new SimpleDateFormat("yyyy-MM-dd-HH").format(new Date()))) {
            if (lastFh != null) {
                logger.removeHandler(lastFh);
            }
            logtime = new SimpleDateFormat("yyyy-MM-dd-HH").format(new Date());
            logger = Logger.getLogger("BBCArsenalLotto Service");
            FileHandler fh;

            try {

                // This block configure the logger with handler and formatter  
                fh = new FileHandler("logs/bbcal-" + logtime + ".log");
                lastFh = fh;
                logger.addHandler(fh);
                SimpleFormatter formatter = new SimpleFormatter();
                fh.setFormatter(formatter);

                // the following statement is used to log any messages  
                //logger.info("My first log");
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return logger;
    }
}
