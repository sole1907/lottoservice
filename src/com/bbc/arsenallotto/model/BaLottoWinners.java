/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bbc.arsenallotto.model;

import java.io.Serializable;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Soul
 */
@Entity
@Table(name = "ba_lotto_winners")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BaLottoWinners.findAll", query = "SELECT b FROM BaLottoWinners b"),
    @NamedQuery(name = "BaLottoWinners.findById", query = "SELECT b FROM BaLottoWinners b WHERE b.id = :id"),
    @NamedQuery(name = "BaLottoWinners.findByOrderId", query = "SELECT b FROM BaLottoWinners b WHERE b.orderId = :orderId"),
    @NamedQuery(name = "BaLottoWinners.destroyByNotification", query = "DELETE FROM BaLottoWinners b WHERE b.notificationId.id = :notificationId"),
    @NamedQuery(name = "BaLottoWinners.findByTierId", query = "SELECT b FROM BaLottoWinners b WHERE b.tierId = :tierId")})
// Define a sequence - might also be in another class:
@SequenceGenerator(name="winners", initialValue=1, allocationSize=1000)
public class BaLottoWinners implements Serializable {
    @JoinColumn(name = "lotto_reference", referencedColumnName = "lotto_reference")
    @ManyToOne(optional = false)
    private BaLottoEntry lottoReference;
    private static final long serialVersionUID = 1L;
    @Id
    // Use the sequence that is defined above:
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="winners")
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "order_id")
    private String orderId;
    @Basic(optional = false)
    @Column(name = "tier_id")
    private short tierId;
    @JoinColumn(name = "notification_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private BaLottoNotifications notificationId;

    public BaLottoWinners() {
    }

    public BaLottoWinners(Integer id) {
        this.id = id;
    }

    public BaLottoWinners(Integer id, String orderId, short tierId) {
        this.id = id;
        this.orderId = orderId;
        this.tierId = tierId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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
        if (!(object instanceof BaLottoWinners)) {
            return false;
        }
        BaLottoWinners other = (BaLottoWinners) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bbc.arsenallotto.model.BaLottoWinners[ id=" + id + " ]";
    }

    public BaLottoEntry getLottoReference() {
        return lottoReference;
    }

    public void setLottoReference(BaLottoEntry lottoReference) {
        this.lottoReference = lottoReference;
    }
    
}
