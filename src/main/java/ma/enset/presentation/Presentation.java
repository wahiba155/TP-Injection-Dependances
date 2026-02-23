package ma.enset.presentation;

import ma.enset.dao.DaoImpl;
import ma.enset.metier.MetierImpl;

public class Presentation {
    public static void main(String[] args) {

        DaoImpl dao = new DaoImpl();
        MetierImpl metier = new MetierImpl();

        metier.setDao(dao);

        System.out.println("Résultat = " + metier.calcul());
    }
}