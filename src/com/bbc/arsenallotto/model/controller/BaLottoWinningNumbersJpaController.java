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
import com.bbc.arsenallotto.model.BaLottoWinningNumbers;
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
public class BaLottoWinningNumbersJpaController implements Serializable {

    public BaLottoWinningNumbersJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(BaLottoWinningNumbers baLottoWinningNumbers) throws IllegalOrphanException {
        List<String> illegalOrphanMessages = null;
        BaLottoNotifications notificationIdOrphanCheck = baLottoWinningNumbers.getNotificationId();
        if (notificationIdOrphanCheck != null) {
            BaLottoWinningNumbers oldBaLottoWinningNumbersOfNotificationId = notificationIdOrphanCheck.getBaLottoWinningNumbers();
            if (oldBaLottoWinningNumbersOfNotificationId != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The BaLottoNotifications " + notificationIdOrphanCheck + " already has an item of type BaLottoWinningNumbers whose notificationId column cannot be null. Please make another selection for the notificationId field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BaLottoNotifications notificationId = baLottoWinningNumbers.getNotificationId();
            if (notificationId != null) {
                notificationId = em.getReference(notificationId.getClass(), notificationId.getId());
                baLottoWinningNumbers.setNotificationId(notificationId);
            }
            em.persist(baLottoWinningNumbers);
            if (notificationId != null) {
                notificationId.setBaLottoWinningNumbers(baLottoWinningNumbers);
                notificationId = em.merge(notificationId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(BaLottoWinningNumbers baLottoWinningNumbers) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BaLottoWinningNumbers persistentBaLottoWinningNumbers = em.find(BaLottoWinningNumbers.class, baLottoWinningNumbers.getId());
            BaLottoNotifications notificationIdOld = persistentBaLottoWinningNumbers.getNotificationId();
            BaLottoNotifications notificationIdNew = baLottoWinningNumbers.getNotificationId();
            List<String> illegalOrphanMessages = null;
            if (notificationIdNew != null && !notificationIdNew.equals(notificationIdOld)) {
                BaLottoWinningNumbers oldBaLottoWinningNumbersOfNotificationId = notificationIdNew.getBaLottoWinningNumbers();
                if (oldBaLottoWinningNumbersOfNotificationId != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The BaLottoNotifications " + notificationIdNew + " already has an item of type BaLottoWinningNumbers whose notificationId column cannot be null. Please make another selection for the notificationId field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (notificationIdNew != null) {
                notificationIdNew = em.getReference(notificationIdNew.getClass(), notificationIdNew.getId());
                baLottoWinningNumbers.setNotificationId(notificationIdNew);
            }
            baLottoWinningNumbers = em.merge(baLottoWinningNumbers);
            if (notificationIdOld != null && !notificationIdOld.equals(notificationIdNew)) {
                notificationIdOld.setBaLottoWinningNumbers(null);
                notificationIdOld = em.merge(notificationIdOld);
            }
            if (notificationIdNew != null && !notificationIdNew.equals(notificationIdOld)) {
                notificationIdNew.setBaLottoWinningNumbers(baLottoWinningNumbers);
                notificationIdNew = em.merge(notificationIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = baLottoWinningNumbers.getId();
                if (findBaLottoWinningNumbers(id) == null) {
                    throw new NonexistentEntityException("The baLottoWinningNumbers with id " + id + " no longer exists.");
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
            BaLottoWinningNumbers baLottoWinningNumbers;
            try {
                baLottoWinningNumbers = em.getReference(BaLottoWinningNumbers.class, id);
                baLottoWinningNumbers.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The baLottoWinningNumbers with id " + id + " no longer exists.", enfe);
            }
            BaLottoNotifications notificationId = baLottoWinningNumbers.getNotificationId();
            if (notificationId != null) {
                notificationId.setBaLottoWinningNumbers(null);
                notificationId = em.merge(notificationId);
            }
            em.remove(baLottoWinningNumbers);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<BaLottoWinningNumbers> findBaLottoWinningNumbersEntities() {
        return findBaLottoWinningNumbersEntities(true, -1, -1);
    }

    public List<BaLottoWinningNumbers> findBaLottoWinningNumbersEntities(int maxResults, int firstResult) {
        return findBaLottoWinningNumbersEntities(false, maxResults, firstResult);
    }

    private List<BaLottoWinningNumbers> findBaLottoWinningNumbersEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(BaLottoWinningNumbers.class));
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

    public BaLottoWinningNumbers findBaLottoWinningNumbers(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(BaLottoWinningNumbers.class, id);
        } finally {
            em.close();
        }
    }

    public int getBaLottoWinningNumbersCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<BaLottoWinningNumbers> rt = cq.from(BaLottoWinningNumbers.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public BaLottoWinningNumbers findByNotificationId(Integer notificationId) {
        BaLottoWinningNumbers winningNos = null;

        EntityManager em = getEntityManager();
        TypedQuery<BaLottoWinningNumbers> winningNosQuery = em.createNamedQuery("BaLottoWinningNumbers.findByNotification", BaLottoWinningNumbers.class);
//set dynamic data for query
        winningNosQuery.setParameter("notificationId", notificationId);

        try {
//execute query and get results
            winningNos = winningNosQuery.getSingleResult();
        } catch (NoResultException nre) {
            winningNos = null;
        } finally {
            em.close();
        }

        return winningNos;
    }

}
