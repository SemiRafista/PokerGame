package main;

import java.util.List;

public class Player{

	private final String playerName;
	private List<Card> hand;
	private List<Card> winningHand;
	private List<Card> drawCards;
	private boolean isInGame;
	private int playerTokens; //possibly List<Token>, where Token has it's value and amount
	private boolean isSmallBlind;
	private boolean isBigBlind;
	private boolean isDealerButton;
	private int currentBet;
	private int currentAuctionBet;
	private final int playerIndex;
	private boolean stateChanged;
	private String name;
	private double power;
	private AuctionGUI ta;
	
	public Player(String name, int tokens, int index, AuctionGUI ta){
		this.playerName=name;
		this.playerIndex=index;
		this.setPlayerTokens(tokens);
		setInGame(true);
		setHand(null);
		setWinningHand(null);
		setDrawCards(null);
		setCurrentBet(0);
		setBigBlind(false);
		setSmallBlind(false);
		setDealerButton(false);
		setStateChanged(false);
		setName(null);
		setTa(ta);
	}
	
	public Player makePlayer(String name, int tokens, int index, AuctionGUI ta){
		return new Player(name, tokens, index, ta);
	}

////AUCTION METHODS////
	
	public ActionTaken playerState;
		
	public void Check(){
		playerState = ActionTaken.CHECKING;
	}
	
	public void Bet(int betValue){
		setPlayerTokens(getPlayerTokens() - betValue);
		setCurrentBet(betValue);
		playerState = ActionTaken.BETING;
	}
	
	public void Call(int auctionBetValue){
		setCurrentBet(auctionBetValue);
		setPlayerTokens(getPlayerTokens() - auctionBetValue);
		playerState = ActionTaken.CALLING;
	}

	public int Raise(int auctionBetvalue, int riseValue){
		setPlayerTokens(getPlayerTokens() - riseValue - auctionBetvalue);
		setCurrentBet(riseValue + auctionBetvalue);
		playerState = ActionTaken.RISING;
		return riseValue;
	}

	public void Fold(){
		playerState = ActionTaken.FOLDING;
	}
	
	public int AllIn(){
		playerState = ActionTaken.ALLIN;
		setCurrentBet(getPlayerTokens());
		return this.playerTokens;
	}
	
	/**
	 * based on button pressed, calls one of auction methods
	 * used in auction to wait for players action
	 */
	public String getMovement() {
		while(getName()==null){
			
		}
        return name;
	}

////GETTERS AND SETTERS////

	public String getPlayerName() {
		return playerName;
	}

	public List<Card> getHand() {
		return hand;
	}

	public void setHand(List<Card> hand) {
		this.hand = hand;
	}

	public List<Card> getWinningHand() {
		return winningHand;
	}

	public void setWinningHand(List<Card> winningHand) {
		this.winningHand = winningHand;
	}

	public List<Card> getDrawCards() {
		return drawCards;
	}

	public void setDrawCards(List<Card> drawCards) {
		this.drawCards = drawCards;
	}

	public int getPlayerTokens() {
		return playerTokens;
	}

	public void setPlayerTokens(int playerTokens) {
		this.playerTokens = playerTokens;
	}

	public int getCurrentBet() {
		return currentBet;
	}

	public void setCurrentBet(int currentBet) {
		this.currentBet = currentBet;
	}

	public int getCurrentAuctionBet() {
		return currentAuctionBet;
	}

	public void setCurrentAuctionBet(int currentAuctionBet) {
		this.currentAuctionBet = currentAuctionBet;
	}

	public boolean isSmallBlind() {
		return isSmallBlind;
	}

	public void setSmallBlind(boolean isSmallBlind) {
		this.isSmallBlind = isSmallBlind;
	}

	public boolean isBigBlind() {
		return isBigBlind;
	}

	public void setBigBlind(boolean isBigBlind) {
		this.isBigBlind = isBigBlind;
	}

	public boolean isDealerButton() {
		return isDealerButton;
	}

	public void setDealerButton(boolean isDealerButton) {
		this.isDealerButton = isDealerButton;
	}

	public boolean isInGame() {
		return isInGame;
	}

	public void setInGame(boolean isInGame) {
		this.isInGame = isInGame;
	}
	
	public int getPlayerIndex() {
		return playerIndex;
	}

	public boolean isStateChanged() {
		return stateChanged;
	}

	public void setStateChanged(boolean stateChanged) {
		this.stateChanged = stateChanged;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPower() {
		return power;
	}

	public void setPower(double power) {
		this.power = power;
	}

	public AuctionGUI getTa() {
		return ta;
	}

	public void setTa(AuctionGUI ta) {
		this.ta = ta;
	}
}
