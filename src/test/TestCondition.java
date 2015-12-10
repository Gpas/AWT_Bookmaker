package test;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.List;

import bookmaker.PasswordManager;
import model.*;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import junit.framework.TestCase;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class TestCondition extends TestCase {
    private SessionFactory sessionFactory;

    @Override
    protected void setUp() throws Exception {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
            // so destroy it manually.
            System.out.println(e);
            StandardServiceRegistryBuilder.destroy( registry );
        }
    }

    @Override
    protected void tearDown() throws Exception {
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }
    }

    @SuppressWarnings({ "unchecked" })
    public void testBetOnCondition() {
        Session session = sessionFactory.openSession();
        String hql = "FROM User";
        Query query = session.createQuery(hql);
        query.setMaxResults(1);
        List result = query.list();
        User user = (User) result.get(0);
        Game game = new Game(new Date(System.currentTimeMillis()), user, 1, 2);
        Condition condition = new Condition(game, 1, 1, 5, 1);
        Bet bet = new Bet(user, condition, 10);
        session.beginTransaction();
        session.save(game);
        session.save(condition);
        session.save(bet);
        session.getTransaction().commit();
        session.close();
    }

}
