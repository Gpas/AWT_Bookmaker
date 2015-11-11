package model;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
public class Bookmaker extends User {

	@OneToMany
	private List<Game> games = new ArrayList<>();
	
	public List<Game> getGames(){
		return Collections.unmodifiableList(games);
	}

	public void setGames(List<Game> games){
		this.games = games;
	}

	public Bookmaker(){

	}

	public Bookmaker(String username, String firstname, String lastname){
		super(username, firstname, lastname);
		setIsBookmaker(true);
	}



}
