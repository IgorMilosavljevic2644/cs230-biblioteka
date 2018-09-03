package Entiteti;

import Entiteti.Iznajmljivanje;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-09-03T13:26:48")
@StaticMetamodel(Knjiga.class)
public class Knjiga_ { 

    public static volatile SingularAttribute<Knjiga, Integer> knjigaId;
    public static volatile CollectionAttribute<Knjiga, Iznajmljivanje> iznajmljivanjeCollection;
    public static volatile SingularAttribute<Knjiga, String> naziv;
    public static volatile SingularAttribute<Knjiga, Integer> kolicina;
    public static volatile SingularAttribute<Knjiga, String> autor;
    public static volatile SingularAttribute<Knjiga, Integer> godina;

}