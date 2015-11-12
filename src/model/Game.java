package model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name="game")
public class Game {

	private int id;
    private Date startTime;
    private Team homeTeam,guestTeam;
    private Map<Condition,Float> conditions;

	private Set<User_Game> users = new HashSet<User_Game>();
    
    
    public boolean started(){
    	
    	Date d = new Date(System.currentTimeMillis());
    	return d.after(startTime);
    }
    
    public Game(Date start, Team homeTeam, Team guestTeam){
    	this.startTime=start;
    	this.homeTeam=homeTeam;
    	this.guestTeam=guestTeam;
    }
    
    public float getOddByCondition(Condition c){
    	float f = 0;
    	if(conditions.containsKey(c))
    	f=conditions.get(c);
    	return f;
    }
    
    public void placeOdds(Condition c,Float f){
    	if(!started()){
    		conditions.put(c, f);
    	}
    }

	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Temporal(TemporalType.DATE)
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@OneToOne
	public Team getHomeTeam() {
		return homeTeam;
	}

	public void setHomeTeam(Team homeTeam) {
		this.homeTeam = homeTeam;
	}

	@OneToOne
	public Team getGuestTeam() {
		return guestTeam;
	}

	public void setGuestTeam(Team guestTeam) {
		this.guestTeam = guestTeam;
	}

	@OneToMany(mappedBy = "game")
	public Set<User_Game> getUsers() {
		return users;
	}

	public void setUsers(Set<User_Game> users) {
		this.users = users;
	}
}
