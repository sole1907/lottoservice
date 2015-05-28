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
public class ANumbers {
    
    private List<Integer> number;
    private Integer bonus;

    /**
     * @return the number
     */
    public List<Integer> getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(List<Integer> number) {
        this.number = number;
    }

    /**
     * @return the bonus
     */
    public Integer getBonus() {
        return bonus;
    }

    /**
     * @param bonus the bonus to set
     */
    public void setBonus(Integer bonus) {
        this.bonus = bonus;
    }
    
}
