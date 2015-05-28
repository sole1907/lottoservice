/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bbc.arsenallotto.model.controller;

import com.bbc.arsenallotto.model.BaLottoConfig;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.bbc.arsenallotto.model.Sts;
import com.bbc.arsenallotto.model.Prle;
import com.bbc.arsenallotto.model.controller.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Soul
 */
public class BaLottoConfigJpaController implements Serializable {

    public BaLottoConfigJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(BaLottoConfig baLottoConfig) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Sts sts = baLottoConfig.getSts();
            if (sts != null) {
                sts = em.getReference(sts.getClass(), sts.getTid());
                baLottoConfig.setSts(sts);
            }
            Prle CByy = baLottoConfig.getCByy();
            if (CByy != null) {
                CByy = em.getReference(CByy.getClass(), CByy.getTid());
                baLottoConfig.setCByy(CByy);
            }
            em.persist(baLottoConfig);
            if (sts != null) {
                sts.getBaLottoConfigCollection().add(baLottoConfig);
                sts = em.merge(sts);
            }
            if (CByy != null) {
                CByy.getBaLottoConfigCollection().add(baLottoConfig);
                CByy = em.merge(CByy);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(BaLottoConfig baLottoConfig) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BaLottoConfig persistentBaLottoConfig = em.find(BaLottoConfig.class, baLottoConfig.getId());
            Sts stsOld = persistentBaLottoConfig.getSts();
            Sts stsNew = baLottoConfig.getSts();
            Prle CByyOld = persistentBaLottoConfig.getCByy();
            Prle CByyNew = baLottoConfig.getCByy();
            if (stsNew != null) {
                stsNew = em.getReference(stsNew.getClass(), stsNew.getTid());
                baLottoConfig.setSts(stsNew);
            }
            if (CByyNew != null) {
                CByyNew = em.getReference(CByyNew.getClass(), CByyNew.getTid());
                baLottoConfig.setCByy(CByyNew);
            }
            baLottoConfig = em.merge(baLottoConfig);
            if (stsOld != null && !stsOld.equals(stsNew)) {
                stsOld.getBaLottoConfigCollection().remove(baLottoConfig);
                stsOld = em.merge(stsOld);
            }
            if (stsNew != null && !stsNew.equals(stsOld)) {
                stsNew.getBaLottoConfigCollection().add(baLottoConfig);
                stsNew = em.merge(stsNew);
            }
            if (CByyOld != null && !CByyOld.equals(CByyNew)) {
                CByyOld.getBaLottoConfigCollection().remove(baLottoConfig);
                CByyOld = em.merge(CByyOld);
            }
            if (CByyNew != null && !CByyNew.equals(CByyOld)) {
                CByyNew.getBaLottoConfigCollection().add(baLottoConfig);
                CByyNew = em.merge(CByyNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = baLottoConfig.getId();
                if (findBaLottoConfig(id) == null) {
                    throw new NonexistentEntityException("The baLottoConfig with id " + id + " no longer exists.");
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
            BaLottoConfig baLottoConfig;
            try {
                baLottoConfig = em.getReference(BaLottoConfig.class, id);
                baLottoConfig.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The baLottoConfig with id " + id + " no longer exists.", enfe);
            }
            Sts sts = baLottoConfig.getSts();
            if (sts != null) {
                sts.getBaLottoConfigCollection().remove(baLottoConfig);
                sts = em.merge(sts);
            }
            Prle CByy = baLottoConfig.getCByy();
            if (CByy != null) {
                CByy.getBaLottoConfigCollection().remove(baLottoConfig);
                CByy = em.merge(CByy);
            }
            em.remove(baLottoConfig);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<BaLottoConfig> findBaLottoConfigEntities() {
        return findBaLottoConfigEntities(true, -1, -1);
    }

    public List<BaLottoConfig> findBaLottoConfigEntities(int maxResults, int firstResult) {
        return findBaLottoConfigEntities(false, maxResults, firstResult);
    }

    private List<BaLottoConfig> findBaLottoConfigEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(BaLottoConfig.class));
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

    public BaLottoConfig findBaLottoConfig(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(BaLottoConfig.class, id);
        } finally {
            em.close();
        }
    }

    public int getBaLottoConfigCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<BaLottoConfig> rt = cq.from(BaLottoConfig.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
