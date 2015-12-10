package model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="[condition]") //condition is a reserved sql keyword, so we need to use []
public class Condition {

	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private int id;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="game")
	private Game game;

	@Column(name = "textId")
	private int textId;
	@Column(name = "leadingTeamId")
	private int leadingTeamId;
	@Column(name = "oddGain")
	private int oddGain;
	@Column(name = "oddBet")
	private int oddBet;

	@OneToMany(mappedBy="condition")
	private Set<Bet> bets = new HashSet<Bet>();
	
	public Condition(){

	}

	public Condition(Game game, int textId, int leadingTeamId, int oddGain, int oddBet){
		this.game = game;
		this.textId = textId;
		this.leadingTeamId = leadingTeamId;
		this.oddGain = oddGain;
		this.oddBet = oddBet;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public int getTextId() {
		return textId;
	}

	public void setTextId(int textId) {
		this.textId = textId;
	}

	public int getOddGain() {
		return oddGain;
	}

	public void setOddGain(int oddGain) {
		this.oddGain = oddGain;
	}

	public int getOddBet() {
		return oddBet;
	}

	public void setOddBet(int oddBet) {
		this.oddBet = oddBet;
	}

	public Set<Bet> getBets() {
		return bets;
	}

	public void setBets(Set<Bet> bets) {
		this.bets = bets;
	}

	public int getLeadingTeamId() {
		return leadingTeamId;
	}

	public void setLeadingTeamId(int leadingTeamId) {
		this.leadingTeamId = leadingTeamId;
	}
}
