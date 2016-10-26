package br.uff.networks.domino_mania.model;

import java.io.IOException;
import java.util.Arrays;

import br.uff.networks.domino_mania.Default;
import br.uff.networks.domino_mania.model.movements.InvalidMovementException;
import br.uff.networks.domino_mania.model.movements.PlayerMovement;

public abstract class DominoGame {

    protected Table table;
    protected Deck deck;
    protected Player[] allPlayers;
    protected ScoreControll score;
    protected CyclicIterator<Player> iterator;

    public void startGame() throws IOException {
        allPlayers = createPlayers();
        setupGame();
        while(!gameOver()){
            setupRound();
            while(!roundOver()){
        	Player current = iterator.next();
        	PlayerMovement movement = nextMovement(current);
        	score.setCurrentPlayer(current);
        	try{
        	    movement.execute(current, table);
        	    handleValidMovement(current);
        	    if(movement.getTile() != null)
        		score.setLastTile(movement.getTile());
        	}catch(InvalidMovementException e){
        	    handleInvalidMovement(current, e);
        	    iterator.previous();
        	}
            }
            score.verifyScore();
            finalizeRound();
        }
        finalizeGame();
    }

    protected abstract Player[] createPlayers() throws IOException;
    
    protected void setupGame(){
	table = new Table();
	score = new ScoreControll(table);
    }
    
    private boolean gameOver() {
	return score.getWinnerTeam() != -1;
    }
    
    protected void setupRound(){
	deck = new Deck();
	table.clear();
        table.addLeft(deck.draw());
        score = new ScoreControll(table);
	distributeHands();
	sortPlayers(allPlayers);
	iterator = new CyclicIterator<>(allPlayers);
    }
    
    private void distributeHands() {
        for (Player player : allPlayers) {
            Hand hand = player.getHand();
            hand.asList().clear();
            for (int i = 0; i < Default.INITIAL_HAND_SIZE; i++) {
                hand.add(deck.draw());
            }
        }
    }

    private void sortPlayers(Player[] array) {
	if(array.length == 0)
	    return;
        Arrays.sort(array, new HandCriteria());
        int i = 0;
        for(Player player : array){
            player.setTeam(i);
            i = 1 - i;
        }
    }
    
    private boolean roundOver(){
	for (Player player : allPlayers)
            if (player.getHand().isEmpty())
                return true;
        return false; 
    }
    
    protected abstract PlayerMovement nextMovement(Player player);
    
    protected abstract void handleValidMovement(Player currentPlayer);
    
    protected abstract void handleInvalidMovement(Player currentPlayer, InvalidMovementException e);
    
    protected abstract void finalizeGame();
    
    protected abstract void finalizeRound();

}
