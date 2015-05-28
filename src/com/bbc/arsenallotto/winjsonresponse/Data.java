/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bbc.arsenallotto.winjsonresponse;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Noble
 */
@XmlRootElement
public class Data {

    private AParams aParams;
    private AMessages aMessages;
    private ODraw oDraw;
    private String messages;

    /**
     * @return the aParams
     */
    public AParams getaParams() {
        return aParams;
    }

    /**
     * @param aParams the aParams to set
     */
    public void setaParams(AParams aParams) {
        this.aParams = aParams;
    }

    /**
     * @return the aMessages
     */
    public AMessages getaMessages() {
        return aMessages;
    }

    /**
     * @param aMessages the aMessages to set
     */
    public void setaMessages(AMessages aMessages) {
        this.aMessages = aMessages;
    }

    /**
     * @return the messages
     */
    public String getMessages() {
        return messages;
    }

    /**
     * @param messages the messages to set
     */
    public void setMessages(String messages) {
        this.messages = messages;
    }

    /**
     * @return the oDraw
     */
    public ODraw getoDraw() {
        return oDraw;
    }

    /**
     * @param oDraw the oDraw to set
     */
    public void setoDraw(ODraw oDraw) {
        this.oDraw = oDraw;
    }

}
