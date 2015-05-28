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
import com.bbc.arsenallotto.model.BaLottoEntry;
import com.bbc.arsenallotto.model.BaLottoWinners;
import com.bbc.arsenallotto.model.controller.exceptions.NonexistentEntityException;
import com.bbc.arsenallotto.service.BBCALLogger;
import java.util.List;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

/**
 *
 * @author Soul
 */
public class BaLottoWinnersJpaController implements Serializable {

    public BaLottoWinnersJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(BaLottoWinners baLottoWinners) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BaLottoNotifications notificationId = baLottoWinners.getNotificationId();
            if (notificationId != null) {
                notificationId = em.getReference(notificationId.getClass(), notificationId.getId());
                baLottoWinners.setNotificationId(notificationId);
            }
            BaLottoEntry lottoReference = baLottoWinners.getLottoReference();
            if (lottoReference != null) {
                lottoReference = em.getReference(lottoReference.getClass(), lottoReference.getId());
                baLottoWinners.setLottoReference(lottoReference);
            }
            em.persist(baLottoWinners);
            if (notificationId != null) {
                notificationId.getBaLottoWinnersCollection().add(baLottoWinners);
                notificationId = em.merge(notificationId);
            }
            if (lottoReference != null) {
                lottoReference.getBaLottoWinnersCollection().add(baLottoWinners);
                lottoReference = em.merge(lottoReference);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            BBCALLogger.getLogger().log(Level.INFO, "OK. Cant Insert Winner :: " + e.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(BaLottoWinners baLottoWinners) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BaLottoWinners persistentBaLottoWinners = em.find(BaLottoWinners.class, baLottoWinners.getId());
            BaLottoNotifications notificationIdOld = persistentBaLottoWinners.getNotificationId();
            BaLottoNotifications notificationIdNew = baLottoWinners.getNotificationId();
            BaLottoEntry lottoReferenceOld = persistentBaLottoWinners.getLottoReference();
            BaLottoEntry lottoReferenceNew = baLottoWinners.getLottoReference();
            if (notificationIdNew != null) {
                notificationIdNew = em.getReference(notificationIdNew.getClass(), notificationIdNew.getId());
                baLottoWinners.setNotificationId(notificationIdNew);
            }
            if (lottoReferenceNew != null) {
                lottoReferenceNew = em.getReference(lottoReferenceNew.getClass(), lottoReferenceNew.getId());
                baLottoWinners.setLottoReference(lottoReferenceNew);
            }
            baLottoWinners = em.merge(baLottoWinners);
            if (notificationIdOld != null && !notificationIdOld.equals(notificationIdNew)) {
                notificationIdOld.getBaLottoWinnersCollection().remove(baLottoWinners);
                notificationIdOld = em.merge(notificationIdOld);
            }
            if (notificationIdNew != null && !notificationIdNew.equals(notificationIdOld)) {
                notificationIdNew.getBaLottoWinnersCollection().add(baLottoWinners);
                notificationIdNew = em.merge(notificationIdNew);
            }
            if (lottoReferenceOld != null && !lottoReferenceOld.equals(lottoReferenceNew)) {
                lottoReferenceOld.getBaLottoWinnersCollection().remove(baLottoWinners);
                lottoReferenceOld = em.merge(lottoReferenceOld);
            }
            if (lottoReferenceNew != null && !lottoReferenceNew.equals(lottoReferenceOld)) {
                lottoReferenceNew.getBaLottoWinnersCollection().add(baLottoWinners);
                lottoReferenceNew = em.merge(lottoReferenceNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = baLottoWinners.getId();
                if (findBaLottoWinners(id) == null) {
                    throw new NonexistentEntityException("The baLottoWinners with id " + id + " no longer exists.");
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
            BaLottoWinners baLottoWinners;
            try {
                baLottoWinners = em.getReference(BaLottoWinners.class, id);
                baLottoWinners.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The baLottoWinners with id " + id + " no longer exists.", enfe);
            }
            BaLottoNotifications notificationId = baLottoWinners.getNotificationId();
            if (notificationId != null) {
                notificationId.getBaLottoWinnersCollection().remove(baLottoWinners);
                notificationId = em.merge(notificationId);
            }
            BaLottoEntry lottoReference = baLottoWinners.getLottoReference();
            if (lottoReference != null) {
                lottoReference.getBaLottoWinnersCollection().remove(baLottoWinners);
                lottoReference = em.merge(lottoReference);
            }
            em.remove(baLottoWinners);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<BaLottoWinners> findBaLottoWinnersEntities() {
        return findBaLottoWinnersEntities(true, -1, -1);
    }

    public List<BaLottoWinners> findBaLottoWinnersEntities(int maxResults, int firstResult) {
        return findBaLottoWinnersEntities(false, maxResults, firstResult);
    }

    private List<BaLottoWinners> findBaLottoWinnersEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(BaLottoWinners.class));
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

    public BaLottoWinners findBaLottoWinners(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(BaLottoWinners.class, id);
        } finally {
            em.close();
        }
    }

    public int getBaLottoWinnersCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<BaLottoWinners> rt = cq.from(BaLottoWinners.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public void destroyByNotificationId(Integer notificationId) {
        EntityManager em = getEntityManager();
        TypedQuery<BaLottoWinners> winnersQuery = em.createNamedQuery("BaLottoWinners.destroyByNotification", BaLottoWinners.class);
//set dynamic data for query
        winnersQuery.setParameter("notificationId", notificationId);
        try {
            em.getTransaction().begin();
//execute query and get results
            winnersQuery.executeUpdate();
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
    
}
