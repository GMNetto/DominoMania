package br.uff.networks.domino_mania.model.movements;

import br.uff.networks.domino_mania.model.CyclicIterator;
import br.uff.networks.domino_mania.model.Deck;
import br.uff.networks.domino_mania.model.Player;
import br.uff.networks.domino_mania.model.Table;
import br.uff.networks.domino_mania.model.Tile;

public class DrawMovement implements PlayerMovement {
    
    private final Deck deck;
    private final CyclicIterator<Player> iterator;
    
    public DrawMovement(Deck deck, CyclicIterator<Player> iterator) {
	this.deck = deck;
	this.iterator = iterator;
    }
    
    @Override
    public Tile getTile() {
	return null;
    }

    @Override
    public void execute(Player player, Table table)
	    throws InvalidMovementException {
	if(!deck.isEmpty())
	    player.getHand().add(deck.draw());
	iterator.previous();
    }

}
