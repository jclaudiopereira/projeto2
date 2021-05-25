/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaocinema.BLL;

import gestaocinema.BLL.exceptions.NonexistentEntityException;
import gestaocinema.DAL.Filme;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import gestaocinema.DAL.Sessao;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author joao_
 */
public class FilmeJpaController implements Serializable {

    public FilmeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Filme filme) {
        if (filme.getSessaoList() == null) {
            filme.setSessaoList(new ArrayList<Sessao>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Sessao> attachedSessaoList = new ArrayList<Sessao>();
            for (Sessao sessaoListSessaoToAttach : filme.getSessaoList()) {
                sessaoListSessaoToAttach = em.getReference(sessaoListSessaoToAttach.getClass(), sessaoListSessaoToAttach.getIdSessao());
                attachedSessaoList.add(sessaoListSessaoToAttach);
            }
            filme.setSessaoList(attachedSessaoList);
            em.persist(filme);
            for (Sessao sessaoListSessao : filme.getSessaoList()) {
                Filme oldFilmeOfSessaoListSessao = sessaoListSessao.getFilme();
                sessaoListSessao.setFilme(filme);
                sessaoListSessao = em.merge(sessaoListSessao);
                if (oldFilmeOfSessaoListSessao != null) {
                    oldFilmeOfSessaoListSessao.getSessaoList().remove(sessaoListSessao);
                    oldFilmeOfSessaoListSessao = em.merge(oldFilmeOfSessaoListSessao);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Filme filme) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Filme persistentFilme = em.find(Filme.class, filme.getIdFilme());
            List<Sessao> sessaoListOld = persistentFilme.getSessaoList();
            List<Sessao> sessaoListNew = filme.getSessaoList();
            List<Sessao> attachedSessaoListNew = new ArrayList<Sessao>();
            for (Sessao sessaoListNewSessaoToAttach : sessaoListNew) {
                sessaoListNewSessaoToAttach = em.getReference(sessaoListNewSessaoToAttach.getClass(), sessaoListNewSessaoToAttach.getIdSessao());
                attachedSessaoListNew.add(sessaoListNewSessaoToAttach);
            }
            sessaoListNew = attachedSessaoListNew;
            filme.setSessaoList(sessaoListNew);
            filme = em.merge(filme);
            for (Sessao sessaoListOldSessao : sessaoListOld) {
                if (!sessaoListNew.contains(sessaoListOldSessao)) {
                    sessaoListOldSessao.setFilme(null);
                    sessaoListOldSessao = em.merge(sessaoListOldSessao);
                }
            }
            for (Sessao sessaoListNewSessao : sessaoListNew) {
                if (!sessaoListOld.contains(sessaoListNewSessao)) {
                    Filme oldFilmeOfSessaoListNewSessao = sessaoListNewSessao.getFilme();
                    sessaoListNewSessao.setFilme(filme);
                    sessaoListNewSessao = em.merge(sessaoListNewSessao);
                    if (oldFilmeOfSessaoListNewSessao != null && !oldFilmeOfSessaoListNewSessao.equals(filme)) {
                        oldFilmeOfSessaoListNewSessao.getSessaoList().remove(sessaoListNewSessao);
                        oldFilmeOfSessaoListNewSessao = em.merge(oldFilmeOfSessaoListNewSessao);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = filme.getIdFilme();
                if (findFilme(id) == null) {
                    throw new NonexistentEntityException("The filme with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(BigDecimal id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Filme filme;
            try {
                filme = em.getReference(Filme.class, id);
                filme.getIdFilme();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The filme with id " + id + " no longer exists.", enfe);
            }
            List<Sessao> sessaoList = filme.getSessaoList();
            for (Sessao sessaoListSessao : sessaoList) {
                sessaoListSessao.setFilme(null);
                sessaoListSessao = em.merge(sessaoListSessao);
            }
            em.remove(filme);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Filme> findFilmeEntities() {
        return findFilmeEntities(true, -1, -1);
    }

    public List<Filme> findFilmeEntities(int maxResults, int firstResult) {
        return findFilmeEntities(false, maxResults, firstResult);
    }

    private List<Filme> findFilmeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Filme.class));
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

    public Filme findFilme(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Filme.class, id);
        } finally {
            em.close();
        }
    }

    public int getFilmeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Filme> rt = cq.from(Filme.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
