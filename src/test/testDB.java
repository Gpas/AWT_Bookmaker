package test;

import java.util.Date;
import java.util.List;

import model.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import junit.framework.TestCase;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 * Illustrates the use of Hibernate native APIs.  The code here is unchanged from the {@code basic} example, the
 * only difference being the use of annotations to supply the metadata instead of Hibernate mapping files.
 *
 * @author Steve Ebersole
 */
public class testDB extends TestCase {
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
    public void testBasicUsage() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.save( new User( "Testuser", "Pascal", "Grueter" , true));
        session.getTransaction().commit();
        session.close();

        // now lets pull events from the database and list them
        session = sessionFactory.openSession();
        session.beginTransaction();
        List result = session.createQuery( "from User" ).list();
        for ( User user : (List<User>) result ) {
            System.out.println( "User " + user.getUsername() + ": " + user.getLastname() +" "+ user.getFirstname() + ", " + user.getIsBookmaker());
        }
        session.getTransaction().commit();
        session.close();
    }

    @SuppressWarnings({ "unchecked" })
    public void testUserGameConnection() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(new User("Testuser", "Pascal", "Grueter", true));
        User user1 = session.get(User.class, 1);
        Game game1 = new Game(new Date(System.currentTimeMillis()), 1, 2);
        session.save(game1);
        User_Game rel = new User_Game();
        rel.setUser(user1);
        rel.setGame(game1);
        session.save(rel);
        session.getTransaction().commit();
        session.close();

    }

}
