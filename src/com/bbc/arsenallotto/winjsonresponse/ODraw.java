/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bbc.arsenallotto.winjsonresponse;

/**
 *
 * @author Soul
 */
public class ODraw {
    
    private String iGameID;
    private String iLotteryID;
    private String sLotteryName;
    private String sLotteryDate;
    private ANumbers aNumbers;
    private APrizes aPrizes;
    private String winners;

    /**
     * @return the iGameID
     */
    public String getiGameID() {
        return iGameID;
    }

    /**
     * @param iGameID the iGameID to set
     */
    public void setiGameID(String iGameID) {
        this.iGameID = iGameID;
    }

    /**
     * @return the iLotteryID
     */
    public String getiLotteryID() {
        return iLotteryID;
    }

    /**
     * @param iLotteryID the iLotteryID to set
     */
    public void setiLotteryID(String iLotteryID) {
        this.iLotteryID = iLotteryID;
    }

    /**
     * @return the sLotteryName
     */
    public String getsLotteryName() {
        return sLotteryName;
    }

    /**
     * @param sLotteryName the sLotteryName to set
     */
    public void setsLotteryName(String sLotteryName) {
        this.sLotteryName = sLotteryName;
    }

    /**
     * @return the sLotteryDate
     */
    public String getsLotteryDate() {
        return sLotteryDate;
    }

    /**
     * @param sLotteryDate the sLotteryDate to set
     */
    public void setsLotteryDate(String sLotteryDate) {
        this.sLotteryDate = sLotteryDate;
    }

    /**
     * @return the aNumbers
     */
    public ANumbers getaNumbers() {
        return aNumbers;
    }

    /**
     * @param aNumbers the aNumbers to set
     */
    public void setaNumbers(ANumbers aNumbers) {
        this.aNumbers = aNumbers;
    }

    /**
     * @return the aPrizes
     */
    public APrizes getaPrizes() {
        return aPrizes;
    }

    /**
     * @param aPrizes the aPrizes to set
     */
    public void setaPrizes(APrizes aPrizes) {
        this.aPrizes = aPrizes;
    }

    /**
     * @return the winners
     */
    public String getWinners() {
        return winners;
    }

    /**
     * @param winners the winners to set
     */
    public void setWinners(String winners) {
        this.winners = winners;
    }
    
}
