package Entiteti;

import Entiteti.Iznajmljivanje;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-09-03T13:26:48")
@StaticMetamodel(Korisnik.class)
public class Korisnik_ { 

    public static volatile SingularAttribute<Korisnik, String> ime;
    public static volatile SingularAttribute<Korisnik, String> prezime;
    public static volatile SingularAttribute<Korisnik, Integer> telefon;
    public static volatile SingularAttribute<Korisnik, Date> datumUclanjivanja;
    public static volatile CollectionAttribute<Korisnik, Iznajmljivanje> iznajmljivanjeCollection;
    public static volatile SingularAttribute<Korisnik, Integer> korisnikId;

}