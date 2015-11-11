package model;

import java.util.Collections;
import java.util.List;


public class Bookmaker extends User {
	
	private List<Game> games;
	
	public List<Game> getGames(){
		return Collections.unmodifiableList(games);
	}
}
