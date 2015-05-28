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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Soul
 */
@Entity
@Table(name = "ba_lotto_winnings_total")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BaLottoWinningsTotal.findAll", query = "SELECT b FROM BaLottoWinningsTotal b"),
    @NamedQuery(name = "BaLottoWinningsTotal.findById", query = "SELECT b FROM BaLottoWinningsTotal b WHERE b.id = :id"),
    @NamedQuery(name = "BaLottoWinningsTotal.findByNotification", query = "SELECT b FROM BaLottoWinningsTotal b WHERE b.notificationId.id = :notificationId"),
    @NamedQuery(name = "BaLottoWinningsTotal.findByTotalPrize", query = "SELECT b FROM BaLottoWinningsTotal b WHERE b.totalPrize = :totalPrize")})
public class BaLottoWinningsTotal implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "total_prize")
    private BigDecimal totalPrize;
    @JoinColumn(name = "notification_id", referencedColumnName = "id")
    @OneToOne(optional = false)
    private BaLottoNotifications notificationId;

    public BaLottoWinningsTotal() {
    }

    public BaLottoWinningsTotal(Integer id) {
        this.id = id;
    }

    public BaLottoWinningsTotal(Integer id, BigDecimal totalPrize) {
        this.id = id;
        this.totalPrize = totalPrize;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getTotalPrize() {
        return totalPrize;
    }

    public void setTotalPrize(BigDecimal totalPrize) {
        this.totalPrize = totalPrize;
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
        if (!(object instanceof BaLottoWinningsTotal)) {
            return false;
        }
        BaLottoWinningsTotal other = (BaLottoWinningsTotal) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bbc.arsenallotto.model.BaLottoWinningsTotal[ id=" + id + " ]";
    }
    
}
