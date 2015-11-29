package test;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.List;

import bookmaker.PasswordManager;
import model.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import junit.framework.TestCase;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class TestPW extends TestCase {
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
    public void testPWManager() {
        String pw = "qwasd1234wasd";
        try {
            String hashedPW = PasswordManager.hashPassword(pw);
            System.out.println(hashedPW);
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(new User("Testuser", "Pascal", "Grueter", hashedPW, true));
            session.getTransaction().commit();
            session.close();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

}
