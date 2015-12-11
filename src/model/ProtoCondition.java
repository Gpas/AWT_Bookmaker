package model;

public class ProtoCondition {
	
	private String leadingTeam;
	int	textId,
		oddGain,
		oddBet;
	
	public String getLeadingTeam() {return leadingTeam;}
	public void setLeadingTeam(String leadingTeam) {this.leadingTeam = leadingTeam;}
	public int getTextId() {return textId;}
	public void setTextId(int textId) {this.textId = textId;}
	public int getOddGain() {return oddGain;}
	public void setOddGain(int oddGain) {this.oddGain = oddGain;}
	public int getOddBet() {return oddBet;}
	public void setOddBet(int oddBet) {this.oddBet = oddBet;}
	
	public ProtoCondition(String leadingTeam,int textId,int oddGain,int oddBet){
		this.setLeadingTeam(leadingTeam);
		this.setTextId(textId);
		this.setOddGain(oddGain);
		this.setOddBet(oddBet);
	}
}
