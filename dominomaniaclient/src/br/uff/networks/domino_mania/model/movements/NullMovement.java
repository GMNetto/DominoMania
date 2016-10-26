package br.uff.networks.domino_mania.model.movements;

import br.uff.networks.domino_mania.model.Player;
import br.uff.networks.domino_mania.model.Table;
import br.uff.networks.domino_mania.model.Tile;

public class NullMovement implements PlayerMovement {
    
    @Override
    public Tile getTile() {
        return null;
    }

    @Override
    public void execute(Player player, Table table){
    }
    
}
