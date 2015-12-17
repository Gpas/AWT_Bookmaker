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
	@Column(name = "[params]")
	private String params;
	@Column(name = "odd")
	private int odd;

	@OneToMany(mappedBy="condition")
	private Set<Bet> bets = new HashSet<Bet>();
	
	public Condition(){

	}

	public Condition(Game game, int textId, int leadingTeamId, int odd){
		this.game = game;
		this.textId = textId;
		this.leadingTeamId = leadingTeamId;
		this.params = "";
		this.odd = odd;
	}

	public Condition(Game game, int textId, int leadingTeamId, String params, int odd){
		this.game = game;
		this.textId = textId;
		this.leadingTeamId = leadingTeamId;
		this.params = params;
		this.odd = odd;
	}

	public int[] getParamsAsInts(){
		if(params != ""){
			String temp[] =params.split(",");
			int[] params = new int[temp.length];
			for( int i = 0; i < temp.length; i++){
				params[i] = Integer.parseInt(temp[i]);
			}
			return params;
		}
		return null;
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

	public int getOdd() {
		return odd;
	}

	public void setOdd(int odd) {
		this.odd = odd;
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

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}
}
