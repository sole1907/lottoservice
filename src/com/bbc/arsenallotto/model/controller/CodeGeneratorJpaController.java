/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bbc.arsenallotto.model.controller;

import com.bbc.arsenallotto.model.CodeGenerator;
import com.bbc.arsenallotto.model.controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Soul
 */
public class CodeGeneratorJpaController implements Serializable {

    public CodeGeneratorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CodeGenerator codeGenerator) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(codeGenerator);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CodeGenerator codeGenerator) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            codeGenerator = em.merge(codeGenerator);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = codeGenerator.getTid();
                if (findCodeGenerator(id) == null) {
                    throw new NonexistentEntityException("The codeGenerator with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CodeGenerator codeGenerator;
            try {
                codeGenerator = em.getReference(CodeGenerator.class, id);
                codeGenerator.getTid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The codeGenerator with id " + id + " no longer exists.", enfe);
            }
            em.remove(codeGenerator);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CodeGenerator> findCodeGeneratorEntities() {
        return findCodeGeneratorEntities(true, -1, -1);
    }

    public List<CodeGenerator> findCodeGeneratorEntities(int maxResults, int firstResult) {
        return findCodeGeneratorEntities(false, maxResults, firstResult);
    }

    private List<CodeGenerator> findCodeGeneratorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CodeGenerator.class));
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

    public CodeGenerator findCodeGenerator(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CodeGenerator.class, id);
        } finally {
            em.close();
        }
    }

    public int getCodeGeneratorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CodeGenerator> rt = cq.from(CodeGenerator.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
