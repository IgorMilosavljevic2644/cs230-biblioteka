
package Entiteti;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Igor
 */
@Entity
@Table(name = "knjiga")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Knjiga.findAll", query = "SELECT k FROM Knjiga k")
    , @NamedQuery(name = "Knjiga.findByKnjigaId", query = "SELECT k FROM Knjiga k WHERE k.knjigaId = :knjigaId")
    , @NamedQuery(name = "Knjiga.findByNaziv", query = "SELECT k FROM Knjiga k WHERE k.naziv = :naziv")
    , @NamedQuery(name = "Knjiga.findByAutor", query = "SELECT k FROM Knjiga k WHERE k.autor = :autor")
    , @NamedQuery(name = "Knjiga.findByGodina", query = "SELECT k FROM Knjiga k WHERE k.godina = :godina")
    , @NamedQuery(name = "Knjiga.findByKolicina", query = "SELECT k FROM Knjiga k WHERE k.kolicina = :kolicina")})
public class Knjiga implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "KNJIGA_ID")
    private Integer knjigaId;
    @Size(max = 255)
    @Column(name = "NAZIV")
    private String naziv;
    @Size(max = 255)
    @Column(name = "AUTOR")
    private String autor;
    @Column(name = "GODINA")
    private Integer godina;
    @Column(name = "KOLICINA")
    private Integer kolicina;
    @OneToMany(mappedBy = "knjigaId")
    private Collection<Iznajmljivanje> iznajmljivanjeCollection;

    public Knjiga() {
    }

    public Knjiga(Integer knjigaId) {
        this.knjigaId = knjigaId;
    }

    public Integer getKnjigaId() {
        return knjigaId;
    }

    public void setKnjigaId(Integer knjigaId) {
        this.knjigaId = knjigaId;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Integer getGodina() {
        return godina;
    }

    public void setGodina(Integer godina) {
        this.godina = godina;
    }

    public Integer getKolicina() {
        return kolicina;
    }

    public void setKolicina(Integer kolicina) {
        this.kolicina = kolicina;
    }

    @XmlTransient
    public Collection<Iznajmljivanje> getIznajmljivanjeCollection() {
        return iznajmljivanjeCollection;
    }

    public void setIznajmljivanjeCollection(Collection<Iznajmljivanje> iznajmljivanjeCollection) {
        this.iznajmljivanjeCollection = iznajmljivanjeCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (knjigaId != null ? knjigaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Knjiga)) {
            return false;
        }
        Knjiga other = (Knjiga) object;
        if ((this.knjigaId == null && other.knjigaId != null) || (this.knjigaId != null && !this.knjigaId.equals(other.knjigaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entiteti.Knjiga[ knjigaId=" + knjigaId + " ]";
    }

}
