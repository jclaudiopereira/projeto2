/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaocinema.BLL;

import gestaocinema.BLL.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import gestaocinema.DAL.Filme;
import gestaocinema.DAL.Sala;
import gestaocinema.DAL.Venda;
import java.util.ArrayList;
import java.util.List;
import gestaocinema.DAL.Lugar;
import gestaocinema.DAL.Sessao;
import java.math.BigDecimal;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author joao_
 */
public class SessaoJpaController implements Serializable {

    public SessaoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Sessao sessao) {
        if (sessao.getVendaList() == null) {
            sessao.setVendaList(new ArrayList<Venda>());
        }
        if (sessao.getLugarList() == null) {
            sessao.setLugarList(new ArrayList<Lugar>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Filme filme = sessao.getFilme();
            if (filme != null) {
                filme = em.getReference(filme.getClass(), filme.getIdFilme());
                sessao.setFilme(filme);
            }
            Sala sala = sessao.getSala();
            if (sala != null) {
                sala = em.getReference(sala.getClass(), sala.getIdSala());
                sessao.setSala(sala);
            }
            List<Venda> attachedVendaList = new ArrayList<Venda>();
            for (Venda vendaListVendaToAttach : sessao.getVendaList()) {
                vendaListVendaToAttach = em.getReference(vendaListVendaToAttach.getClass(), vendaListVendaToAttach.getIdVenda());
                attachedVendaList.add(vendaListVendaToAttach);
            }
            sessao.setVendaList(attachedVendaList);
            List<Lugar> attachedLugarList = new ArrayList<Lugar>();
            for (Lugar lugarListLugarToAttach : sessao.getLugarList()) {
                lugarListLugarToAttach = em.getReference(lugarListLugarToAttach.getClass(), lugarListLugarToAttach.getIdLugar());
                attachedLugarList.add(lugarListLugarToAttach);
            }
            sessao.setLugarList(attachedLugarList);
            em.persist(sessao);
            if (filme != null) {
                filme.getSessaoList().add(sessao);
                filme = em.merge(filme);
            }
            if (sala != null) {
                sala.getSessaoList().add(sessao);
                sala = em.merge(sala);
            }
            for (Venda vendaListVenda : sessao.getVendaList()) {
                Sessao oldSessaoOfVendaListVenda = vendaListVenda.getSessao();
                vendaListVenda.setSessao(sessao);
                vendaListVenda = em.merge(vendaListVenda);
                if (oldSessaoOfVendaListVenda != null) {
                    oldSessaoOfVendaListVenda.getVendaList().remove(vendaListVenda);
                    oldSessaoOfVendaListVenda = em.merge(oldSessaoOfVendaListVenda);
                }
            }
            for (Lugar lugarListLugar : sessao.getLugarList()) {
                Sessao oldSessaoOfLugarListLugar = lugarListLugar.getSessao();
                lugarListLugar.setSessao(sessao);
                lugarListLugar = em.merge(lugarListLugar);
                if (oldSessaoOfLugarListLugar != null) {
                    oldSessaoOfLugarListLugar.getLugarList().remove(lugarListLugar);
                    oldSessaoOfLugarListLugar = em.merge(oldSessaoOfLugarListLugar);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Sessao sessao) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Sessao persistentSessao = em.find(Sessao.class, sessao.getIdSessao());
            Filme filmeOld = persistentSessao.getFilme();
            Filme filmeNew = sessao.getFilme();
            Sala salaOld = persistentSessao.getSala();
            Sala salaNew = sessao.getSala();
            List<Venda> vendaListOld = persistentSessao.getVendaList();
            List<Venda> vendaListNew = sessao.getVendaList();
            List<Lugar> lugarListOld = persistentSessao.getLugarList();
            List<Lugar> lugarListNew = sessao.getLugarList();
            if (filmeNew != null) {
                filmeNew = em.getReference(filmeNew.getClass(), filmeNew.getIdFilme());
                sessao.setFilme(filmeNew);
            }
            if (salaNew != null) {
                salaNew = em.getReference(salaNew.getClass(), salaNew.getIdSala());
                sessao.setSala(salaNew);
            }
            List<Venda> attachedVendaListNew = new ArrayList<Venda>();
            for (Venda vendaListNewVendaToAttach : vendaListNew) {
                vendaListNewVendaToAttach = em.getReference(vendaListNewVendaToAttach.getClass(), vendaListNewVendaToAttach.getIdVenda());
                attachedVendaListNew.add(vendaListNewVendaToAttach);
            }
            vendaListNew = attachedVendaListNew;
            sessao.setVendaList(vendaListNew);
            List<Lugar> attachedLugarListNew = new ArrayList<Lugar>();
            for (Lugar lugarListNewLugarToAttach : lugarListNew) {
                lugarListNewLugarToAttach = em.getReference(lugarListNewLugarToAttach.getClass(), lugarListNewLugarToAttach.getIdLugar());
                attachedLugarListNew.add(lugarListNewLugarToAttach);
            }
            lugarListNew = attachedLugarListNew;
            sessao.setLugarList(lugarListNew);
            sessao = em.merge(sessao);
            if (filmeOld != null && !filmeOld.equals(filmeNew)) {
                filmeOld.getSessaoList().remove(sessao);
                filmeOld = em.merge(filmeOld);
            }
            if (filmeNew != null && !filmeNew.equals(filmeOld)) {
                filmeNew.getSessaoList().add(sessao);
                filmeNew = em.merge(filmeNew);
            }
            if (salaOld != null && !salaOld.equals(salaNew)) {
                salaOld.getSessaoList().remove(sessao);
                salaOld = em.merge(salaOld);
            }
            if (salaNew != null && !salaNew.equals(salaOld)) {
                salaNew.getSessaoList().add(sessao);
                salaNew = em.merge(salaNew);
            }
            for (Venda vendaListOldVenda : vendaListOld) {
                if (!vendaListNew.contains(vendaListOldVenda)) {
                    vendaListOldVenda.setSessao(null);
                    vendaListOldVenda = em.merge(vendaListOldVenda);
                }
            }
            for (Venda vendaListNewVenda : vendaListNew) {
                if (!vendaListOld.contains(vendaListNewVenda)) {
                    Sessao oldSessaoOfVendaListNewVenda = vendaListNewVenda.getSessao();
                    vendaListNewVenda.setSessao(sessao);
                    vendaListNewVenda = em.merge(vendaListNewVenda);
                    if (oldSessaoOfVendaListNewVenda != null && !oldSessaoOfVendaListNewVenda.equals(sessao)) {
                        oldSessaoOfVendaListNewVenda.getVendaList().remove(vendaListNewVenda);
                        oldSessaoOfVendaListNewVenda = em.merge(oldSessaoOfVendaListNewVenda);
                    }
                }
            }
            for (Lugar lugarListOldLugar : lugarListOld) {
                if (!lugarListNew.contains(lugarListOldLugar)) {
                    lugarListOldLugar.setSessao(null);
                    lugarListOldLugar = em.merge(lugarListOldLugar);
                }
            }
            for (Lugar lugarListNewLugar : lugarListNew) {
                if (!lugarListOld.contains(lugarListNewLugar)) {
                    Sessao oldSessaoOfLugarListNewLugar = lugarListNewLugar.getSessao();
                    lugarListNewLugar.setSessao(sessao);
                    lugarListNewLugar = em.merge(lugarListNewLugar);
                    if (oldSessaoOfLugarListNewLugar != null && !oldSessaoOfLugarListNewLugar.equals(sessao)) {
                        oldSessaoOfLugarListNewLugar.getLugarList().remove(lugarListNewLugar);
                        oldSessaoOfLugarListNewLugar = em.merge(oldSessaoOfLugarListNewLugar);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = sessao.getIdSessao();
                if (findSessao(id) == null) {
                    throw new NonexistentEntityException("The sessao with id " + id + " no longer exists.");
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
            Sessao sessao;
            try {
                sessao = em.getReference(Sessao.class, id);
                sessao.getIdSessao();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The sessao with id " + id + " no longer exists.", enfe);
            }
            Filme filme = sessao.getFilme();
            if (filme != null) {
                filme.getSessaoList().remove(sessao);
                filme = em.merge(filme);
            }
            Sala sala = sessao.getSala();
            if (sala != null) {
                sala.getSessaoList().remove(sessao);
                sala = em.merge(sala);
            }
            List<Venda> vendaList = sessao.getVendaList();
            for (Venda vendaListVenda : vendaList) {
                vendaListVenda.setSessao(null);
                vendaListVenda = em.merge(vendaListVenda);
            }
            List<Lugar> lugarList = sessao.getLugarList();
            for (Lugar lugarListLugar : lugarList) {
                lugarListLugar.setSessao(null);
                lugarListLugar = em.merge(lugarListLugar);
            }
            em.remove(sessao);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Sessao> findSessaoEntities() {
        return findSessaoEntities(true, -1, -1);
    }

    public List<Sessao> findSessaoEntities(int maxResults, int firstResult) {
        return findSessaoEntities(false, maxResults, firstResult);
    }

    private List<Sessao> findSessaoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Sessao.class));
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

    public Sessao findSessao(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Sessao.class, id);
        } finally {
            em.close();
        }
    }

    public int getSessaoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Sessao> rt = cq.from(Sessao.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
