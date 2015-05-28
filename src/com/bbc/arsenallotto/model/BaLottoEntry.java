/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bbc.arsenallotto.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Soul
 */
@Entity
@Table(name = "ba_lotto_entry")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BaLottoEntry.findAll", query = "SELECT b FROM BaLottoEntry b"),
    @NamedQuery(name = "BaLottoEntry.findById", query = "SELECT b FROM BaLottoEntry b WHERE b.id = :id"),
    @NamedQuery(name = "BaLottoEntry.findByPaymentReference", query = "SELECT b FROM BaLottoEntry b WHERE b.paymentReference = :paymentReference"),
    @NamedQuery(name = "BaLottoEntry.findByLottoReference", query = "SELECT b FROM BaLottoEntry b WHERE b.lottoReference = :lottoReference"),
    @NamedQuery(name = "BaLottoEntry.findByAmount", query = "SELECT b FROM BaLottoEntry b WHERE b.amount = :amount"),
    @NamedQuery(name = "BaLottoEntry.findByDays", query = "SELECT b FROM BaLottoEntry b WHERE b.days = :days"),
    @NamedQuery(name = "BaLottoEntry.findByDuration", query = "SELECT b FROM BaLottoEntry b WHERE b.duration = :duration"),
    @NamedQuery(name = "BaLottoEntry.findByEntryDate", query = "SELECT b FROM BaLottoEntry b WHERE b.entryDate = :entryDate"),
    @NamedQuery(name = "BaLottoEntry.findByResponseCode", query = "SELECT b FROM BaLottoEntry b WHERE b.responseCode = :responseCode"),
    @NamedQuery(name = "BaLottoEntry.findByResponseMessage", query = "SELECT b FROM BaLottoEntry b WHERE b.responseMessage = :responseMessage"),
    @NamedQuery(name = "BaLottoEntry.findByResponseDate", query = "SELECT b FROM BaLottoEntry b WHERE b.responseDate = :responseDate"),
    @NamedQuery(name = "BaLottoEntry.findByCheckDigit", query = "SELECT b FROM BaLottoEntry b WHERE b.checkDigit = :checkDigit"),
    @NamedQuery(name = "BaLottoEntry.findByAction", query = "SELECT b FROM BaLottoEntry b WHERE b.action = :action"),
    @NamedQuery(name = "BaLottoEntry.findByOrderId", query = "SELECT b FROM BaLottoEntry b WHERE b.orderId = :orderId"),
    @NamedQuery(name = "BaLottoEntry.findByBookingRef", query = "SELECT b FROM BaLottoEntry b WHERE b.bookingRef = :bookingRef"),
    @NamedQuery(name = "BaLottoEntry.findQueuedEntries", query = "SELECT b FROM BaLottoEntry b WHERE (b.action is null or b.action <> :action) and b.attempts < :maxattempts"),
    @NamedQuery(name = "BaLottoEntry.findByAttempts", query = "SELECT b FROM BaLottoEntry b WHERE b.attempts = :attempts")})
public class BaLottoEntry implements Serializable {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lottoReference")
    private Collection<BaLottoWinners> baLottoWinnersCollection;
    @Basic(optional = false)
    @Column(name = "game_id")
    private int gameId;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "payment_reference")
    private String paymentReference;
    @Column(name = "lotto_reference")
    private String lottoReference;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "amount")
    private BigDecimal amount;
    @Basic(optional = false)
    @Column(name = "days")
    private short days;
    @Basic(optional = false)
    @Column(name = "duration")
    private short duration;
    @Basic(optional = false)
    @Column(name = "entry_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date entryDate;
    @Column(name = "response_code")
    private Integer responseCode;
    @Column(name = "response_message")
    private String responseMessage;
    @Column(name = "response_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date responseDate;
    @Basic(optional = false)
    @Column(name = "check_digit")
    private String checkDigit;
    @Column(name = "action")
    private String action;
    @Column(name = "order_id")
    private String orderId;
    @Column(name = "booking_ref")
    private String bookingRef;
    @Column(name = "attempts")
    private Short attempts;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entryId")
    private Collection<BaLottoEntryDetails> baLottoEntryDetailsCollection;
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private BaLottoMember memberId;
    @JoinColumn(name = "status", referencedColumnName = "TID")
    @ManyToOne
    private Sts status;
    @JoinColumn(name = "channel_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private BaLottoChannel channelId;
    @JoinColumn(name = "agent_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private BaLottoAgent agentId;

    public BaLottoEntry() {
    }

    public BaLottoEntry(Integer id) {
        this.id = id;
    }

    public BaLottoEntry(Integer id, String paymentReference, BigDecimal amount, short days, short duration, Date entryDate, String checkDigit) {
        this.id = id;
        this.paymentReference = paymentReference;
        this.amount = amount;
        this.days = days;
        this.duration = duration;
        this.entryDate = entryDate;
        this.checkDigit = checkDigit;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public String getLottoReference() {
        return lottoReference;
    }

    public void setLottoReference(String lottoReference) {
        this.lottoReference = lottoReference;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public short getDays() {
        return days;
    }

    public void setDays(short days) {
        this.days = days;
    }

    public short getDuration() {
        return duration;
    }

    public void setDuration(short duration) {
        this.duration = duration;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public Date getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(Date responseDate) {
        this.responseDate = responseDate;
    }

    public String getCheckDigit() {
        return checkDigit;
    }

    public void setCheckDigit(String checkDigit) {
        this.checkDigit = checkDigit;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getBookingRef() {
        return bookingRef;
    }

    public void setBookingRef(String bookingRef) {
        this.bookingRef = bookingRef;
    }

    public Short getAttempts() {
        return attempts;
    }

    public void setAttempts(Short attempts) {
        this.attempts = attempts;
    }

    @XmlTransient
    public Collection<BaLottoEntryDetails> getBaLottoEntryDetailsCollection() {
        return baLottoEntryDetailsCollection;
    }

    public void setBaLottoEntryDetailsCollection(Collection<BaLottoEntryDetails> baLottoEntryDetailsCollection) {
        this.baLottoEntryDetailsCollection = baLottoEntryDetailsCollection;
    }

    public BaLottoMember getMemberId() {
        return memberId;
    }

    public void setMemberId(BaLottoMember memberId) {
        this.memberId = memberId;
    }

    public Sts getStatus() {
        return status;
    }

    public void setStatus(Sts status) {
        this.status = status;
    }

    public BaLottoChannel getChannelId() {
        return channelId;
    }

    public void setChannelId(BaLottoChannel channelId) {
        this.channelId = channelId;
    }

    public BaLottoAgent getAgentId() {
        return agentId;
    }

    public void setAgentId(BaLottoAgent agentId) {
        this.agentId = agentId;
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
        if (!(object instanceof BaLottoEntry)) {
            return false;
        }
        BaLottoEntry other = (BaLottoEntry) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bbc.arsenallotto.model.BaLottoEntry[ id=" + id + " ]";
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    @XmlTransient
    public Collection<BaLottoWinners> getBaLottoWinnersCollection() {
        return baLottoWinnersCollection;
    }

    public void setBaLottoWinnersCollection(Collection<BaLottoWinners> baLottoWinnersCollection) {
        this.baLottoWinnersCollection = baLottoWinnersCollection;
    }

}
