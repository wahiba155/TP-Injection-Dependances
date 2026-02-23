package ma.enset.metier;

import ma.enset.dao.IDao;
import ma.enset.ioc.Inject;

public class MetierImpl implements IMetier {

    private IDao dao;

    @Inject
    public MetierImpl(IDao dao) {
        this.dao = dao;
    }

     public MetierImpl() {
        // constructeur vide nécessaire pour new MetierImpl()
    }
    @Override
    public double calcul() {
        return dao.getData() * 2;
    }
}