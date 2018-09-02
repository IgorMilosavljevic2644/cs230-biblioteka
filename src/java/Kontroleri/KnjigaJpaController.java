
package Kontroleri;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entiteti.Iznajmljivanje;
import Entiteti.Knjiga;
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
public class KnjigaJpaController implements Serializable {

    public KnjigaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Knjiga knjiga) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (knjiga.getIznajmljivanjeCollection() == null) {
            knjiga.setIznajmljivanjeCollection(new ArrayList<Iznajmljivanje>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Iznajmljivanje> attachedIznajmljivanjeCollection = new ArrayList<Iznajmljivanje>();
            for (Iznajmljivanje iznajmljivanjeCollectionIznajmljivanjeToAttach : knjiga.getIznajmljivanjeCollection()) {
                iznajmljivanjeCollectionIznajmljivanjeToAttach = em.getReference(iznajmljivanjeCollectionIznajmljivanjeToAttach.getClass(), iznajmljivanjeCollectionIznajmljivanjeToAttach.getIznajmljivanjeId());
                attachedIznajmljivanjeCollection.add(iznajmljivanjeCollectionIznajmljivanjeToAttach);
            }
            knjiga.setIznajmljivanjeCollection(attachedIznajmljivanjeCollection);
            em.persist(knjiga);
            for (Iznajmljivanje iznajmljivanjeCollectionIznajmljivanje : knjiga.getIznajmljivanjeCollection()) {
                Knjiga oldKnjigaIdOfIznajmljivanjeCollectionIznajmljivanje = iznajmljivanjeCollectionIznajmljivanje.getKnjigaId();
                iznajmljivanjeCollectionIznajmljivanje.setKnjigaId(knjiga);
                iznajmljivanjeCollectionIznajmljivanje = em.merge(iznajmljivanjeCollectionIznajmljivanje);
                if (oldKnjigaIdOfIznajmljivanjeCollectionIznajmljivanje != null) {
                    oldKnjigaIdOfIznajmljivanjeCollectionIznajmljivanje.getIznajmljivanjeCollection().remove(iznajmljivanjeCollectionIznajmljivanje);
                    oldKnjigaIdOfIznajmljivanjeCollectionIznajmljivanje = em.merge(oldKnjigaIdOfIznajmljivanjeCollectionIznajmljivanje);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findKnjiga(knjiga.getKnjigaId()) != null) {
                throw new PreexistingEntityException("Knjiga " + knjiga + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Knjiga knjiga) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Knjiga persistentKnjiga = em.find(Knjiga.class, knjiga.getKnjigaId());
            Collection<Iznajmljivanje> iznajmljivanjeCollectionOld = persistentKnjiga.getIznajmljivanjeCollection();
            Collection<Iznajmljivanje> iznajmljivanjeCollectionNew = knjiga.getIznajmljivanjeCollection();
            Collection<Iznajmljivanje> attachedIznajmljivanjeCollectionNew = new ArrayList<Iznajmljivanje>();
            for (Iznajmljivanje iznajmljivanjeCollectionNewIznajmljivanjeToAttach : iznajmljivanjeCollectionNew) {
                iznajmljivanjeCollectionNewIznajmljivanjeToAttach = em.getReference(iznajmljivanjeCollectionNewIznajmljivanjeToAttach.getClass(), iznajmljivanjeCollectionNewIznajmljivanjeToAttach.getIznajmljivanjeId());
                attachedIznajmljivanjeCollectionNew.add(iznajmljivanjeCollectionNewIznajmljivanjeToAttach);
            }
            iznajmljivanjeCollectionNew = attachedIznajmljivanjeCollectionNew;
            knjiga.setIznajmljivanjeCollection(iznajmljivanjeCollectionNew);
            knjiga = em.merge(knjiga);
            for (Iznajmljivanje iznajmljivanjeCollectionOldIznajmljivanje : iznajmljivanjeCollectionOld) {
                if (!iznajmljivanjeCollectionNew.contains(iznajmljivanjeCollectionOldIznajmljivanje)) {
                    iznajmljivanjeCollectionOldIznajmljivanje.setKnjigaId(null);
                    iznajmljivanjeCollectionOldIznajmljivanje = em.merge(iznajmljivanjeCollectionOldIznajmljivanje);
                }
            }
            for (Iznajmljivanje iznajmljivanjeCollectionNewIznajmljivanje : iznajmljivanjeCollectionNew) {
                if (!iznajmljivanjeCollectionOld.contains(iznajmljivanjeCollectionNewIznajmljivanje)) {
                    Knjiga oldKnjigaIdOfIznajmljivanjeCollectionNewIznajmljivanje = iznajmljivanjeCollectionNewIznajmljivanje.getKnjigaId();
                    iznajmljivanjeCollectionNewIznajmljivanje.setKnjigaId(knjiga);
                    iznajmljivanjeCollectionNewIznajmljivanje = em.merge(iznajmljivanjeCollectionNewIznajmljivanje);
                    if (oldKnjigaIdOfIznajmljivanjeCollectionNewIznajmljivanje != null && !oldKnjigaIdOfIznajmljivanjeCollectionNewIznajmljivanje.equals(knjiga)) {
                        oldKnjigaIdOfIznajmljivanjeCollectionNewIznajmljivanje.getIznajmljivanjeCollection().remove(iznajmljivanjeCollectionNewIznajmljivanje);
                        oldKnjigaIdOfIznajmljivanjeCollectionNewIznajmljivanje = em.merge(oldKnjigaIdOfIznajmljivanjeCollectionNewIznajmljivanje);
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
                Integer id = knjiga.getKnjigaId();
                if (findKnjiga(id) == null) {
                    throw new NonexistentEntityException("The knjiga with id " + id + " no longer exists.");
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
            Knjiga knjiga;
            try {
                knjiga = em.getReference(Knjiga.class, id);
                knjiga.getKnjigaId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The knjiga with id " + id + " no longer exists.", enfe);
            }
            Collection<Iznajmljivanje> iznajmljivanjeCollection = knjiga.getIznajmljivanjeCollection();
            for (Iznajmljivanje iznajmljivanjeCollectionIznajmljivanje : iznajmljivanjeCollection) {
                iznajmljivanjeCollectionIznajmljivanje.setKnjigaId(null);
                iznajmljivanjeCollectionIznajmljivanje = em.merge(iznajmljivanjeCollectionIznajmljivanje);
            }
            em.remove(knjiga);
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

    public List<Knjiga> findKnjigaEntities() {
        return findKnjigaEntities(true, -1, -1);
    }

    public List<Knjiga> findKnjigaEntities(int maxResults, int firstResult) {
        return findKnjigaEntities(false, maxResults, firstResult);
    }

    private List<Knjiga> findKnjigaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Knjiga.class));
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

    public Knjiga findKnjiga(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Knjiga.class, id);
        } finally {
            em.close();
        }
    }

    public int getKnjigaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Knjiga> rt = cq.from(Knjiga.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
