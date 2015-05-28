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
import com.bbc.arsenallotto.model.BaLottoWinningsSummary;
import com.bbc.arsenallotto.model.controller.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

/**
 *
 * @author Soul
 */
public class BaLottoWinningsSummaryJpaController implements Serializable {

    public BaLottoWinningsSummaryJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(BaLottoWinningsSummary baLottoWinningsSummary) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BaLottoNotifications notificationId = baLottoWinningsSummary.getNotificationId();
            if (notificationId != null) {
                notificationId = em.getReference(notificationId.getClass(), notificationId.getId());
                baLottoWinningsSummary.setNotificationId(notificationId);
            }
            em.persist(baLottoWinningsSummary);
            if (notificationId != null) {
                notificationId.getBaLottoWinningsSummaryCollection().add(baLottoWinningsSummary);
                notificationId = em.merge(notificationId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(BaLottoWinningsSummary baLottoWinningsSummary) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BaLottoWinningsSummary persistentBaLottoWinningsSummary = em.find(BaLottoWinningsSummary.class, baLottoWinningsSummary.getId());
            BaLottoNotifications notificationIdOld = persistentBaLottoWinningsSummary.getNotificationId();
            BaLottoNotifications notificationIdNew = baLottoWinningsSummary.getNotificationId();
            if (notificationIdNew != null) {
                notificationIdNew = em.getReference(notificationIdNew.getClass(), notificationIdNew.getId());
                baLottoWinningsSummary.setNotificationId(notificationIdNew);
            }
            baLottoWinningsSummary = em.merge(baLottoWinningsSummary);
            if (notificationIdOld != null && !notificationIdOld.equals(notificationIdNew)) {
                notificationIdOld.getBaLottoWinningsSummaryCollection().remove(baLottoWinningsSummary);
                notificationIdOld = em.merge(notificationIdOld);
            }
            if (notificationIdNew != null && !notificationIdNew.equals(notificationIdOld)) {
                notificationIdNew.getBaLottoWinningsSummaryCollection().add(baLottoWinningsSummary);
                notificationIdNew = em.merge(notificationIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = baLottoWinningsSummary.getId();
                if (findBaLottoWinningsSummary(id) == null) {
                    throw new NonexistentEntityException("The baLottoWinningsSummary with id " + id + " no longer exists.");
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
            BaLottoWinningsSummary baLottoWinningsSummary;
            try {
                baLottoWinningsSummary = em.getReference(BaLottoWinningsSummary.class, id);
                baLottoWinningsSummary.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The baLottoWinningsSummary with id " + id + " no longer exists.", enfe);
            }
            BaLottoNotifications notificationId = baLottoWinningsSummary.getNotificationId();
            if (notificationId != null) {
                notificationId.getBaLottoWinningsSummaryCollection().remove(baLottoWinningsSummary);
                notificationId = em.merge(notificationId);
            }
            em.remove(baLottoWinningsSummary);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<BaLottoWinningsSummary> findBaLottoWinningsSummaryEntities() {
        return findBaLottoWinningsSummaryEntities(true, -1, -1);
    }

    public List<BaLottoWinningsSummary> findBaLottoWinningsSummaryEntities(int maxResults, int firstResult) {
        return findBaLottoWinningsSummaryEntities(false, maxResults, firstResult);
    }

    private List<BaLottoWinningsSummary> findBaLottoWinningsSummaryEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(BaLottoWinningsSummary.class));
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

    public BaLottoWinningsSummary findBaLottoWinningsSummary(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(BaLottoWinningsSummary.class, id);
        } finally {
            em.close();
        }
    }

    public int getBaLottoWinningsSummaryCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<BaLottoWinningsSummary> rt = cq.from(BaLottoWinningsSummary.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public void destroyByNotificationId(Integer notificationId) {
        EntityManager em = getEntityManager();
        TypedQuery<BaLottoWinningsSummary> winningsSummaryQuery = em.createNamedQuery("BaLottoWinningsSummary.destroyByNotification", BaLottoWinningsSummary.class);
//set dynamic data for query
        winningsSummaryQuery.setParameter("notificationId", notificationId);
        try {
            em.getTransaction().begin();
//execute query and get results
            winningsSummaryQuery.executeUpdate();
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
