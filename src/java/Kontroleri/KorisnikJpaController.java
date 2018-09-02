
package Kontroleri;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entiteti.Iznajmljivanje;
import Entiteti.Korisnik;
import Kontroleri.exceptions.NonexistentEntityException;
import Kontroleri.exceptions.PreexistingEntityException;
import Kontroleri.exceptions.RollbackFailureException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Igor
 */
public class KorisnikJpaController implements Serializable {

    public KorisnikJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Korisnik korisnik) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (korisnik.getIznajmljivanjeCollection() == null) {
            korisnik.setIznajmljivanjeCollection(new ArrayList<Iznajmljivanje>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Iznajmljivanje> attachedIznajmljivanjeCollection = new ArrayList<Iznajmljivanje>();
            for (Iznajmljivanje iznajmljivanjeCollectionIznajmljivanjeToAttach : korisnik.getIznajmljivanjeCollection()) {
                iznajmljivanjeCollectionIznajmljivanjeToAttach = em.getReference(iznajmljivanjeCollectionIznajmljivanjeToAttach.getClass(), iznajmljivanjeCollectionIznajmljivanjeToAttach.getIznajmljivanjeId());
                attachedIznajmljivanjeCollection.add(iznajmljivanjeCollectionIznajmljivanjeToAttach);
            }
            korisnik.setIznajmljivanjeCollection(attachedIznajmljivanjeCollection);
            em.persist(korisnik);
            for (Iznajmljivanje iznajmljivanjeCollectionIznajmljivanje : korisnik.getIznajmljivanjeCollection()) {
                Korisnik oldKorisnikIdOfIznajmljivanjeCollectionIznajmljivanje = iznajmljivanjeCollectionIznajmljivanje.getKorisnikId();
                iznajmljivanjeCollectionIznajmljivanje.setKorisnikId(korisnik);
                iznajmljivanjeCollectionIznajmljivanje = em.merge(iznajmljivanjeCollectionIznajmljivanje);
                if (oldKorisnikIdOfIznajmljivanjeCollectionIznajmljivanje != null) {
                    oldKorisnikIdOfIznajmljivanjeCollectionIznajmljivanje.getIznajmljivanjeCollection().remove(iznajmljivanjeCollectionIznajmljivanje);
                    oldKorisnikIdOfIznajmljivanjeCollectionIznajmljivanje = em.merge(oldKorisnikIdOfIznajmljivanjeCollectionIznajmljivanje);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findKorisnik(korisnik.getKorisnikId()) != null) {
                throw new PreexistingEntityException("Korisnik " + korisnik + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Korisnik korisnik) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Korisnik persistentKorisnik = em.find(Korisnik.class, korisnik.getKorisnikId());
            Collection<Iznajmljivanje> iznajmljivanjeCollectionOld = persistentKorisnik.getIznajmljivanjeCollection();
            Collection<Iznajmljivanje> iznajmljivanjeCollectionNew = korisnik.getIznajmljivanjeCollection();
            Collection<Iznajmljivanje> attachedIznajmljivanjeCollectionNew = new ArrayList<Iznajmljivanje>();
            for (Iznajmljivanje iznajmljivanjeCollectionNewIznajmljivanjeToAttach : iznajmljivanjeCollectionNew) {
                iznajmljivanjeCollectionNewIznajmljivanjeToAttach = em.getReference(iznajmljivanjeCollectionNewIznajmljivanjeToAttach.getClass(), iznajmljivanjeCollectionNewIznajmljivanjeToAttach.getIznajmljivanjeId());
                attachedIznajmljivanjeCollectionNew.add(iznajmljivanjeCollectionNewIznajmljivanjeToAttach);
            }
            iznajmljivanjeCollectionNew = attachedIznajmljivanjeCollectionNew;
            korisnik.setIznajmljivanjeCollection(iznajmljivanjeCollectionNew);
            korisnik = em.merge(korisnik);
            for (Iznajmljivanje iznajmljivanjeCollectionOldIznajmljivanje : iznajmljivanjeCollectionOld) {
                if (!iznajmljivanjeCollectionNew.contains(iznajmljivanjeCollectionOldIznajmljivanje)) {
                    iznajmljivanjeCollectionOldIznajmljivanje.setKorisnikId(null);
                    iznajmljivanjeCollectionOldIznajmljivanje = em.merge(iznajmljivanjeCollectionOldIznajmljivanje);
                }
            }
            for (Iznajmljivanje iznajmljivanjeCollectionNewIznajmljivanje : iznajmljivanjeCollectionNew) {
                if (!iznajmljivanjeCollectionOld.contains(iznajmljivanjeCollectionNewIznajmljivanje)) {
                    Korisnik oldKorisnikIdOfIznajmljivanjeCollectionNewIznajmljivanje = iznajmljivanjeCollectionNewIznajmljivanje.getKorisnikId();
                    iznajmljivanjeCollectionNewIznajmljivanje.setKorisnikId(korisnik);
                    iznajmljivanjeCollectionNewIznajmljivanje = em.merge(iznajmljivanjeCollectionNewIznajmljivanje);
                    if (oldKorisnikIdOfIznajmljivanjeCollectionNewIznajmljivanje != null && !oldKorisnikIdOfIznajmljivanjeCollectionNewIznajmljivanje.equals(korisnik)) {
                        oldKorisnikIdOfIznajmljivanjeCollectionNewIznajmljivanje.getIznajmljivanjeCollection().remove(iznajmljivanjeCollectionNewIznajmljivanje);
                        oldKorisnikIdOfIznajmljivanjeCollectionNewIznajmljivanje = em.merge(oldKorisnikIdOfIznajmljivanjeCollectionNewIznajmljivanje);
                    }
                }
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
                Integer id = korisnik.getKorisnikId();
                if (findKorisnik(id) == null) {
                    throw new NonexistentEntityException("The korisnik with id " + id + " no longer exists.");
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
            Korisnik korisnik;
            try {
                korisnik = em.getReference(Korisnik.class, id);
                korisnik.getKorisnikId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The korisnik with id " + id + " no longer exists.", enfe);
            }
            Collection<Iznajmljivanje> iznajmljivanjeCollection = korisnik.getIznajmljivanjeCollection();
            for (Iznajmljivanje iznajmljivanjeCollectionIznajmljivanje : iznajmljivanjeCollection) {
                iznajmljivanjeCollectionIznajmljivanje.setKorisnikId(null);
                iznajmljivanjeCollectionIznajmljivanje = em.merge(iznajmljivanjeCollectionIznajmljivanje);
            }
            em.remove(korisnik);
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

    public List<Korisnik> findKorisnikEntities() {
        return findKorisnikEntities(true, -1, -1);
    }

    public List<Korisnik> findKorisnikEntities(int maxResults, int firstResult) {
        return findKorisnikEntities(false, maxResults, firstResult);
    }

    private List<Korisnik> findKorisnikEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Korisnik.class));
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

    public Korisnik findKorisnik(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Korisnik.class, id);
        } finally {
            em.close();
        }
    }

    public int getKorisnikCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Korisnik> rt = cq.from(Korisnik.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
