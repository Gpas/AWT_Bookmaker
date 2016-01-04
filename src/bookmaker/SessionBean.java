package bookmaker;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import java.io.Serializable;
import java.util.ArrayList;
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

    private User user;
    private String message, title;
    private String errorLogin;
    private SessionFactory sessionFactory;
    private Locale locale;
    private ResourceBundle bundle;
    private PropertiesUtil propertiesUtil;

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
        //Init language
        locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        bundle = ResourceBundle.getBundle("lang", locale);
        //Init navigation
        links.add(new Navlink("registration","navbarRegister"));
        links.add(new Navlink("games", "navbarGames"));
        links.add(new Navlink("newGame", "navbarCreateGame"));
        this.reloadNavlinks();
        //Load bundles for properties util
        propertiesUtil = new PropertiesUtil();
        propertiesUtil.readProperties(locale);
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
        //Get the current page
        String viewId = faceContext.getViewRoot().getViewId();
        //Reload the text of navlinks in the link list
        this.reloadNavlinks();
        //Reload util
        propertiesUtil.readProperties(locale);
        // Return the current page with reload
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

    public PropertiesUtil getPropertiesUtil() {
        return propertiesUtil;
    }

    public void setPropertiesUtil(PropertiesUtil propertiesUtil) {
        this.propertiesUtil = propertiesUtil;
    }

    //region Navigation Handling
    //Navigation Handling
    //**************************************
    public class Navlink{

        private String outcome;
        private String identifier;
        private String text;
        private String viewId;

        public Navlink(String outcome, String identifier){
            this.outcome = outcome;
            this.identifier = identifier;
            this.viewId = "/" + outcome + ".xhtml";
        }

        public String getOutcome() {
            return outcome;
        }

        public void setOutcome(String outcome) {
            this.outcome = outcome;
        }

        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getViewId() {
            return viewId;
        }

        public void setViewId(String viewId) {
            this.viewId = viewId;
        }
    }

    private List<Navlink> links = new ArrayList<>();

    public void reloadNavlinks(){
        for(Navlink link : links){
            link.text = bundle.getString(link.identifier);
        }
    }

    public List<Navlink> getLinks() {
        return links;
    }

    public void setLinks(List<Navlink> links) {
        this.links = links;
    }
    //endregion

}