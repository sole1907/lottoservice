/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bbc.arsenallotto.model.controller;

import com.bbc.arsenallotto.model.BaLottoNotifications;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.bbc.arsenallotto.model.BaLottoWinningNumbers;
import com.bbc.arsenallotto.model.BaLottoWinningsTotal;
import com.bbc.arsenallotto.model.BaLottoWinningsSummary;
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
public class BaLottoNotificationsJpaController implements Serializable {

    public BaLottoNotificationsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(BaLottoNotifications baLottoNotifications) {
        if (baLottoNotifications.getBaLottoWinningsSummaryCollection() == null) {
            baLottoNotifications.setBaLottoWinningsSummaryCollection(new ArrayList<BaLottoWinningsSummary>());
        }
        if (baLottoNotifications.getBaLottoWinnersCollection() == null) {
            baLottoNotifications.setBaLottoWinnersCollection(new ArrayList<BaLottoWinners>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BaLottoWinningNumbers baLottoWinningNumbers = baLottoNotifications.getBaLottoWinningNumbers();
            if (baLottoWinningNumbers != null) {
                baLottoWinningNumbers = em.getReference(baLottoWinningNumbers.getClass(), baLottoWinningNumbers.getId());
                baLottoNotifications.setBaLottoWinningNumbers(baLottoWinningNumbers);
            }
            BaLottoWinningsTotal baLottoWinningsTotal = baLottoNotifications.getBaLottoWinningsTotal();
            if (baLottoWinningsTotal != null) {
                baLottoWinningsTotal = em.getReference(baLottoWinningsTotal.getClass(), baLottoWinningsTotal.getId());
                baLottoNotifications.setBaLottoWinningsTotal(baLottoWinningsTotal);
            }
            Collection<BaLottoWinningsSummary> attachedBaLottoWinningsSummaryCollection = new ArrayList<BaLottoWinningsSummary>();
            for (BaLottoWinningsSummary baLottoWinningsSummaryCollectionBaLottoWinningsSummaryToAttach : baLottoNotifications.getBaLottoWinningsSummaryCollection()) {
                baLottoWinningsSummaryCollectionBaLottoWinningsSummaryToAttach = em.getReference(baLottoWinningsSummaryCollectionBaLottoWinningsSummaryToAttach.getClass(), baLottoWinningsSummaryCollectionBaLottoWinningsSummaryToAttach.getId());
                attachedBaLottoWinningsSummaryCollection.add(baLottoWinningsSummaryCollectionBaLottoWinningsSummaryToAttach);
            }
            baLottoNotifications.setBaLottoWinningsSummaryCollection(attachedBaLottoWinningsSummaryCollection);
            Collection<BaLottoWinners> attachedBaLottoWinnersCollection = new ArrayList<BaLottoWinners>();
            for (BaLottoWinners baLottoWinnersCollectionBaLottoWinnersToAttach : baLottoNotifications.getBaLottoWinnersCollection()) {
                baLottoWinnersCollectionBaLottoWinnersToAttach = em.getReference(baLottoWinnersCollectionBaLottoWinnersToAttach.getClass(), baLottoWinnersCollectionBaLottoWinnersToAttach.getId());
                attachedBaLottoWinnersCollection.add(baLottoWinnersCollectionBaLottoWinnersToAttach);
            }
            baLottoNotifications.setBaLottoWinnersCollection(attachedBaLottoWinnersCollection);
            em.persist(baLottoNotifications);
            if (baLottoWinningNumbers != null) {
                BaLottoNotifications oldNotificationIdOfBaLottoWinningNumbers = baLottoWinningNumbers.getNotificationId();
                if (oldNotificationIdOfBaLottoWinningNumbers != null) {
                    oldNotificationIdOfBaLottoWinningNumbers.setBaLottoWinningNumbers(null);
                    oldNotificationIdOfBaLottoWinningNumbers = em.merge(oldNotificationIdOfBaLottoWinningNumbers);
                }
                baLottoWinningNumbers.setNotificationId(baLottoNotifications);
                baLottoWinningNumbers = em.merge(baLottoWinningNumbers);
            }
            if (baLottoWinningsTotal != null) {
                BaLottoNotifications oldNotificationIdOfBaLottoWinningsTotal = baLottoWinningsTotal.getNotificationId();
                if (oldNotificationIdOfBaLottoWinningsTotal != null) {
                    oldNotificationIdOfBaLottoWinningsTotal.setBaLottoWinningsTotal(null);
                    oldNotificationIdOfBaLottoWinningsTotal = em.merge(oldNotificationIdOfBaLottoWinningsTotal);
                }
                baLottoWinningsTotal.setNotificationId(baLottoNotifications);
                baLottoWinningsTotal = em.merge(baLottoWinningsTotal);
            }
            for (BaLottoWinningsSummary baLottoWinningsSummaryCollectionBaLottoWinningsSummary : baLottoNotifications.getBaLottoWinningsSummaryCollection()) {
                BaLottoNotifications oldNotificationIdOfBaLottoWinningsSummaryCollectionBaLottoWinningsSummary = baLottoWinningsSummaryCollectionBaLottoWinningsSummary.getNotificationId();
                baLottoWinningsSummaryCollectionBaLottoWinningsSummary.setNotificationId(baLottoNotifications);
                baLottoWinningsSummaryCollectionBaLottoWinningsSummary = em.merge(baLottoWinningsSummaryCollectionBaLottoWinningsSummary);
                if (oldNotificationIdOfBaLottoWinningsSummaryCollectionBaLottoWinningsSummary != null) {
                    oldNotificationIdOfBaLottoWinningsSummaryCollectionBaLottoWinningsSummary.getBaLottoWinningsSummaryCollection().remove(baLottoWinningsSummaryCollectionBaLottoWinningsSummary);
                    oldNotificationIdOfBaLottoWinningsSummaryCollectionBaLottoWinningsSummary = em.merge(oldNotificationIdOfBaLottoWinningsSummaryCollectionBaLottoWinningsSummary);
                }
            }
            for (BaLottoWinners baLottoWinnersCollectionBaLottoWinners : baLottoNotifications.getBaLottoWinnersCollection()) {
                BaLottoNotifications oldNotificationIdOfBaLottoWinnersCollectionBaLottoWinners = baLottoWinnersCollectionBaLottoWinners.getNotificationId();
                baLottoWinnersCollectionBaLottoWinners.setNotificationId(baLottoNotifications);
                baLottoWinnersCollectionBaLottoWinners = em.merge(baLottoWinnersCollectionBaLottoWinners);
                if (oldNotificationIdOfBaLottoWinnersCollectionBaLottoWinners != null) {
                    oldNotificationIdOfBaLottoWinnersCollectionBaLottoWinners.getBaLottoWinnersCollection().remove(baLottoWinnersCollectionBaLottoWinners);
                    oldNotificationIdOfBaLottoWinnersCollectionBaLottoWinners = em.merge(oldNotificationIdOfBaLottoWinnersCollectionBaLottoWinners);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(BaLottoNotifications baLottoNotifications) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            //em.refresh(baLottoNotifications);
            BaLottoNotifications persistentBaLottoNotifications = em.find(BaLottoNotifications.class, baLottoNotifications.getId());
            BaLottoWinningNumbers baLottoWinningNumbersOld = persistentBaLottoNotifications.getBaLottoWinningNumbers();
            BaLottoWinningNumbers baLottoWinningNumbersNew = baLottoNotifications.getBaLottoWinningNumbers();
            BaLottoWinningsTotal baLottoWinningsTotalOld = persistentBaLottoNotifications.getBaLottoWinningsTotal();
            BaLottoWinningsTotal baLottoWinningsTotalNew = baLottoNotifications.getBaLottoWinningsTotal();
            Collection<BaLottoWinningsSummary> baLottoWinningsSummaryCollectionOld = persistentBaLottoNotifications.getBaLottoWinningsSummaryCollection();
            Collection<BaLottoWinningsSummary> baLottoWinningsSummaryCollectionNew = baLottoNotifications.getBaLottoWinningsSummaryCollection();
            Collection<BaLottoWinners> baLottoWinnersCollectionOld = persistentBaLottoNotifications.getBaLottoWinnersCollection();
            Collection<BaLottoWinners> baLottoWinnersCollectionNew = baLottoNotifications.getBaLottoWinnersCollection();
            List<String> illegalOrphanMessages = null;
            if (baLottoWinningNumbersOld != null && !baLottoWinningNumbersOld.equals(baLottoWinningNumbersNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain BaLottoWinningNumbers " + baLottoWinningNumbersOld + " since its notificationId field is not nullable.");
            }
            if (baLottoWinningsTotalOld != null && !baLottoWinningsTotalOld.equals(baLottoWinningsTotalNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain BaLottoWinningsTotal " + baLottoWinningsTotalOld + " since its notificationId field is not nullable.");
            }
            for (BaLottoWinningsSummary baLottoWinningsSummaryCollectionOldBaLottoWinningsSummary : baLottoWinningsSummaryCollectionOld) {
                if (!baLottoWinningsSummaryCollectionNew.contains(baLottoWinningsSummaryCollectionOldBaLottoWinningsSummary)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain BaLottoWinningsSummary " + baLottoWinningsSummaryCollectionOldBaLottoWinningsSummary + " since its notificationId field is not nullable.");
                }
            }
            for (BaLottoWinners baLottoWinnersCollectionOldBaLottoWinners : baLottoWinnersCollectionOld) {
                if (!baLottoWinnersCollectionNew.contains(baLottoWinnersCollectionOldBaLottoWinners)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain BaLottoWinners " + baLottoWinnersCollectionOldBaLottoWinners + " since its notificationId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (baLottoWinningNumbersNew != null) {
                baLottoWinningNumbersNew = em.getReference(baLottoWinningNumbersNew.getClass(), baLottoWinningNumbersNew.getId());
                baLottoNotifications.setBaLottoWinningNumbers(baLottoWinningNumbersNew);
            }
            if (baLottoWinningsTotalNew != null) {
                baLottoWinningsTotalNew = em.getReference(baLottoWinningsTotalNew.getClass(), baLottoWinningsTotalNew.getId());
                baLottoNotifications.setBaLottoWinningsTotal(baLottoWinningsTotalNew);
            }
            Collection<BaLottoWinningsSummary> attachedBaLottoWinningsSummaryCollectionNew = new ArrayList<BaLottoWinningsSummary>();
            for (BaLottoWinningsSummary baLottoWinningsSummaryCollectionNewBaLottoWinningsSummaryToAttach : baLottoWinningsSummaryCollectionNew) {
                baLottoWinningsSummaryCollectionNewBaLottoWinningsSummaryToAttach = em.getReference(baLottoWinningsSummaryCollectionNewBaLottoWinningsSummaryToAttach.getClass(), baLottoWinningsSummaryCollectionNewBaLottoWinningsSummaryToAttach.getId());
                attachedBaLottoWinningsSummaryCollectionNew.add(baLottoWinningsSummaryCollectionNewBaLottoWinningsSummaryToAttach);
            }
            baLottoWinningsSummaryCollectionNew = attachedBaLottoWinningsSummaryCollectionNew;
            baLottoNotifications.setBaLottoWinningsSummaryCollection(baLottoWinningsSummaryCollectionNew);
            Collection<BaLottoWinners> attachedBaLottoWinnersCollectionNew = new ArrayList<BaLottoWinners>();
            for (BaLottoWinners baLottoWinnersCollectionNewBaLottoWinnersToAttach : baLottoWinnersCollectionNew) {
                baLottoWinnersCollectionNewBaLottoWinnersToAttach = em.getReference(baLottoWinnersCollectionNewBaLottoWinnersToAttach.getClass(), baLottoWinnersCollectionNewBaLottoWinnersToAttach.getId());
                attachedBaLottoWinnersCollectionNew.add(baLottoWinnersCollectionNewBaLottoWinnersToAttach);
            }
            baLottoWinnersCollectionNew = attachedBaLottoWinnersCollectionNew;
            baLottoNotifications.setBaLottoWinnersCollection(baLottoWinnersCollectionNew);
            baLottoNotifications = em.merge(baLottoNotifications);
            if (baLottoWinningNumbersNew != null && !baLottoWinningNumbersNew.equals(baLottoWinningNumbersOld)) {
                BaLottoNotifications oldNotificationIdOfBaLottoWinningNumbers = baLottoWinningNumbersNew.getNotificationId();
                if (oldNotificationIdOfBaLottoWinningNumbers != null) {
                    oldNotificationIdOfBaLottoWinningNumbers.setBaLottoWinningNumbers(null);
                    oldNotificationIdOfBaLottoWinningNumbers = em.merge(oldNotificationIdOfBaLottoWinningNumbers);
                }
                baLottoWinningNumbersNew.setNotificationId(baLottoNotifications);
                baLottoWinningNumbersNew = em.merge(baLottoWinningNumbersNew);
            }
            if (baLottoWinningsTotalNew != null && !baLottoWinningsTotalNew.equals(baLottoWinningsTotalOld)) {
                BaLottoNotifications oldNotificationIdOfBaLottoWinningsTotal = baLottoWinningsTotalNew.getNotificationId();
                if (oldNotificationIdOfBaLottoWinningsTotal != null) {
                    oldNotificationIdOfBaLottoWinningsTotal.setBaLottoWinningsTotal(null);
                    oldNotificationIdOfBaLottoWinningsTotal = em.merge(oldNotificationIdOfBaLottoWinningsTotal);
                }
                baLottoWinningsTotalNew.setNotificationId(baLottoNotifications);
                baLottoWinningsTotalNew = em.merge(baLottoWinningsTotalNew);
            }
            for (BaLottoWinningsSummary baLottoWinningsSummaryCollectionNewBaLottoWinningsSummary : baLottoWinningsSummaryCollectionNew) {
                if (!baLottoWinningsSummaryCollectionOld.contains(baLottoWinningsSummaryCollectionNewBaLottoWinningsSummary)) {
                    BaLottoNotifications oldNotificationIdOfBaLottoWinningsSummaryCollectionNewBaLottoWinningsSummary = baLottoWinningsSummaryCollectionNewBaLottoWinningsSummary.getNotificationId();
                    baLottoWinningsSummaryCollectionNewBaLottoWinningsSummary.setNotificationId(baLottoNotifications);
                    baLottoWinningsSummaryCollectionNewBaLottoWinningsSummary = em.merge(baLottoWinningsSummaryCollectionNewBaLottoWinningsSummary);
                    if (oldNotificationIdOfBaLottoWinningsSummaryCollectionNewBaLottoWinningsSummary != null && !oldNotificationIdOfBaLottoWinningsSummaryCollectionNewBaLottoWinningsSummary.equals(baLottoNotifications)) {
                        oldNotificationIdOfBaLottoWinningsSummaryCollectionNewBaLottoWinningsSummary.getBaLottoWinningsSummaryCollection().remove(baLottoWinningsSummaryCollectionNewBaLottoWinningsSummary);
                        oldNotificationIdOfBaLottoWinningsSummaryCollectionNewBaLottoWinningsSummary = em.merge(oldNotificationIdOfBaLottoWinningsSummaryCollectionNewBaLottoWinningsSummary);
                    }
                }
            }
            for (BaLottoWinners baLottoWinnersCollectionNewBaLottoWinners : baLottoWinnersCollectionNew) {
                if (!baLottoWinnersCollectionOld.contains(baLottoWinnersCollectionNewBaLottoWinners)) {
                    BaLottoNotifications oldNotificationIdOfBaLottoWinnersCollectionNewBaLottoWinners = baLottoWinnersCollectionNewBaLottoWinners.getNotificationId();
                    baLottoWinnersCollectionNewBaLottoWinners.setNotificationId(baLottoNotifications);
                    baLottoWinnersCollectionNewBaLottoWinners = em.merge(baLottoWinnersCollectionNewBaLottoWinners);
                    if (oldNotificationIdOfBaLottoWinnersCollectionNewBaLottoWinners != null && !oldNotificationIdOfBaLottoWinnersCollectionNewBaLottoWinners.equals(baLottoNotifications)) {
                        oldNotificationIdOfBaLottoWinnersCollectionNewBaLottoWinners.getBaLottoWinnersCollection().remove(baLottoWinnersCollectionNewBaLottoWinners);
                        oldNotificationIdOfBaLottoWinnersCollectionNewBaLottoWinners = em.merge(oldNotificationIdOfBaLottoWinnersCollectionNewBaLottoWinners);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = baLottoNotifications.getId();
                if (findBaLottoNotifications(id) == null) {
                    throw new NonexistentEntityException("The baLottoNotifications with id " + id + " no longer exists.");
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
            BaLottoNotifications baLottoNotifications;
            try {
                baLottoNotifications = em.getReference(BaLottoNotifications.class, id);
                baLottoNotifications.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The baLottoNotifications with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            BaLottoWinningNumbers baLottoWinningNumbersOrphanCheck = baLottoNotifications.getBaLottoWinningNumbers();
            if (baLottoWinningNumbersOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This BaLottoNotifications (" + baLottoNotifications + ") cannot be destroyed since the BaLottoWinningNumbers " + baLottoWinningNumbersOrphanCheck + " in its baLottoWinningNumbers field has a non-nullable notificationId field.");
            }
            BaLottoWinningsTotal baLottoWinningsTotalOrphanCheck = baLottoNotifications.getBaLottoWinningsTotal();
            if (baLottoWinningsTotalOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This BaLottoNotifications (" + baLottoNotifications + ") cannot be destroyed since the BaLottoWinningsTotal " + baLottoWinningsTotalOrphanCheck + " in its baLottoWinningsTotal field has a non-nullable notificationId field.");
            }
            Collection<BaLottoWinningsSummary> baLottoWinningsSummaryCollectionOrphanCheck = baLottoNotifications.getBaLottoWinningsSummaryCollection();
            for (BaLottoWinningsSummary baLottoWinningsSummaryCollectionOrphanCheckBaLottoWinningsSummary : baLottoWinningsSummaryCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This BaLottoNotifications (" + baLottoNotifications + ") cannot be destroyed since the BaLottoWinningsSummary " + baLottoWinningsSummaryCollectionOrphanCheckBaLottoWinningsSummary + " in its baLottoWinningsSummaryCollection field has a non-nullable notificationId field.");
            }
            Collection<BaLottoWinners> baLottoWinnersCollectionOrphanCheck = baLottoNotifications.getBaLottoWinnersCollection();
            for (BaLottoWinners baLottoWinnersCollectionOrphanCheckBaLottoWinners : baLottoWinnersCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This BaLottoNotifications (" + baLottoNotifications + ") cannot be destroyed since the BaLottoWinners " + baLottoWinnersCollectionOrphanCheckBaLottoWinners + " in its baLottoWinnersCollection field has a non-nullable notificationId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(baLottoNotifications);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<BaLottoNotifications> findBaLottoNotificationsEntities() {
        return findBaLottoNotificationsEntities(true, -1, -1);
    }

    public List<BaLottoNotifications> findBaLottoNotificationsEntities(int maxResults, int firstResult) {
        return findBaLottoNotificationsEntities(false, maxResults, firstResult);
    }

    private List<BaLottoNotifications> findBaLottoNotificationsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(BaLottoNotifications.class));
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

    public BaLottoNotifications findBaLottoNotifications(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(BaLottoNotifications.class, id);
        } finally {
            em.close();
        }
    }

    public int getBaLottoNotificationsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<BaLottoNotifications> rt = cq.from(BaLottoNotifications.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public List<BaLottoNotifications> findQueuedNotifications() {
        List<BaLottoNotifications> queuedLottoNotifications = null;

        EntityManager em = getEntityManager();
        TypedQuery<BaLottoNotifications> queuedNotifications = em.createNamedQuery("BaLottoNotifications.findByProcessed", BaLottoNotifications.class);
//set dynamic data for query
        queuedNotifications.setParameter("processed", false);

        try {
//execute query and get results
            queuedLottoNotifications = queuedNotifications.getResultList();
        } catch (NoResultException nre) {
            queuedLottoNotifications = null;
            BBCALLogger.getLogger().log(Level.INFO, null, nre);
        } finally {
            em.close();
        }

        return queuedLottoNotifications;
    }
    
    public void markProcessed(Integer notificationId) {
        EntityManager em = getEntityManager();
        TypedQuery<BaLottoNotifications> notificationsQuery = em.createNamedQuery("BaLottoNotifications.markProcessed", BaLottoNotifications.class);
//set dynamic data for query
        notificationsQuery.setParameter("id", notificationId);
        try {
            em.getTransaction().begin();
//execute query and get results
            notificationsQuery.executeUpdate();
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
