/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bbc.arsenallotto.winjsonresponse;

import java.util.List;

/**
 *
 * @author Soul
 */
public class APrizes {
  
    private List<FPrize> fPrize;
    private Double fPrizePool;

    /**
     * @return the fPrize
     */
    public List<FPrize> getfPrize() {
        return fPrize;
    }

    /**
     * @param fPrize the fPrize to set
     */
    public void setfPrize(List<FPrize> fPrize) {
        this.fPrize = fPrize;
    }

    /**
     * @return the fPrizePool
     */
    public Double getfPrizePool() {
        return fPrizePool;
    }

    /**
     * @param fPrizePool the fPrizePool to set
     */
    public void setfPrizePool(Double fPrizePool) {
        this.fPrizePool = fPrizePool;
    }
    
}
