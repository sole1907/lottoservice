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
import com.bbc.arsenallotto.model.BaLottoNotifications;
import com.bbc.arsenallotto.model.BaLottoWinningsTotal;
import com.bbc.arsenallotto.model.controller.exceptions.IllegalOrphanException;
import com.bbc.arsenallotto.model.controller.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

/**
 *
 * @author Soul
 */
public class BaLottoWinningsTotalJpaController implements Serializable {

    public BaLottoWinningsTotalJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(BaLottoWinningsTotal baLottoWinningsTotal) throws IllegalOrphanException {
        List<String> illegalOrphanMessages = null;
        BaLottoNotifications notificationIdOrphanCheck = baLottoWinningsTotal.getNotificationId();
        if (notificationIdOrphanCheck != null) {
            BaLottoWinningsTotal oldBaLottoWinningsTotalOfNotificationId = notificationIdOrphanCheck.getBaLottoWinningsTotal();
            if (oldBaLottoWinningsTotalOfNotificationId != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The BaLottoNotifications " + notificationIdOrphanCheck + " already has an item of type BaLottoWinningsTotal whose notificationId column cannot be null. Please make another selection for the notificationId field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BaLottoNotifications notificationId = baLottoWinningsTotal.getNotificationId();
            if (notificationId != null) {
                notificationId = em.getReference(notificationId.getClass(), notificationId.getId());
                baLottoWinningsTotal.setNotificationId(notificationId);
            }
            em.persist(baLottoWinningsTotal);
            if (notificationId != null) {
                notificationId.setBaLottoWinningsTotal(baLottoWinningsTotal);
                notificationId = em.merge(notificationId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(BaLottoWinningsTotal baLottoWinningsTotal) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BaLottoWinningsTotal persistentBaLottoWinningsTotal = em.find(BaLottoWinningsTotal.class, baLottoWinningsTotal.getId());
            BaLottoNotifications notificationIdOld = persistentBaLottoWinningsTotal.getNotificationId();
            BaLottoNotifications notificationIdNew = baLottoWinningsTotal.getNotificationId();
            List<String> illegalOrphanMessages = null;
            if (notificationIdNew != null && !notificationIdNew.equals(notificationIdOld)) {
                BaLottoWinningsTotal oldBaLottoWinningsTotalOfNotificationId = notificationIdNew.getBaLottoWinningsTotal();
                if (oldBaLottoWinningsTotalOfNotificationId != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The BaLottoNotifications " + notificationIdNew + " already has an item of type BaLottoWinningsTotal whose notificationId column cannot be null. Please make another selection for the notificationId field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (notificationIdNew != null) {
                notificationIdNew = em.getReference(notificationIdNew.getClass(), notificationIdNew.getId());
                baLottoWinningsTotal.setNotificationId(notificationIdNew);
            }
            baLottoWinningsTotal = em.merge(baLottoWinningsTotal);
            if (notificationIdOld != null && !notificationIdOld.equals(notificationIdNew)) {
                notificationIdOld.setBaLottoWinningsTotal(null);
                notificationIdOld = em.merge(notificationIdOld);
            }
            if (notificationIdNew != null && !notificationIdNew.equals(notificationIdOld)) {
                notificationIdNew.setBaLottoWinningsTotal(baLottoWinningsTotal);
                notificationIdNew = em.merge(notificationIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = baLottoWinningsTotal.getId();
                if (findBaLottoWinningsTotal(id) == null) {
                    throw new NonexistentEntityException("The baLottoWinningsTotal with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BaLottoWinningsTotal baLottoWinningsTotal;
            try {
                baLottoWinningsTotal = em.getReference(BaLottoWinningsTotal.class, id);
                baLottoWinningsTotal.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The baLottoWinningsTotal with id " + id + " no longer exists.", enfe);
            }
            BaLottoNotifications notificationId = baLottoWinningsTotal.getNotificationId();
            if (notificationId != null) {
                notificationId.setBaLottoWinningsTotal(null);
                notificationId = em.merge(notificationId);
            }
            em.remove(baLottoWinningsTotal);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<BaLottoWinningsTotal> findBaLottoWinningsTotalEntities() {
        return findBaLottoWinningsTotalEntities(true, -1, -1);
    }

    public List<BaLottoWinningsTotal> findBaLottoWinningsTotalEntities(int maxResults, int firstResult) {
        return findBaLottoWinningsTotalEntities(false, maxResults, firstResult);
    }

    private List<BaLottoWinningsTotal> findBaLottoWinningsTotalEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(BaLottoWinningsTotal.class));
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

    public BaLottoWinningsTotal findBaLottoWinningsTotal(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(BaLottoWinningsTotal.class, id);
        } finally {
            em.close();
        }
    }

    public int getBaLottoWinningsTotalCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<BaLottoWinningsTotal> rt = cq.from(BaLottoWinningsTotal.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public BaLottoWinningsTotal findByNotificationId(Integer notificationId) {
        BaLottoWinningsTotal winningsTotal = null;

        EntityManager em = getEntityManager();
        TypedQuery<BaLottoWinningsTotal> winningsTotalQuery = em.createNamedQuery("BaLottoWinningsTotal.findByNotification", BaLottoWinningsTotal.class);
//set dynamic data for query
        winningsTotalQuery.setParameter("notificationId", notificationId);

        try {
//execute query and get results
            winningsTotal = winningsTotalQuery.getSingleResult();
        } catch (NoResultException nre) {
            winningsTotal = null;
        } finally {
            em.close();
        }

        return winningsTotal;
    }

}
