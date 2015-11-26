package bookmaker;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import model.Game;
import model.Team;
import model.User;
import model.User_Game;

@ManagedBean
@SessionScoped
public class SessionBean implements Serializable {

//    FacesContext context = FacesContext.getCurrentInstance();
//    ResourceBundle bundle = ResourceBundle.getBundle("lang", context.getViewRoot().getLocale());

    private User user;
    private String language;
    private String message, title;

    public SessionBean(){
        //init();
    }

    public User getUser() {
    	User u = new User("foo","bar");
    	Date d = new Date(System.currentTimeMillis());
    	Team home=new Team("Team1");
    	Team guest=new Team("Team2");
    	Game g =new Game(d,home,guest);
    	home=new Team("Team3");
    	guest=new Team("Team4");
    	Game g0 =new Game(d,home,guest);
    	
    	u.addUserGame(new User_Game(g));
    	u.addUserGame(new User_Game(g0));
    	
    	return u;
    	//return user;
        
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