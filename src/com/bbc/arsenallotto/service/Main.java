/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bbc.arsenallotto.service;

import com.bbc.arsenallotto.processor.LottoProcessor;
import com.bbc.arsenallotto.processor.WinningsProcessor;
import org.boris.winrun4j.AbstractService;
import org.boris.winrun4j.ServiceException;

/**
 *
 * @author Soul
 */
public class Main extends AbstractService {

    private boolean started = false;
    private LottoProcessor lottoProcessor = null;
    private WinningsProcessor winningsProcessor = null;

    /*public static void main(String[] args) {
     new LottoProcessor().start();
     new WinningsProcessor().start();
     }*/
    @Override
    public int serviceMain(String[] strings) throws ServiceException {

        while (!shutdown) {
            if (!started) {
                new Listener(3456).start();
                lottoProcessor = new LottoProcessor();
                lottoProcessor.start();
                winningsProcessor = new WinningsProcessor();
                winningsProcessor.start();
                started = true;
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
            }
        }

        lottoProcessor.shutDown();
        winningsProcessor.interrupt();
        winningsProcessor.shutDown();
        return 0;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
