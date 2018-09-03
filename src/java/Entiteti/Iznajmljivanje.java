
package Entiteti;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Igor
 */
@Entity
@Table(name = "iznajmljivanje")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Iznajmljivanje.findAll", query = "SELECT i FROM Iznajmljivanje i")
    , @NamedQuery(name = "Iznajmljivanje.findByIznajmljivanjeId", query = "SELECT i FROM Iznajmljivanje i WHERE i.iznajmljivanjeId = :iznajmljivanjeId")
    , @NamedQuery(name = "Iznajmljivanje.findByIznajmljivanjeDatum", query = "SELECT i FROM Iznajmljivanje i WHERE i.iznajmljivanjeDatum = :iznajmljivanjeDatum")})
public class Iznajmljivanje implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "IZNAJMLJIVANJE_ID")
    private Integer iznajmljivanjeId;
    @Column(name = "IZNAJMLJIVANJE_DATUM")
    @Temporal(TemporalType.DATE)
    private Date iznajmljivanjeDatum;
    @JoinColumn(name = "KNJIGA_ID", referencedColumnName = "KNJIGA_ID")
    @ManyToOne
    private Knjiga knjigaId;
    @JoinColumn(name = "KORISNIK_ID", referencedColumnName = "KORISNIK_ID")
    @ManyToOne
    private Korisnik korisnikId;
    
    private static AtomicInteger iznajmljivanjeIdCounter = new AtomicInteger(0);

    public Iznajmljivanje() {
        this.iznajmljivanjeId = iznajmljivanjeIdCounter.incrementAndGet();
    }

    public Iznajmljivanje(Integer iznajmljivanjeId) {
        this.iznajmljivanjeId = iznajmljivanjeId;
    }

    public Integer getIznajmljivanjeId() {
        return iznajmljivanjeId;
    }

    public void setIznajmljivanjeId(Integer iznajmljivanjeId) {
        this.iznajmljivanjeId = iznajmljivanjeId;
    }

    public Date getIznajmljivanjeDatum() {
        return iznajmljivanjeDatum;
    }
    
    public String getIsticanjeDatum() {
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.getIznajmljivanjeDatum());
        calendar.add(Calendar.DATE, 20);
        return date.format(calendar.getTime());
    }

    public void setIznajmljivanjeDatum(Date iznajmljivanjeDatum) {
        this.iznajmljivanjeDatum = iznajmljivanjeDatum;
    }

    public Knjiga getKnjigaId() {
        return knjigaId;
    }

    public void setKnjigaId(Knjiga knjigaId) {
        this.knjigaId = knjigaId;
    }

    public Korisnik getKorisnikId() {
        return korisnikId;
    }

    public void setKorisnikId(Korisnik korisnikId) {
        this.korisnikId = korisnikId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iznajmljivanjeId != null ? iznajmljivanjeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Iznajmljivanje)) {
            return false;
        }
        Iznajmljivanje other = (Iznajmljivanje) object;
        if ((this.iznajmljivanjeId == null && other.iznajmljivanjeId != null) || (this.iznajmljivanjeId != null && !this.iznajmljivanjeId.equals(other.iznajmljivanjeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entiteti.Iznajmljivanje[ iznajmljivanjeId=" + iznajmljivanjeId + " ]";
    }

}
