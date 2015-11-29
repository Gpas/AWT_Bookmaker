package model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name="game")
public class Game {

	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private int id;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "owner")
	private User owner;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name ="startTime")
    private Date startTime;

	@Column(name ="homeTeam")
    private int homeTeam;
	@Column(name ="guestTeam")
	private int guestTeam;

	@OneToMany(mappedBy = "game")
	private Set<Condition> conditions = new HashSet<Condition>();

	public Game(){

	}

	public Game(Date start, User owner, int homeTeam, int guestTeam){
		this.startTime=start;
		this.owner = owner;
		this.homeTeam=homeTeam;
		this.guestTeam=guestTeam;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

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

	public Set<Condition> getConditions() {
		return conditions;
	}

	public void setConditions(Set<Condition> conditions) {
		this.conditions = conditions;
	}
}
