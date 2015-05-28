/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bbc.arsenallotto.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Soul
 */
@Entity
@Table(name = "ba_lotto_winnings_summary")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BaLottoWinningsSummary.findAll", query = "SELECT b FROM BaLottoWinningsSummary b"),
    @NamedQuery(name = "BaLottoWinningsSummary.findById", query = "SELECT b FROM BaLottoWinningsSummary b WHERE b.id = :id"),
    @NamedQuery(name = "BaLottoWinningsSummary.findByPrize", query = "SELECT b FROM BaLottoWinningsSummary b WHERE b.prize = :prize"),
    @NamedQuery(name = "BaLottoWinningsSummary.findByQuantity", query = "SELECT b FROM BaLottoWinningsSummary b WHERE b.quantity = :quantity"),
    @NamedQuery(name = "BaLottoWinningsSummary.destroyByNotification", query = "DELETE FROM BaLottoWinningsSummary b WHERE b.notificationId.id = :notificationId"),
    @NamedQuery(name = "BaLottoWinningsSummary.findByTierId", query = "SELECT b FROM BaLottoWinningsSummary b WHERE b.tierId = :tierId")})
public class BaLottoWinningsSummary implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "prize")
    private String prize;
    @Basic(optional = false)
    @Column(name = "tier_name")
    private String tierName;
    @Basic(optional = false)
    @Column(name = "quantity")
    private int quantity;
    @Basic(optional = false)
    @Column(name = "tier_id")
    private short tierId;
    @Basic(optional = false)
    @Column(name = "prize_code")
    private short prizeCode;
    @JoinColumn(name = "notification_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private BaLottoNotifications notificationId;

    public BaLottoWinningsSummary() {
    }

    public BaLottoWinningsSummary(Integer id) {
        this.id = id;
    }

    public BaLottoWinningsSummary(Integer id, String prize, int quantity, short tierId) {
        this.id = id;
        this.prize = prize;
        this.quantity = quantity;
        this.tierId = tierId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public short getTierId() {
        return tierId;
    }

    public void setTierId(short tierId) {
        this.tierId = tierId;
    }

    public BaLottoNotifications getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(BaLottoNotifications notificationId) {
        this.notificationId = notificationId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BaLottoWinningsSummary)) {
            return false;
        }
        BaLottoWinningsSummary other = (BaLottoWinningsSummary) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bbc.arsenallotto.model.BaLottoWinningsSummary[ id=" + id + " ]";
    }

    /**
     * @return the tierName
     */
    public String getTierName() {
        return tierName;
    }

    /**
     * @param tierName the tierName to set
     */
    public void setTierName(String tierName) {
        this.tierName = tierName;
    }

    /**
     * @return the prizeCode
     */
    public short getPrizeCode() {
        return prizeCode;
    }

    /**
     * @param prizeCode the prizeCode to set
     */
    public void setPrizeCode(short prizeCode) {
        this.prizeCode = prizeCode;
    }

}
