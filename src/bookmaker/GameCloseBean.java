package bookmaker;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import model.Game;

@ManagedBean
@SessionScoped
public class GameCloseBean implements Serializable{
	
	public GameCloseBean(){}
	
	private int closeGameId = -1;
	private Game closeGame;

	public int getCloseGameId() {
		return closeGameId;
	}
	public void setCloseGameId(int closeGameId) {
		this.closeGameId = closeGameId;
	}
	public Game getCloseGame() {
		return closeGame;
	}
	public void setCloseGame(Game closeGame) {
		this.closeGame = closeGame;
	}
	

}
