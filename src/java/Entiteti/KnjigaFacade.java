
package Entiteti;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Igor
 */
@Stateless
public class KnjigaFacade extends AbstractFacade<Knjiga> {

    @PersistenceContext(unitName = "CS230-Projekat3PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public KnjigaFacade() {
        super(Knjiga.class);
    }

}
