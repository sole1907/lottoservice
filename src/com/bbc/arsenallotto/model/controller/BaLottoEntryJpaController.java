/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bbc.arsenallotto.model.controller;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.bbc.arsenallotto.model.BaLottoMember;
import com.bbc.arsenallotto.model.Sts;
import com.bbc.arsenallotto.model.BaLottoChannel;
import com.bbc.arsenallotto.model.BaLottoAgent;
import com.bbc.arsenallotto.model.BaLottoEntry;
import com.bbc.arsenallotto.model.BaLottoEntryDetails;
import java.util.ArrayList;
import java.util.Collection;
import com.bbc.arsenallotto.model.BaLottoWinners;
import com.bbc.arsenallotto.model.controller.exceptions.IllegalOrphanException;
import com.bbc.arsenallotto.model.controller.exceptions.NonexistentEntityException;
import com.bbc.arsenallotto.service.BBCALLogger;
import java.util.List;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

/**
 *
 * @author Soul
 */
public class BaLottoEntryJpaController implements Serializable {

    public BaLottoEntryJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(BaLottoEntry baLottoEntry) {
        if (baLottoEntry.getBaLottoEntryDetailsCollection() == null) {
            baLottoEntry.setBaLottoEntryDetailsCollection(new ArrayList<BaLottoEntryDetails>());
        }
        if (baLottoEntry.getBaLottoWinnersCollection() == null) {
            baLottoEntry.setBaLottoWinnersCollection(new ArrayList<BaLottoWinners>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BaLottoMember memberId = baLottoEntry.getMemberId();
            if (memberId != null) {
                memberId = em.getReference(memberId.getClass(), memberId.getId());
                baLottoEntry.setMemberId(memberId);
            }
            Sts status = baLottoEntry.getStatus();
            if (status != null) {
                status = em.getReference(status.getClass(), status.getTid());
                baLottoEntry.setStatus(status);
            }
            BaLottoChannel channelId = baLottoEntry.getChannelId();
            if (channelId != null) {
                channelId = em.getReference(channelId.getClass(), channelId.getId());
                baLottoEntry.setChannelId(channelId);
            }
            BaLottoAgent agentId = baLottoEntry.getAgentId();
            if (agentId != null) {
                agentId = em.getReference(agentId.getClass(), agentId.getId());
                baLottoEntry.setAgentId(agentId);
            }
            Collection<BaLottoEntryDetails> attachedBaLottoEntryDetailsCollection = new ArrayList<BaLottoEntryDetails>();
            for (BaLottoEntryDetails baLottoEntryDetailsCollectionBaLottoEntryDetailsToAttach : baLottoEntry.getBaLottoEntryDetailsCollection()) {
                baLottoEntryDetailsCollectionBaLottoEntryDetailsToAttach = em.getReference(baLottoEntryDetailsCollectionBaLottoEntryDetailsToAttach.getClass(), baLottoEntryDetailsCollectionBaLottoEntryDetailsToAttach.getId());
                attachedBaLottoEntryDetailsCollection.add(baLottoEntryDetailsCollectionBaLottoEntryDetailsToAttach);
            }
            baLottoEntry.setBaLottoEntryDetailsCollection(attachedBaLottoEntryDetailsCollection);
            Collection<BaLottoWinners> attachedBaLottoWinnersCollection = new ArrayList<BaLottoWinners>();
            for (BaLottoWinners baLottoWinnersCollectionBaLottoWinnersToAttach : baLottoEntry.getBaLottoWinnersCollection()) {
                baLottoWinnersCollectionBaLottoWinnersToAttach = em.getReference(baLottoWinnersCollectionBaLottoWinnersToAttach.getClass(), baLottoWinnersCollectionBaLottoWinnersToAttach.getId());
                attachedBaLottoWinnersCollection.add(baLottoWinnersCollectionBaLottoWinnersToAttach);
            }
            baLottoEntry.setBaLottoWinnersCollection(attachedBaLottoWinnersCollection);
            em.persist(baLottoEntry);
            if (memberId != null) {
                memberId.getBaLottoEntryCollection().add(baLottoEntry);
                memberId = em.merge(memberId);
            }
            if (status != null) {
                status.getBaLottoEntryCollection().add(baLottoEntry);
                status = em.merge(status);
            }
            if (channelId != null) {
                channelId.getBaLottoEntryCollection().add(baLottoEntry);
                channelId = em.merge(channelId);
            }
            if (agentId != null) {
                agentId.getBaLottoEntryCollection().add(baLottoEntry);
                agentId = em.merge(agentId);
            }
            for (BaLottoEntryDetails baLottoEntryDetailsCollectionBaLottoEntryDetails : baLottoEntry.getBaLottoEntryDetailsCollection()) {
                BaLottoEntry oldEntryIdOfBaLottoEntryDetailsCollectionBaLottoEntryDetails = baLottoEntryDetailsCollectionBaLottoEntryDetails.getEntryId();
                baLottoEntryDetailsCollectionBaLottoEntryDetails.setEntryId(baLottoEntry);
                baLottoEntryDetailsCollectionBaLottoEntryDetails = em.merge(baLottoEntryDetailsCollectionBaLottoEntryDetails);
                if (oldEntryIdOfBaLottoEntryDetailsCollectionBaLottoEntryDetails != null) {
                    oldEntryIdOfBaLottoEntryDetailsCollectionBaLottoEntryDetails.getBaLottoEntryDetailsCollection().remove(baLottoEntryDetailsCollectionBaLottoEntryDetails);
                    oldEntryIdOfBaLottoEntryDetailsCollectionBaLottoEntryDetails = em.merge(oldEntryIdOfBaLottoEntryDetailsCollectionBaLottoEntryDetails);
                }
            }
            for (BaLottoWinners baLottoWinnersCollectionBaLottoWinners : baLottoEntry.getBaLottoWinnersCollection()) {
                BaLottoEntry oldLottoReferenceOfBaLottoWinnersCollectionBaLottoWinners = baLottoWinnersCollectionBaLottoWinners.getLottoReference();
                baLottoWinnersCollectionBaLottoWinners.setLottoReference(baLottoEntry);
                baLottoWinnersCollectionBaLottoWinners = em.merge(baLottoWinnersCollectionBaLottoWinners);
                if (oldLottoReferenceOfBaLottoWinnersCollectionBaLottoWinners != null) {
                    oldLottoReferenceOfBaLottoWinnersCollectionBaLottoWinners.getBaLottoWinnersCollection().remove(baLottoWinnersCollectionBaLottoWinners);
                    oldLottoReferenceOfBaLottoWinnersCollectionBaLottoWinners = em.merge(oldLottoReferenceOfBaLottoWinnersCollectionBaLottoWinners);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(BaLottoEntry baLottoEntry) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BaLottoEntry persistentBaLottoEntry = em.find(BaLottoEntry.class, baLottoEntry.getId());
            BaLottoMember memberIdOld = persistentBaLottoEntry.getMemberId();
            BaLottoMember memberIdNew = baLottoEntry.getMemberId();
            Sts statusOld = persistentBaLottoEntry.getStatus();
            Sts statusNew = baLottoEntry.getStatus();
            BaLottoChannel channelIdOld = persistentBaLottoEntry.getChannelId();
            BaLottoChannel channelIdNew = baLottoEntry.getChannelId();
            BaLottoAgent agentIdOld = persistentBaLottoEntry.getAgentId();
            BaLottoAgent agentIdNew = baLottoEntry.getAgentId();
            Collection<BaLottoEntryDetails> baLottoEntryDetailsCollectionOld = persistentBaLottoEntry.getBaLottoEntryDetailsCollection();
            Collection<BaLottoEntryDetails> baLottoEntryDetailsCollectionNew = baLottoEntry.getBaLottoEntryDetailsCollection();
            Collection<BaLottoWinners> baLottoWinnersCollectionOld = persistentBaLottoEntry.getBaLottoWinnersCollection();
            Collection<BaLottoWinners> baLottoWinnersCollectionNew = baLottoEntry.getBaLottoWinnersCollection();
            List<String> illegalOrphanMessages = null;
            for (BaLottoEntryDetails baLottoEntryDetailsCollectionOldBaLottoEntryDetails : baLottoEntryDetailsCollectionOld) {
                if (!baLottoEntryDetailsCollectionNew.contains(baLottoEntryDetailsCollectionOldBaLottoEntryDetails)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain BaLottoEntryDetails " + baLottoEntryDetailsCollectionOldBaLottoEntryDetails + " since its entryId field is not nullable.");
                }
            }
            for (BaLottoWinners baLottoWinnersCollectionOldBaLottoWinners : baLottoWinnersCollectionOld) {
                if (!baLottoWinnersCollectionNew.contains(baLottoWinnersCollectionOldBaLottoWinners)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain BaLottoWinners " + baLottoWinnersCollectionOldBaLottoWinners + " since its lottoReference field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (memberIdNew != null) {
                memberIdNew = em.getReference(memberIdNew.getClass(), memberIdNew.getId());
                baLottoEntry.setMemberId(memberIdNew);
            }
            if (statusNew != null) {
                statusNew = em.getReference(statusNew.getClass(), statusNew.getTid());
                baLottoEntry.setStatus(statusNew);
            }
            if (channelIdNew != null) {
                channelIdNew = em.getReference(channelIdNew.getClass(), channelIdNew.getId());
                baLottoEntry.setChannelId(channelIdNew);
            }
            if (agentIdNew != null) {
                agentIdNew = em.getReference(agentIdNew.getClass(), agentIdNew.getId());
                baLottoEntry.setAgentId(agentIdNew);
            }
            Collection<BaLottoEntryDetails> attachedBaLottoEntryDetailsCollectionNew = new ArrayList<BaLottoEntryDetails>();
            for (BaLottoEntryDetails baLottoEntryDetailsCollectionNewBaLottoEntryDetailsToAttach : baLottoEntryDetailsCollectionNew) {
                baLottoEntryDetailsCollectionNewBaLottoEntryDetailsToAttach = em.getReference(baLottoEntryDetailsCollectionNewBaLottoEntryDetailsToAttach.getClass(), baLottoEntryDetailsCollectionNewBaLottoEntryDetailsToAttach.getId());
                attachedBaLottoEntryDetailsCollectionNew.add(baLottoEntryDetailsCollectionNewBaLottoEntryDetailsToAttach);
            }
            baLottoEntryDetailsCollectionNew = attachedBaLottoEntryDetailsCollectionNew;
            baLottoEntry.setBaLottoEntryDetailsCollection(baLottoEntryDetailsCollectionNew);
            Collection<BaLottoWinners> attachedBaLottoWinnersCollectionNew = new ArrayList<BaLottoWinners>();
            for (BaLottoWinners baLottoWinnersCollectionNewBaLottoWinnersToAttach : baLottoWinnersCollectionNew) {
                baLottoWinnersCollectionNewBaLottoWinnersToAttach = em.getReference(baLottoWinnersCollectionNewBaLottoWinnersToAttach.getClass(), baLottoWinnersCollectionNewBaLottoWinnersToAttach.getId());
                attachedBaLottoWinnersCollectionNew.add(baLottoWinnersCollectionNewBaLottoWinnersToAttach);
            }
            baLottoWinnersCollectionNew = attachedBaLottoWinnersCollectionNew;
            baLottoEntry.setBaLottoWinnersCollection(baLottoWinnersCollectionNew);
            baLottoEntry = em.merge(baLottoEntry);
            if (memberIdOld != null && !memberIdOld.equals(memberIdNew)) {
                memberIdOld.getBaLottoEntryCollection().remove(baLottoEntry);
                memberIdOld = em.merge(memberIdOld);
            }
            if (memberIdNew != null && !memberIdNew.equals(memberIdOld)) {
                memberIdNew.getBaLottoEntryCollection().add(baLottoEntry);
                memberIdNew = em.merge(memberIdNew);
            }
            if (statusOld != null && !statusOld.equals(statusNew)) {
                statusOld.getBaLottoEntryCollection().remove(baLottoEntry);
                statusOld = em.merge(statusOld);
            }
            if (statusNew != null && !statusNew.equals(statusOld)) {
                statusNew.getBaLottoEntryCollection().add(baLottoEntry);
                statusNew = em.merge(statusNew);
            }
            if (channelIdOld != null && !channelIdOld.equals(channelIdNew)) {
                channelIdOld.getBaLottoEntryCollection().remove(baLottoEntry);
                channelIdOld = em.merge(channelIdOld);
            }
            if (channelIdNew != null && !channelIdNew.equals(channelIdOld)) {
                channelIdNew.getBaLottoEntryCollection().add(baLottoEntry);
                channelIdNew = em.merge(channelIdNew);
            }
            if (agentIdOld != null && !agentIdOld.equals(agentIdNew)) {
                agentIdOld.getBaLottoEntryCollection().remove(baLottoEntry);
                agentIdOld = em.merge(agentIdOld);
            }
            if (agentIdNew != null && !agentIdNew.equals(agentIdOld)) {
                agentIdNew.getBaLottoEntryCollection().add(baLottoEntry);
                agentIdNew = em.merge(agentIdNew);
            }
            for (BaLottoEntryDetails baLottoEntryDetailsCollectionNewBaLottoEntryDetails : baLottoEntryDetailsCollectionNew) {
                if (!baLottoEntryDetailsCollectionOld.contains(baLottoEntryDetailsCollectionNewBaLottoEntryDetails)) {
                    BaLottoEntry oldEntryIdOfBaLottoEntryDetailsCollectionNewBaLottoEntryDetails = baLottoEntryDetailsCollectionNewBaLottoEntryDetails.getEntryId();
                    baLottoEntryDetailsCollectionNewBaLottoEntryDetails.setEntryId(baLottoEntry);
                    baLottoEntryDetailsCollectionNewBaLottoEntryDetails = em.merge(baLottoEntryDetailsCollectionNewBaLottoEntryDetails);
                    if (oldEntryIdOfBaLottoEntryDetailsCollectionNewBaLottoEntryDetails != null && !oldEntryIdOfBaLottoEntryDetailsCollectionNewBaLottoEntryDetails.equals(baLottoEntry)) {
                        oldEntryIdOfBaLottoEntryDetailsCollectionNewBaLottoEntryDetails.getBaLottoEntryDetailsCollection().remove(baLottoEntryDetailsCollectionNewBaLottoEntryDetails);
                        oldEntryIdOfBaLottoEntryDetailsCollectionNewBaLottoEntryDetails = em.merge(oldEntryIdOfBaLottoEntryDetailsCollectionNewBaLottoEntryDetails);
                    }
                }
            }
            for (BaLottoWinners baLottoWinnersCollectionNewBaLottoWinners : baLottoWinnersCollectionNew) {
                if (!baLottoWinnersCollectionOld.contains(baLottoWinnersCollectionNewBaLottoWinners)) {
                    BaLottoEntry oldLottoReferenceOfBaLottoWinnersCollectionNewBaLottoWinners = baLottoWinnersCollectionNewBaLottoWinners.getLottoReference();
                    baLottoWinnersCollectionNewBaLottoWinners.setLottoReference(baLottoEntry);
                    baLottoWinnersCollectionNewBaLottoWinners = em.merge(baLottoWinnersCollectionNewBaLottoWinners);
                    if (oldLottoReferenceOfBaLottoWinnersCollectionNewBaLottoWinners != null && !oldLottoReferenceOfBaLottoWinnersCollectionNewBaLottoWinners.equals(baLottoEntry)) {
                        oldLottoReferenceOfBaLottoWinnersCollectionNewBaLottoWinners.getBaLottoWinnersCollection().remove(baLottoWinnersCollectionNewBaLottoWinners);
                        oldLottoReferenceOfBaLottoWinnersCollectionNewBaLottoWinners = em.merge(oldLottoReferenceOfBaLottoWinnersCollectionNewBaLottoWinners);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = baLottoEntry.getId();
                if (findBaLottoEntry(id) == null) {
                    throw new NonexistentEntityException("The baLottoEntry with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BaLottoEntry baLottoEntry;
            try {
                baLottoEntry = em.getReference(BaLottoEntry.class, id);
                baLottoEntry.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The baLottoEntry with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<BaLottoEntryDetails> baLottoEntryDetailsCollectionOrphanCheck = baLottoEntry.getBaLottoEntryDetailsCollection();
            for (BaLottoEntryDetails baLottoEntryDetailsCollectionOrphanCheckBaLottoEntryDetails : baLottoEntryDetailsCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This BaLottoEntry (" + baLottoEntry + ") cannot be destroyed since the BaLottoEntryDetails " + baLottoEntryDetailsCollectionOrphanCheckBaLottoEntryDetails + " in its baLottoEntryDetailsCollection field has a non-nullable entryId field.");
            }
            Collection<BaLottoWinners> baLottoWinnersCollectionOrphanCheck = baLottoEntry.getBaLottoWinnersCollection();
            for (BaLottoWinners baLottoWinnersCollectionOrphanCheckBaLottoWinners : baLottoWinnersCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This BaLottoEntry (" + baLottoEntry + ") cannot be destroyed since the BaLottoWinners " + baLottoWinnersCollectionOrphanCheckBaLottoWinners + " in its baLottoWinnersCollection field has a non-nullable lottoReference field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            BaLottoMember memberId = baLottoEntry.getMemberId();
            if (memberId != null) {
                memberId.getBaLottoEntryCollection().remove(baLottoEntry);
                memberId = em.merge(memberId);
            }
            Sts status = baLottoEntry.getStatus();
            if (status != null) {
                status.getBaLottoEntryCollection().remove(baLottoEntry);
                status = em.merge(status);
            }
            BaLottoChannel channelId = baLottoEntry.getChannelId();
            if (channelId != null) {
                channelId.getBaLottoEntryCollection().remove(baLottoEntry);
                channelId = em.merge(channelId);
            }
            BaLottoAgent agentId = baLottoEntry.getAgentId();
            if (agentId != null) {
                agentId.getBaLottoEntryCollection().remove(baLottoEntry);
                agentId = em.merge(agentId);
            }
            em.remove(baLottoEntry);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<BaLottoEntry> findBaLottoEntryEntities() {
        return findBaLottoEntryEntities(true, -1, -1);
    }

    public List<BaLottoEntry> findBaLottoEntryEntities(int maxResults, int firstResult) {
        return findBaLottoEntryEntities(false, maxResults, firstResult);
    }

    private List<BaLottoEntry> findBaLottoEntryEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(BaLottoEntry.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public BaLottoEntry findBaLottoEntry(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(BaLottoEntry.class, id);
        } finally {
            em.close();
        }
    }

    public int getBaLottoEntryCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<BaLottoEntry> rt = cq.from(BaLottoEntry.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<BaLottoEntry> findQueuedEntries(String successStatus, int maxAttempts, int limit) {
        List<BaLottoEntry> queuedLottoEntries = null;

        EntityManager em = getEntityManager();
        TypedQuery<BaLottoEntry> queuedEntries = em.createNamedQuery("BaLottoEntry.findQueuedEntries", BaLottoEntry.class);
//set dynamic data for query
        queuedEntries.setParameter("action", successStatus);
        queuedEntries.setParameter("maxattempts", maxAttempts);
        queuedEntries.setMaxResults(limit);

        try {
//execute query and get results
            queuedLottoEntries = queuedEntries.getResultList();
        } catch (NoResultException nre) {
            queuedLottoEntries = null;
            BBCALLogger.getLogger().log(Level.INFO, null, nre);
        } finally {
            em.close();
        }

        return queuedLottoEntries;
    }

    public BaLottoEntry findByLottoReference(String lottoReference) {
        BaLottoEntry lottoEntry = null;

        EntityManager em = getEntityManager();
        TypedQuery<BaLottoEntry> queuedEntries = em.createNamedQuery("BaLottoEntry.findByLottoReference", BaLottoEntry.class);
//set dynamic data for query
        queuedEntries.setParameter("lottoReference", lottoReference);

        try {
//execute query and get results
            lottoEntry = queuedEntries.getSingleResult();
        } catch (NoResultException nre) {
            lottoEntry = null;
            BBCALLogger.getLogger().log(Level.INFO, null, nre);
        } finally {
            em.close();
        }

        return lottoEntry;
    }
}
