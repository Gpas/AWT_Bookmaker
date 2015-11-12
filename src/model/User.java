package model;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table (name="user")
public class User {

    private int id;
    private float credit;
    private String username, firstname, lastname;
    private boolean isBookmaker;

    private Set<User_Game> games = new HashSet<User_Game>();

    public User(){

    };

    public User(String username, String firstname, String lastname, boolean isBookmaker){
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.isBookmaker = isBookmaker;
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

    public float getCredit() {
        return credit;
    }

    public void setCredit(float credit) {
        this.credit = credit;
    }

    @OneToMany(mappedBy = "user")
    public Set<User_Game> getGames() {
        return games;
    }

    public void setGames(Set<User_Game> games) {
        this.games = games;
    }
}
