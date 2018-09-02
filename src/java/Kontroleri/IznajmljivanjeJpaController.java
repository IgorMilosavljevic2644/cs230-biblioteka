
package Kontroleri;

import Entiteti.Iznajmljivanje;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entiteti.Knjiga;
import Entiteti.Korisnik;
import Kontroleri.exceptions.NonexistentEntityException;
import Kontroleri.exceptions.PreexistingEntityException;
import Kontroleri.exceptions.RollbackFailureException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Igor
 */
public class IznajmljivanjeJpaController implements Serializable {

    public IznajmljivanjeJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Iznajmljivanje iznajmljivanje) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Knjiga knjigaId = iznajmljivanje.getKnjigaId();
            if (knjigaId != null) {
                knjigaId = em.getReference(knjigaId.getClass(), knjigaId.getKnjigaId());
                iznajmljivanje.setKnjigaId(knjigaId);
            }
            Korisnik korisnikId = iznajmljivanje.getKorisnikId();
            if (korisnikId != null) {
                korisnikId = em.getReference(korisnikId.getClass(), korisnikId.getKorisnikId());
                iznajmljivanje.setKorisnikId(korisnikId);
            }
            em.persist(iznajmljivanje);
            if (knjigaId != null) {
                knjigaId.getIznajmljivanjeCollection().add(iznajmljivanje);
                knjigaId = em.merge(knjigaId);
            }
            if (korisnikId != null) {
                korisnikId.getIznajmljivanjeCollection().add(iznajmljivanje);
                korisnikId = em.merge(korisnikId);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findIznajmljivanje(iznajmljivanje.getIznajmljivanjeId()) != null) {
                throw new PreexistingEntityException("Iznajmljivanje " + iznajmljivanje + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Iznajmljivanje iznajmljivanje) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Iznajmljivanje persistentIznajmljivanje = em.find(Iznajmljivanje.class, iznajmljivanje.getIznajmljivanjeId());
            Knjiga knjigaIdOld = persistentIznajmljivanje.getKnjigaId();
            Knjiga knjigaIdNew = iznajmljivanje.getKnjigaId();
            Korisnik korisnikIdOld = persistentIznajmljivanje.getKorisnikId();
            Korisnik korisnikIdNew = iznajmljivanje.getKorisnikId();
            if (knjigaIdNew != null) {
                knjigaIdNew = em.getReference(knjigaIdNew.getClass(), knjigaIdNew.getKnjigaId());
                iznajmljivanje.setKnjigaId(knjigaIdNew);
            }
            if (korisnikIdNew != null) {
                korisnikIdNew = em.getReference(korisnikIdNew.getClass(), korisnikIdNew.getKorisnikId());
                iznajmljivanje.setKorisnikId(korisnikIdNew);
            }
            iznajmljivanje = em.merge(iznajmljivanje);
            if (knjigaIdOld != null && !knjigaIdOld.equals(knjigaIdNew)) {
                knjigaIdOld.getIznajmljivanjeCollection().remove(iznajmljivanje);
                knjigaIdOld = em.merge(knjigaIdOld);
            }
            if (knjigaIdNew != null && !knjigaIdNew.equals(knjigaIdOld)) {
                knjigaIdNew.getIznajmljivanjeCollection().add(iznajmljivanje);
                knjigaIdNew = em.merge(knjigaIdNew);
            }
            if (korisnikIdOld != null && !korisnikIdOld.equals(korisnikIdNew)) {
                korisnikIdOld.getIznajmljivanjeCollection().remove(iznajmljivanje);
                korisnikIdOld = em.merge(korisnikIdOld);
            }
            if (korisnikIdNew != null && !korisnikIdNew.equals(korisnikIdOld)) {
                korisnikIdNew.getIznajmljivanjeCollection().add(iznajmljivanje);
                korisnikIdNew = em.merge(korisnikIdNew);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = iznajmljivanje.getIznajmljivanjeId();
                if (findIznajmljivanje(id) == null) {
                    throw new NonexistentEntityException("The iznajmljivanje with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Iznajmljivanje iznajmljivanje;
            try {
                iznajmljivanje = em.getReference(Iznajmljivanje.class, id);
                iznajmljivanje.getIznajmljivanjeId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The iznajmljivanje with id " + id + " no longer exists.", enfe);
            }
            Knjiga knjigaId = iznajmljivanje.getKnjigaId();
            if (knjigaId != null) {
                knjigaId.getIznajmljivanjeCollection().remove(iznajmljivanje);
                knjigaId = em.merge(knjigaId);
            }
            Korisnik korisnikId = iznajmljivanje.getKorisnikId();
            if (korisnikId != null) {
                korisnikId.getIznajmljivanjeCollection().remove(iznajmljivanje);
                korisnikId = em.merge(korisnikId);
            }
            em.remove(iznajmljivanje);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Iznajmljivanje> findIznajmljivanjeEntities() {
        return findIznajmljivanjeEntities(true, -1, -1);
    }

    public List<Iznajmljivanje> findIznajmljivanjeEntities(int maxResults, int firstResult) {
        return findIznajmljivanjeEntities(false, maxResults, firstResult);
    }

    private List<Iznajmljivanje> findIznajmljivanjeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Iznajmljivanje.class));
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

    public Iznajmljivanje findIznajmljivanje(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Iznajmljivanje.class, id);
        } finally {
            em.close();
        }
    }

    public int getIznajmljivanjeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Iznajmljivanje> rt = cq.from(Iznajmljivanje.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
