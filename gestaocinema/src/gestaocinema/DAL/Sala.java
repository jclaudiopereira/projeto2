/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestaocinema.DAL;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
@Table(name = "SALA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sala.findAll", query = "SELECT s FROM Sala s")
    , @NamedQuery(name = "Sala.findByIdSala", query = "SELECT s FROM Sala s WHERE s.idSala = :idSala")
    , @NamedQuery(name = "Sala.findByLugares", query = "SELECT s FROM Sala s WHERE s.lugares = :lugares")
    , @NamedQuery(name = "Sala.findByEcr\u00e3", query = "SELECT s FROM Sala s WHERE s.ecr\u00e3 = :ecr\u00e3")
    , @NamedQuery(name = "Sala.findByOcupada", query = "SELECT s FROM Sala s WHERE s.ocupada = :ocupada")
    , @NamedQuery(name = "Sala.findByNomeSala", query = "SELECT s FROM Sala s WHERE s.nomeSala = :nomeSala")})
public class Sala implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID_SALA")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private BigDecimal idSala;
    @Column(name = "LUGARES")
    private BigInteger lugares;
    @Column(name = "ECR\u00c3")
    private String ecrã;
    @Column(name = "OCUPADA")
    private String ocupada;
    @Column(name = "NOME_SALA")
    private String nomeSala;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sala")
    private List<Sessao> sessaoList;

    public Sala() {
    }

    public Sala(BigDecimal idSala) {
        this.idSala = idSala;
    }

    public BigDecimal getIdSala() {
        return idSala;
    }

    public void setIdSala(BigDecimal idSala) {
        this.idSala = idSala;
    }

    public BigInteger getLugares() {
        return lugares;
    }

    public void setLugares(BigInteger lugares) {
        this.lugares = lugares;
    }

    public String getEcrã() {
        return ecrã;
    }

    public void setEcrã(String ecrã) {
        this.ecrã = ecrã;
    }

    public String getOcupada() {
        return ocupada;
    }

    public void setOcupada(String ocupada) {
        this.ocupada = ocupada;
    }

    public String getNomeSala() {
        return nomeSala;
    }

    public void setNomeSala(String nomeSala) {
        this.nomeSala = nomeSala;
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
        hash += (idSala != null ? idSala.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sala)) {
            return false;
        }
        Sala other = (Sala) object;
        if ((this.idSala == null && other.idSala != null) || (this.idSala != null && !this.idSala.equals(other.idSala))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gestaocinema.DAL.Sala[ idSala=" + idSala + " ]";
    }
    
}
