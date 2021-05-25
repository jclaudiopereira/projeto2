/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaocinema.DAL;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author joao_
 */
@Entity
@Table(name = "SESSAO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sessao.findAll", query = "SELECT s FROM Sessao s")
    , @NamedQuery(name = "Sessao.findByIdSessao", query = "SELECT s FROM Sessao s WHERE s.idSessao = :idSessao")
    , @NamedQuery(name = "Sessao.findByData", query = "SELECT s FROM Sessao s WHERE s.data = :data")
    , @NamedQuery(name = "Sessao.findByHora", query = "SELECT s FROM Sessao s WHERE s.hora = :hora")
    , @NamedQuery(name = "Sessao.findByPreco", query = "SELECT s FROM Sessao s WHERE s.preco = :preco")
    , @NamedQuery(name = "Sessao.findByEstado", query = "SELECT s FROM Sessao s WHERE s.estado = :estado")
    , @NamedQuery(name = "Sessao.findByOcupacao", query = "SELECT s FROM Sessao s WHERE s.ocupacao = :ocupacao")})
public class Sessao implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID_SESSAO")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private BigDecimal idSessao;
    @Column(name = "DATA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date data;
    @Column(name = "HORA")
    private String hora;
    @Column(name = "PRECO")
    private Double preco;
    @Column(name = "ESTADO")
    private String estado;
    @Column(name = "OCUPACAO")
    private Double ocupacao;
    @OneToMany(mappedBy = "sessao")
    private List<Venda> vendaList;
    @JoinColumn(name = "FILME", referencedColumnName = "ID_FILME")
    @ManyToOne
    private Filme filme;
    @JoinColumn(name = "SALA", referencedColumnName = "ID_SALA")
    @ManyToOne(optional = false)
    private Sala sala;
    @OneToMany(mappedBy = "sessao")
    private List<Lugar> lugarList;

    public Sessao() {
    }

    public Sessao(BigDecimal idSessao) {
        this.idSessao = idSessao;
    }

    public BigDecimal getIdSessao() {
        return idSessao;
    }

    public void setIdSessao(BigDecimal idSessao) {
        this.idSessao = idSessao;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Double getOcupacao() {
        return ocupacao;
    }

    public void setOcupacao(Double ocupacao) {
        this.ocupacao = ocupacao;
    }

    @XmlTransient
    public List<Venda> getVendaList() {
        return vendaList;
    }

    public void setVendaList(List<Venda> vendaList) {
        this.vendaList = vendaList;
    }

    public Filme getFilme() {
        return filme;
    }

    public void setFilme(Filme filme) {
        this.filme = filme;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    @XmlTransient
    public List<Lugar> getLugarList() {
        return lugarList;
    }

    public void setLugarList(List<Lugar> lugarList) {
        this.lugarList = lugarList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSessao != null ? idSessao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sessao)) {
            return false;
        }
        Sessao other = (Sessao) object;
        if ((this.idSessao == null && other.idSessao != null) || (this.idSessao != null && !this.idSessao.equals(other.idSessao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gestaocinema.DAL.Sessao[ idSessao=" + idSessao + " ]";
    }
    
}
