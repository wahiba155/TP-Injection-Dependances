package ma.enset.presentation;

import ma.enset.dao.DaoImpl;
import ma.enset.metier.MetierImpl;
import ma.enset.ioc.Inject;
import ma.enset.ioc.MiniApplicationContext;

public class TestMiniFramework {

    public static class MyService {
        @Inject
        private MetierImpl metier;

        public void run() {
            System.out.println("Résultat = " + metier.calcul());
        }
    }

    public static void main(String[] args) throws Exception {
        
        MiniApplicationContext context = new MiniApplicationContext();

        // Ajouter les beans
        context.addBean(DaoImpl.class, new DaoImpl());
        context.addBean(MetierImpl.class, new MetierImpl());

        // Créer service et injecter dépendances
        MyService service = new MyService();
        context.injectDependencies(service);

        service.run();
        
    }
}