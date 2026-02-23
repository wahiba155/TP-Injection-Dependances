package ma.enset.presentation;

import ma.enset.dao.DaoImpl;
import ma.enset.metier.MetierImpl;
import ma.enset.ioc.MiniApplicationContext;

public class Presentation {
    public static void main(String[] args) throws Exception {

        MiniApplicationContext context = new MiniApplicationContext();

        // Ajouter Dao dans le conteneur
        context.addBean(DaoImpl.class, new DaoImpl());

        // Créer Metier avec injection constructeur
        MetierImpl metier = context.createBean(MetierImpl.class);

        System.out.println("Résultat = " + metier.calcul());
    }
}