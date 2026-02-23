package ma.enset.presentation;

import ma.enset.dao.IDao;
import ma.enset.metier.IMetier;

public class Presentation2 {
    public static void main(String[] args) throws Exception {

        Class cDao = Class.forName("dao.DaoImpl");
        IDao dao = (IDao) cDao.getDeclaredConstructor().newInstance();

        Class cMetier = Class.forName("ma.enset.metier.MetierImpl");
        IMetier metier = (IMetier) cMetier.getDeclaredConstructor().newInstance();

        metier.getClass().getMethod("setDao", IDao.class).invoke(metier, dao);

        System.out.println("Résultat = " + metier.calcul());
    }
}