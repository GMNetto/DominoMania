package br.uff.networks.domino_mania.model.movements;

import br.uff.networks.domino_mania.model.Player;
import br.uff.networks.domino_mania.model.Table;
import br.uff.networks.domino_mania.model.Tile;

public class PlayRight implements PlayerMovement {

    private final Tile tile;

    public PlayRight(Tile tile) {
	this.tile = tile;
    }
    
    @Override
    public Tile getTile() {
	return tile;
    }

    @Override
    public void execute(Player player, Table table)
	    throws InvalidMovementException {
	if(player.getHand().contains(tile)){
	    if(table.addRight(tile))
		player.getHand().remove(tile);
	    else
		throw new InvalidMovementException();
	}
	else
	    throw new InvalidMovementException();
    }
}
