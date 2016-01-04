package model;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table (name="user")
public class User {

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private int id;

    @Column(name="balance")
    private float balance = 0;
    @Column(name="username")
    private String username;
    @Column(name="password",columnDefinition = "varchar(256)")
    private String password;
    @Column(name="firstname")
    private String firstname;
    @Column(name="lastname")
    private String lastname;
    @Column(name="isBookmaker")
    private boolean isBookmaker = false;

    @OneToMany(mappedBy = "user")
    private Set<Bet> bets = new HashSet<Bet>();
    @OneToMany(mappedBy = "owner")
    private Set<Game> games = new HashSet<Game>();

    
    public User(){
    	
    }
    
    public User(String firstname,String lastname){
    	this.firstname=firstname;
    	this.lastname=lastname;
    }

    public User(String username, String firstname, String lastname, String password, boolean isBookmaker){
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
        this.isBookmaker = isBookmaker;
    }

    public boolean changeBalance(float amount, boolean addition){
            if(addition){
                this.balance += amount;
                return true;
            }
            else if(((this.balance - amount) < 0)){
                return false;
            }
            else{
                this.balance -= amount;
                return true;
            }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public void setIsBookmaker(boolean isBookmaker) {
        this.isBookmaker = isBookmaker;
    }

    public Set<Game> getGames() {
        return games;
    }

    public void setGames(Set<Game> games) {
        this.games = games;
    }

    public Set<Bet> getBets() {
        return bets;
    }

    public void setBets(Set<Bet> bets) {
        this.bets = bets;
    }
}
