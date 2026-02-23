package ma.enset.dao;

import org.springframework.stereotype.Repository;

@Repository
public class DaoImpl implements IDao {

    @Override
    public double getData() {
        return Math.random() * 100;
    }
}