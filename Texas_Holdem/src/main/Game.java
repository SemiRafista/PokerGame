package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import exceptions.InvalidNumberOfRankException;
import exceptions.InvalidNumberOfSuitException;
/**
 * 
 * @author Seba
 *
 * TODO:
 * Get players from server
 * 
 */
public class Game {
	
	private List<Player> players;
	private List<Card> tableCards = new ArrayList<Card>();
	public Auction auction;
	
	
	public Game(List<Player> players) throws InvalidNumberOfRankException, 
					     InvalidNumberOfSuitException{
		this.players = new ArrayList<Player>();
		this.players.addAll(players);
	}
	
	
	
	
	public void play() throws InvalidNumberOfRankException,
							  InvalidNumberOfSuitException{
		players.get(0).setSmallBlind(true);
		players.get(1).setSmallBlind(true);
		
		while( players.size() > 1 ){
			round();
		}
		
		//TODO : implement what's goin'  on after determining winner
	
	}
	
	
	
	
	public void round() throws InvalidNumberOfRankException, 
							   InvalidNumberOfSuitException{
		
		List<Player> winnersList = new ArrayList<Player> ();
	
		distributeCards();
		makePlayersInGame();
		
		auction = new Auction(players);
		
		for( int i = 0 ; i <= 3 ; ++i){
			
			auction.StartAuction(i);
			putCardsOnTheTable(i);
		}
		
		 winnersList = createSortedWinnersList();
		 giveGainToWinners(winnersList);
		 removeLosers(); // smierc frajerom hehe
		 changeSmallBlind();
		 tableCards.clear();
		 Deck.getInstance().initializeCards();
		 
	}
	
	
	
	
	public List<Player> getPlayers(){
		return players;
	}
	
	
	
	public List<Card> getTableCards(){
		return tableCards;
	}
	
	
	
	
	public void setTableCards(List<Card> tableCards){
		this.tableCards = tableCards;
	}
	
	
	
	private void changeSmallBlind() {
		int counter = 0;
		
		while ( counter < players.size() && players.get(counter).isSmallBlind() == false){
			counter++;
		}
		
		players.get(counter).setSmallBlind(false);
		if( counter + 1 == players.size() ){
			counter = 0;
		}
		else
			counter++;
		
		players.get(counter).setSmallBlind(true);
		players.get(counter).setBigBlind(false);
		
		if( counter + 1 == players.size() ){
			counter = 0 ;
		}
		else
			counter++;
		
		players.get(counter).setBigBlind(true);
		
		
	}




	public void makePlayersInGame() {
		for( Player player : players){
			player.setInGame(true);
		}
	}




	private void giveGainToWinners(List<Player> winnersList) {
		int[] wagers = new int[players.size()+7];
		int wager = 0;
		int lowerRate = 0;
		int higherRate = 0;
		int helpingPot = 0;
		double power = 0 ;
		List<Player> helpingList = new ArrayList<Player>();
		List<Player> secondHelpingList = new ArrayList<Player>();
		
		for( int i = 0 ; i < players.size() ; ++i ){
			wagers[i] = players.get(i).getCurrentBet();
		}
		
		while( auction.getCurrentPot() > 0 ){
			power = winnersList.get(0).getPower();
			
			helpingList = createHelpingList(power, winnersList);
			
			lowerRate = 0 ;
			higherRate = helpingList.get(0).getCurrentBet(); 
			
			while(helpingList.isEmpty() == false ){
				helpingPot = 0;
				higherRate = helpingList.get(0).getCurrentBet(); 
				
				secondHelpingList = createSecondHelpingList(helpingList , higherRate);
				
				for( int i = 0 ; i < players.size() ; ++i ){
					if( wagers[ i ] > lowerRate ){
						if( wagers[ i ] > higherRate ){
							helpingPot += higherRate - lowerRate ;
						}
						else{
							helpingPot += wagers[ i ] - lowerRate;
						}
					}
				}
				
				auction.setCurrentBet(auction.getCurrentPot()-helpingPot);
				
				for( Player player : secondHelpingList){
					player.setPlayerTokens(player.getPlayerTokens()+helpingPot/secondHelpingList.size());
				}
				
				secondHelpingList.clear();
				
				lowerRate = higherRate;
				
			}
		}
	}




	private List<Player> createSecondHelpingList(List<Player> helpingList, int higherRate) {
		List<Player> secondHelpingList = new ArrayList<Player>();
		
		for( Player player : helpingList){
			if( player.getCurrentBet() == higherRate ){
				secondHelpingList.add(player);
			}
		}
		
		for( Player player : secondHelpingList ){
			helpingList.remove(player);
		}
		
		return secondHelpingList;
	}




	private List<Player> createHelpingList(double power, List<Player> winnersList) {
		
		List <Player> helpingList = new ArrayList<Player>();
		
		for( Player player : players ){
			if( player.getPower() == power ){
				helpingList.add(player);
			}
		}
		
		for( Player player : helpingList ){
			winnersList.remove(player);
		}
		
		return helpingList;
	}




	public List<Player> createSortedWinnersList() {
		List<Player> winners = new ArrayList<Player>();
		double max = 0;
		
		for( Player player : players){
			if( player.isInGame() ){
				HandDeterminer.determineHand(player.getHand(), tableCards, player); 
				winners.add(player);
			}
		}
		
		Collections.sort(winners, new SortPlayer() );
	
		return winners;
	}




	public void putCardsOnTheTable(int i) throws InvalidNumberOfRankException, 
												  InvalidNumberOfSuitException{
		if( i == 0 ){
			tableCards.addAll( Deck.getInstance().giveCards( 3 ));
		}
		else{
			tableCards.addAll( Deck.getInstance().giveCards( 1 ));
		}
		
	}

	
	
	
	public void distributeCards() throws InvalidNumberOfRankException, 
										  InvalidNumberOfSuitException {
		for( Player player : players){
			player.setHand( Deck.getInstance().giveCards(2) );
		}
	}
	
	
	

	private void removeLosers() {
		for( Player player : players){
			if( player.getPlayerTokens() == 0 ){
				players.remove(player);
			}
		}
	}
	
	
	

	private static class SortPlayer implements Comparator<Player>{

		@Override
		public int compare(Player o1, Player o2) {
			if(o1.getPower() < o2.getPower()){
				return 1;
			}
			else if( o1.getPower() > o2.getPower() ){
				return -1;
			}
			else {
				if( o1.getCurrentBet() > o2.getCurrentBet() ){
					return 1;
				}
				else if( o1.getCurrentBet() < o2.getCurrentBet() ){
					return -1;
				}
				else
					return 0;
			}
		}
		
	}
	
	
	
	
	
	
	
}