/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bbc.arsenallotto.winjsonresponse;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

/**
 *
 * @author Soul
 */
public class FPrize {
    
    private Short iTier;
    private Short iPrizeCode;
    private Integer iWinners;
    private String fPrize;
    private String sTierName;

    /**
     * @return the iWinners
     */
    @XmlAttribute
    public Integer getiWinners() {
        return iWinners;
    }

    /**
     * @param iWinners the iWinners to set
     */
    public void setiWinners(Integer iWinners) {
        this.iWinners = iWinners;
    }

    /**
     * @return the iTier
     */
    @XmlAttribute
    public Short getiTier() {
        return iTier;
    }

    /**
     * @param iTier the iTier to set
     */
    public void setiTier(Short iTier) {
        this.iTier = iTier;
    }

    /**
     * @return the fPrize
     */
    @XmlValue
    public String getfPrize() {
        return fPrize;
    }

    /**
     * @param fPrize the fPrize to set
     */
    public void setfPrize(String fPrize) {
        this.fPrize = fPrize;
    }

    /**
     * @return the tierName
     */
    @XmlAttribute
    public String getsTierName() {
        return sTierName;
    }

    /**
     * @param tierName the tierName to set
     */
    public void setsTierName(String sTierName) {
        this.sTierName = sTierName;
    }

    /**
     * @return the iPrizeCode
     */
    @XmlAttribute
    public Short getiPrizeCode() {
        return iPrizeCode;
    }

    /**
     * @param iPrizeCode the iPrizeCode to set
     */
    public void setiPrizeCode(Short iPrizeCode) {
        this.iPrizeCode = iPrizeCode;
    }
}
