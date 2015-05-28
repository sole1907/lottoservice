/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bbc.arsenallotto.model.controller;

import com.bbc.arsenallotto.model.BaLottoAgent;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.bbc.arsenallotto.model.Sts;
import com.bbc.arsenallotto.model.Prle;
import com.bbc.arsenallotto.model.BaLottoEntry;
import com.bbc.arsenallotto.model.controller.exceptions.IllegalOrphanException;
import com.bbc.arsenallotto.model.controller.exceptions.NonexistentEntityException;
import com.bbc.arsenallotto.model.controller.exceptions.PreexistingEntityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Soul
 */
public class BaLottoAgentJpaController implements Serializable {

    public BaLottoAgentJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(BaLottoAgent baLottoAgent) throws PreexistingEntityException, Exception {
        if (baLottoAgent.getBaLottoEntryCollection() == null) {
            baLottoAgent.setBaLottoEntryCollection(new ArrayList<BaLottoEntry>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Sts sts = baLottoAgent.getSts();
            if (sts != null) {
                sts = em.getReference(sts.getClass(), sts.getTid());
                baLottoAgent.setSts(sts);
            }
            Prle CByy = baLottoAgent.getCByy();
            if (CByy != null) {
                CByy = em.getReference(CByy.getClass(), CByy.getTid());
                baLottoAgent.setCByy(CByy);
            }
            Collection<BaLottoEntry> attachedBaLottoEntryCollection = new ArrayList<BaLottoEntry>();
            for (BaLottoEntry baLottoEntryCollectionBaLottoEntryToAttach : baLottoAgent.getBaLottoEntryCollection()) {
                baLottoEntryCollectionBaLottoEntryToAttach = em.getReference(baLottoEntryCollectionBaLottoEntryToAttach.getClass(), baLottoEntryCollectionBaLottoEntryToAttach.getId());
                attachedBaLottoEntryCollection.add(baLottoEntryCollectionBaLottoEntryToAttach);
            }
            baLottoAgent.setBaLottoEntryCollection(attachedBaLottoEntryCollection);
            em.persist(baLottoAgent);
            if (sts != null) {
                sts.getBaLottoAgentCollection().add(baLottoAgent);
                sts = em.merge(sts);
            }
            if (CByy != null) {
                CByy.getBaLottoAgentCollection().add(baLottoAgent);
                CByy = em.merge(CByy);
            }
            for (BaLottoEntry baLottoEntryCollectionBaLottoEntry : baLottoAgent.getBaLottoEntryCollection()) {
                BaLottoAgent oldAgentIdOfBaLottoEntryCollectionBaLottoEntry = baLottoEntryCollectionBaLottoEntry.getAgentId();
                baLottoEntryCollectionBaLottoEntry.setAgentId(baLottoAgent);
                baLottoEntryCollectionBaLottoEntry = em.merge(baLottoEntryCollectionBaLottoEntry);
                if (oldAgentIdOfBaLottoEntryCollectionBaLottoEntry != null) {
                    oldAgentIdOfBaLottoEntryCollectionBaLottoEntry.getBaLottoEntryCollection().remove(baLottoEntryCollectionBaLottoEntry);
                    oldAgentIdOfBaLottoEntryCollectionBaLottoEntry = em.merge(oldAgentIdOfBaLottoEntryCollectionBaLottoEntry);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findBaLottoAgent(baLottoAgent.getId()) != null) {
                throw new PreexistingEntityException("BaLottoAgent " + baLottoAgent + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(BaLottoAgent baLottoAgent) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BaLottoAgent persistentBaLottoAgent = em.find(BaLottoAgent.class, baLottoAgent.getId());
            Sts stsOld = persistentBaLottoAgent.getSts();
            Sts stsNew = baLottoAgent.getSts();
            Prle CByyOld = persistentBaLottoAgent.getCByy();
            Prle CByyNew = baLottoAgent.getCByy();
            Collection<BaLottoEntry> baLottoEntryCollectionOld = persistentBaLottoAgent.getBaLottoEntryCollection();
            Collection<BaLottoEntry> baLottoEntryCollectionNew = baLottoAgent.getBaLottoEntryCollection();
            List<String> illegalOrphanMessages = null;
            for (BaLottoEntry baLottoEntryCollectionOldBaLottoEntry : baLottoEntryCollectionOld) {
                if (!baLottoEntryCollectionNew.contains(baLottoEntryCollectionOldBaLottoEntry)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain BaLottoEntry " + baLottoEntryCollectionOldBaLottoEntry + " since its agentId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (stsNew != null) {
                stsNew = em.getReference(stsNew.getClass(), stsNew.getTid());
                baLottoAgent.setSts(stsNew);
            }
            if (CByyNew != null) {
                CByyNew = em.getReference(CByyNew.getClass(), CByyNew.getTid());
                baLottoAgent.setCByy(CByyNew);
            }
            Collection<BaLottoEntry> attachedBaLottoEntryCollectionNew = new ArrayList<BaLottoEntry>();
            for (BaLottoEntry baLottoEntryCollectionNewBaLottoEntryToAttach : baLottoEntryCollectionNew) {
                baLottoEntryCollectionNewBaLottoEntryToAttach = em.getReference(baLottoEntryCollectionNewBaLottoEntryToAttach.getClass(), baLottoEntryCollectionNewBaLottoEntryToAttach.getId());
                attachedBaLottoEntryCollectionNew.add(baLottoEntryCollectionNewBaLottoEntryToAttach);
            }
            baLottoEntryCollectionNew = attachedBaLottoEntryCollectionNew;
            baLottoAgent.setBaLottoEntryCollection(baLottoEntryCollectionNew);
            baLottoAgent = em.merge(baLottoAgent);
            if (stsOld != null && !stsOld.equals(stsNew)) {
                stsOld.getBaLottoAgentCollection().remove(baLottoAgent);
                stsOld = em.merge(stsOld);
            }
            if (stsNew != null && !stsNew.equals(stsOld)) {
                stsNew.getBaLottoAgentCollection().add(baLottoAgent);
                stsNew = em.merge(stsNew);
            }
            if (CByyOld != null && !CByyOld.equals(CByyNew)) {
                CByyOld.getBaLottoAgentCollection().remove(baLottoAgent);
                CByyOld = em.merge(CByyOld);
            }
            if (CByyNew != null && !CByyNew.equals(CByyOld)) {
                CByyNew.getBaLottoAgentCollection().add(baLottoAgent);
                CByyNew = em.merge(CByyNew);
            }
            for (BaLottoEntry baLottoEntryCollectionNewBaLottoEntry : baLottoEntryCollectionNew) {
                if (!baLottoEntryCollectionOld.contains(baLottoEntryCollectionNewBaLottoEntry)) {
                    BaLottoAgent oldAgentIdOfBaLottoEntryCollectionNewBaLottoEntry = baLottoEntryCollectionNewBaLottoEntry.getAgentId();
                    baLottoEntryCollectionNewBaLottoEntry.setAgentId(baLottoAgent);
                    baLottoEntryCollectionNewBaLottoEntry = em.merge(baLottoEntryCollectionNewBaLottoEntry);
                    if (oldAgentIdOfBaLottoEntryCollectionNewBaLottoEntry != null && !oldAgentIdOfBaLottoEntryCollectionNewBaLottoEntry.equals(baLottoAgent)) {
                        oldAgentIdOfBaLottoEntryCollectionNewBaLottoEntry.getBaLottoEntryCollection().remove(baLottoEntryCollectionNewBaLottoEntry);
                        oldAgentIdOfBaLottoEntryCollectionNewBaLottoEntry = em.merge(oldAgentIdOfBaLottoEntryCollectionNewBaLottoEntry);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = baLottoAgent.getId();
                if (findBaLottoAgent(id) == null) {
                    throw new NonexistentEntityException("The baLottoAgent with id " + id + " no longer exists.");
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
            BaLottoAgent baLottoAgent;
            try {
                baLottoAgent = em.getReference(BaLottoAgent.class, id);
                baLottoAgent.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The baLottoAgent with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<BaLottoEntry> baLottoEntryCollectionOrphanCheck = baLottoAgent.getBaLottoEntryCollection();
            for (BaLottoEntry baLottoEntryCollectionOrphanCheckBaLottoEntry : baLottoEntryCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This BaLottoAgent (" + baLottoAgent + ") cannot be destroyed since the BaLottoEntry " + baLottoEntryCollectionOrphanCheckBaLottoEntry + " in its baLottoEntryCollection field has a non-nullable agentId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Sts sts = baLottoAgent.getSts();
            if (sts != null) {
                sts.getBaLottoAgentCollection().remove(baLottoAgent);
                sts = em.merge(sts);
            }
            Prle CByy = baLottoAgent.getCByy();
            if (CByy != null) {
                CByy.getBaLottoAgentCollection().remove(baLottoAgent);
                CByy = em.merge(CByy);
            }
            em.remove(baLottoAgent);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<BaLottoAgent> findBaLottoAgentEntities() {
        return findBaLottoAgentEntities(true, -1, -1);
    }

    public List<BaLottoAgent> findBaLottoAgentEntities(int maxResults, int firstResult) {
        return findBaLottoAgentEntities(false, maxResults, firstResult);
    }

    private List<BaLottoAgent> findBaLottoAgentEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(BaLottoAgent.class));
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

    public BaLottoAgent findBaLottoAgent(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(BaLottoAgent.class, id);
        } finally {
            em.close();
        }
    }

    public int getBaLottoAgentCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<BaLottoAgent> rt = cq.from(BaLottoAgent.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
