/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaocinema.DAL;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author joao_
 */
@Entity
@Table(name = "FILME")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Filme.findAll", query = "SELECT f FROM Filme f")
    , @NamedQuery(name = "Filme.findByIdFilme", query = "SELECT f FROM Filme f WHERE f.idFilme = :idFilme")
    , @NamedQuery(name = "Filme.findByNome", query = "SELECT f FROM Filme f WHERE f.nome = :nome")
    , @NamedQuery(name = "Filme.findByRating", query = "SELECT f FROM Filme f WHERE f.rating = :rating")
    , @NamedQuery(name = "Filme.findByPegi", query = "SELECT f FROM Filme f WHERE f.pegi = :pegi")
    , @NamedQuery(name = "Filme.findByRealizador", query = "SELECT f FROM Filme f WHERE f.realizador = :realizador")
    , @NamedQuery(name = "Filme.findByDuracao", query = "SELECT f FROM Filme f WHERE f.duracao = :duracao")
    , @NamedQuery(name = "Filme.findByEstado", query = "SELECT f FROM Filme f WHERE f.estado = :estado")})
public class Filme implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID_FILME")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private BigDecimal idFilme;
    @Column(name = "NOME")
    private String nome;
    @Column(name = "RATING")
    private String rating;
    @Column(name = "PEGI")
    private String pegi;
    @Column(name = "REALIZADOR")
    private String realizador;
    @Column(name = "DURACAO")
    private String duracao;
    @Column(name = "ESTADO")
    private String estado;
    @OneToMany(mappedBy = "filme")
    private List<Sessao> sessaoList;

    public Filme() {
    }

    public Filme(BigDecimal idFilme) {
        this.idFilme = idFilme;
    }

    public BigDecimal getIdFilme() {
        return idFilme;
    }

    public void setIdFilme(BigDecimal idFilme) {
        this.idFilme = idFilme;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getPegi() {
        return pegi;
    }

    public void setPegi(String pegi) {
        this.pegi = pegi;
    }

    public String getRealizador() {
        return realizador;
    }

    public void setRealizador(String realizador) {
        this.realizador = realizador;
    }

    public String getDuracao() {
        return duracao;
    }

    public void setDuracao(String duracao) {
        this.duracao = duracao;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @XmlTransient
    public List<Sessao> getSessaoList() {
        return sessaoList;
    }

    public void setSessaoList(List<Sessao> sessaoList) {
        this.sessaoList = sessaoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idFilme != null ? idFilme.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Filme)) {
            return false;
        }
        Filme other = (Filme) object;
        if ((this.idFilme == null && other.idFilme != null) || (this.idFilme != null && !this.idFilme.equals(other.idFilme))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gestaocinema.DAL.Filme[ idFilme=" + idFilme + " ]";
    }
    
}
