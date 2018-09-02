package Entiteti;

import Entiteti.Knjiga;
import Entiteti.Korisnik;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-08-30T21:48:24")
@StaticMetamodel(Iznajmljivanje.class)
public class Iznajmljivanje_ { 

    public static volatile SingularAttribute<Iznajmljivanje, Integer> iznajmljivanjeId;
    public static volatile SingularAttribute<Iznajmljivanje, Knjiga> knjigaId;
    public static volatile SingularAttribute<Iznajmljivanje, Date> iznajmljivanjeDatum;
    public static volatile SingularAttribute<Iznajmljivanje, Korisnik> korisnikId;

}