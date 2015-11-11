package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table (name="user")
public abstract class User {

    private int id;
    private float credit;
    private String username, firstname, lastname;
    private boolean isBookmaker;

    public User(){

    };

    public User(String username, String firstname, String lastname){
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.isBookmaker = false;
    }


    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    public int getId() {
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public boolean getIsBookmaker() {
        return isBookmaker;
    }

    public void setIsBookmaker(boolean value){
        this.isBookmaker = value;
    }

    public void login(){
    	
    }
    
    public void logout(){
    	
    }



    

}
