package model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="condition")
public class Condition {

	public enum State{
	    PENDING,
	    YES,
	    NO
	}

	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private int id;

	@ManyToOne
	@JoinColumn(name="game_id")
	private Game game;

	private int text_id;
	private int odd1, odd2;

	@Enumerated(EnumType.ORDINAL)
	private State state = State.PENDING;

	@OneToMany(mappedBy="condition")
	private Set<Bet> bets = new HashSet<Bet>();
	
	public Condition(){

	}

	public Condition(Game game, int text_id, int odd1, int odd2){
		this.game = game;
		this.text_id = text_id;
		this.odd1 = odd1;
		this.odd2 = odd2;
		this.state = State.PENDING;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Set<Bet> getBets() {
		return bets;
	}

	public void setBets(Set<Bet> bets) {
		this.bets = bets;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public int getText_id() {
		return text_id;
	}

	public void setText_id(int text_id) {
		this.text_id = text_id;
	}

	public int getOdd1() {
		return odd1;
	}

	public void setOdd1(int odd1) {
		this.odd1 = odd1;
	}

	public int getOdd2() {
		return odd2;
	}

	public void setOdd2(int odd2) {
		this.odd2 = odd2;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}
}
