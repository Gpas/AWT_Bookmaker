package bookmaker;



import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import model.Game;
import model.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

@ManagedBean
@SessionScoped
public class SessionBean implements Serializable {

//    FacesContext context = FacesContext.getCurrentInstance();
//    ResourceBundle bundle = ResourceBundle.getBundle("lang", context.getViewRoot().getLocale());

    private User user;
    private String language;
    private String message, title;

    private SessionFactory sessionFactory;

    public SessionBean(){
        // Create the SessionFactory
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
        //Init user
        user = new User();
    }

    public User getUser() {
    	User u = new User("foo","bar");
    	Date d = new Date(System.currentTimeMillis());
    	Game g =new Game(d,u,1,2);
    	Game g0 =new Game(d,u,3,4);

    	
    	return u;
    	//return user;
        
    }

    public String login(){
        if(user.getUsername() != ""){
            //Get the password input
            String password = user.getPassword();
            try{
                user = loadUser(user.getUsername());
                //Now the actual user password is in the user object
                if(PasswordManager.checkPassword(password, user.getPassword())){
                    //Successful login
                    return "login";
                }
                else{
                    //Invalid password
                    return "home";
                }
            }
            catch(Exception e){
                //Error
                return "home";
            }
        }
        //Invalid Username
        return "home";

    }

    public User loadUser(String username) throws Exception{
        Session hibernateSession = sessionFactory.openSession();
        String hql = "FROM User u WHERE u.username = :username";
        Query query = hibernateSession.createQuery(hql);
        query.setParameter("username",username);
        List result = query.list();
        hibernateSession.close();
        if(result.size() > 0){
            User user = (User) result.get(0);
            return user;
        }
        else{
            throw new Exception("User with username '"+username+"' doesn't exist!");
        }
    }

    public void saveUser(User user){
        Session hibernateSession = sessionFactory.openSession();
        hibernateSession.beginTransaction();
        hibernateSession.save(user);
        hibernateSession.getTransaction().commit();
        hibernateSession.close();
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void registerUser(){
    	
    }

//    public String login() {
//        title = bundle.getString("tLogin");
//        message =  MessageFormat.format(bundle.getString("mLogin"), user.getUsername());
//        return "login";
//    }
//
//    public String logout() {
//        init();
//        return "home";
//    }
//
//    public String register() {
//        return "register";
//    }
//
//    public void changeLanguage(ActionEvent e){
//        language = e.getComponent().getId();
//        Locale lang = new Locale(language);
//        bundle = ResourceBundle.getBundle("lang", lang);
//    }
//
//    private void init(){
//        title = bundle.getString("tHome");
//        message = bundle.getString("mHome");
//        user = null;
//    }
}