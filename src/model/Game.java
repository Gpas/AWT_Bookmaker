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
    private int homeTeam,guestTeam;

	private Set<User_Game> users = new HashSet<User_Game>();
	private Set<Condition> conditions = new HashSet<Condition>();

	public Game(){

	}

	public Game(Date start, int homeTeam, int guestTeam){
		this.startTime=start;
		this.homeTeam=homeTeam;
		this.guestTeam=guestTeam;
	}

    public boolean started(){
    	
    	Date d = new Date(System.currentTimeMillis());
    	return d.after(startTime);
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

	@OneToMany(mappedBy = "game")
	public Set<Condition> getConditions() {
		return conditions;
	}

	public void setConditions(Set<Condition> conditions) {
		this.conditions = conditions;
	}

	@Temporal(TemporalType.DATE)
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public int getHomeTeam() {
		return homeTeam;
	}

	public void setHomeTeam(int homeTeam) {
		this.homeTeam = homeTeam;
	}

	public int getGuestTeam() {
		return guestTeam;
	}

	public void setGuestTeam(int guestTeam) {
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
