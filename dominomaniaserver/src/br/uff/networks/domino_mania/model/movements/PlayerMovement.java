package br.uff.networks.domino_mania.model.movements;

import br.uff.networks.domino_mania.model.Player;
import br.uff.networks.domino_mania.model.Table;
import br.uff.networks.domino_mania.model.Tile;

public interface PlayerMovement{
    
    void execute(Player player, Table table) throws InvalidMovementException;

    Tile getTile();

}
