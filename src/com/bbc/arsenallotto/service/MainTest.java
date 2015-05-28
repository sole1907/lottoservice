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
public class MainTest {

    public static void main(String[] args) {
        new LottoProcessor().start();
        new WinningsProcessor().start();
    }

}
