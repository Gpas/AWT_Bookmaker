package bookmaker;



import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

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

    ResourceBundle bundle = ResourceBundle.getBundle("lang", FacesContext.getCurrentInstance().getViewRoot().getLocale());
    //ResourceBundle bundle = ResourceBundle.getBundle("lang");


    private User user;
    private String message, title;
    private String errorLogin;
    private Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

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
        return this.user;
    }

    public String login(){
        if(user.getUsername() != ""){
            //Get the password input
            String password = user.getPassword();
            try{
                User userTemp = loadUser(user.getUsername());
                //Now the actual user password is in the user object
                if(PasswordManager.checkPassword(password, userTemp.getPassword())){
                    //Successful login
                    user = userTemp;
                    errorLogin = "";
                    return "home";
                }
            }
            catch(Exception e){
                //Error
                errorLogin = e.getMessage();
                user = new User();
                return "home";
            }
        }
        //Invalid Username or Password
        errorLogin = bundle.getString("loginError");
        user = new User();
        return "home";

    }

    public String logout(){
        user = new User();
        return "home";
    }

    public User loadUser(String username) throws Exception{
        Session hibernateSession = sessionFactory.openSession();
        String hql = "FROM User u WHERE u.username = :username";
        Query query = hibernateSession.createQuery(hql);
        query.setParameter("username", username);
        List result = query.list();
        hibernateSession.close();
        if(result.size() > 0){
            User user = (User) result.get(0);
            return user;
        }
        else{
            throw new Exception(bundle.getString("loginError"));
        }
    }

    public void saveUser(User user){
        Session hibernateSession = sessionFactory.openSession();
        hibernateSession.beginTransaction();
        hibernateSession.save(user);
        hibernateSession.getTransaction().commit();
        hibernateSession.close();
    }

    public String changeLanguage(String langId){
        FacesContext faceContext = FacesContext.getCurrentInstance();
        locale = Locale.forLanguageTag(langId);
        bundle = ResourceBundle.getBundle("lang", locale);
        faceContext.getViewRoot().setLocale(locale);
        String viewId = faceContext.getViewRoot().getViewId();
        return viewId+"?faces-redirect=true";
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getErrorLogin() {
        return errorLogin;
    }

    public void setErrorLogin(String errorLogin) {
        this.errorLogin = errorLogin;
    }

    //    public String login() {
//        title = bundle.getString("tLogin");
//        message =  MessageFormat.format(bundle.getString("mLogin"), user.getUsername());
//        return "login";
//    }
//
//    public String register() {
//        return "register";
//    }
//

//
//    private void init(){
//        title = bundle.getString("tHome");
//        message = bundle.getString("mHome");
//        user = null;
//    }
}