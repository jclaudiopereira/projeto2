/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaocinema.BLL;

import gestaocinema.BLL.exceptions.IllegalOrphanException;
import gestaocinema.BLL.exceptions.NonexistentEntityException;
import gestaocinema.DAL.Sala;
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
public class SalaJpaController implements Serializable {

    public SalaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Sala sala) {
        if (sala.getSessaoList() == null) {
            sala.setSessaoList(new ArrayList<Sessao>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Sessao> attachedSessaoList = new ArrayList<Sessao>();
            for (Sessao sessaoListSessaoToAttach : sala.getSessaoList()) {
                sessaoListSessaoToAttach = em.getReference(sessaoListSessaoToAttach.getClass(), sessaoListSessaoToAttach.getIdSessao());
                attachedSessaoList.add(sessaoListSessaoToAttach);
            }
            sala.setSessaoList(attachedSessaoList);
            em.persist(sala);
            for (Sessao sessaoListSessao : sala.getSessaoList()) {
                Sala oldSalaOfSessaoListSessao = sessaoListSessao.getSala();
                sessaoListSessao.setSala(sala);
                sessaoListSessao = em.merge(sessaoListSessao);
                if (oldSalaOfSessaoListSessao != null) {
                    oldSalaOfSessaoListSessao.getSessaoList().remove(sessaoListSessao);
                    oldSalaOfSessaoListSessao = em.merge(oldSalaOfSessaoListSessao);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Sala sala) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Sala persistentSala = em.find(Sala.class, sala.getIdSala());
            List<Sessao> sessaoListOld = persistentSala.getSessaoList();
            List<Sessao> sessaoListNew = sala.getSessaoList();
            List<String> illegalOrphanMessages = null;
            for (Sessao sessaoListOldSessao : sessaoListOld) {
                if (!sessaoListNew.contains(sessaoListOldSessao)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Sessao " + sessaoListOldSessao + " since its sala field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Sessao> attachedSessaoListNew = new ArrayList<Sessao>();
            for (Sessao sessaoListNewSessaoToAttach : sessaoListNew) {
                sessaoListNewSessaoToAttach = em.getReference(sessaoListNewSessaoToAttach.getClass(), sessaoListNewSessaoToAttach.getIdSessao());
                attachedSessaoListNew.add(sessaoListNewSessaoToAttach);
            }
            sessaoListNew = attachedSessaoListNew;
            sala.setSessaoList(sessaoListNew);
            sala = em.merge(sala);
            for (Sessao sessaoListNewSessao : sessaoListNew) {
                if (!sessaoListOld.contains(sessaoListNewSessao)) {
                    Sala oldSalaOfSessaoListNewSessao = sessaoListNewSessao.getSala();
                    sessaoListNewSessao.setSala(sala);
                    sessaoListNewSessao = em.merge(sessaoListNewSessao);
                    if (oldSalaOfSessaoListNewSessao != null && !oldSalaOfSessaoListNewSessao.equals(sala)) {
                        oldSalaOfSessaoListNewSessao.getSessaoList().remove(sessaoListNewSessao);
                        oldSalaOfSessaoListNewSessao = em.merge(oldSalaOfSessaoListNewSessao);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = sala.getIdSala();
                if (findSala(id) == null) {
                    throw new NonexistentEntityException("The sala with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(BigDecimal id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Sala sala;
            try {
                sala = em.getReference(Sala.class, id);
                sala.getIdSala();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The sala with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Sessao> sessaoListOrphanCheck = sala.getSessaoList();
            for (Sessao sessaoListOrphanCheckSessao : sessaoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Sala (" + sala + ") cannot be destroyed since the Sessao " + sessaoListOrphanCheckSessao + " in its sessaoList field has a non-nullable sala field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(sala);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Sala> findSalaEntities() {
        return findSalaEntities(true, -1, -1);
    }

    public List<Sala> findSalaEntities(int maxResults, int firstResult) {
        return findSalaEntities(false, maxResults, firstResult);
    }

    private List<Sala> findSalaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Sala.class));
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

    public Sala findSala(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Sala.class, id);
        } finally {
            em.close();
        }
    }

    public int getSalaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Sala> rt = cq.from(Sala.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
