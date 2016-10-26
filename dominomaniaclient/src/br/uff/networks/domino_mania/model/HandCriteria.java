package br.uff.networks.domino_mania.model;

import java.util.Comparator;

class HandCriteria implements Comparator<Player> {

    @Override
    public int compare(Player a, Player b) {
        Tile aTile = getHighestDoubleTile(a);
        Tile bTile = getHighestDoubleTile(b);
        if(aTile == null && bTile == null)
            return 0;
        int comp = compareNullity(aTile, bTile);
        if (comp == 0) 
            return compareInt(aTile.getLeft(), bTile.getLeft());
        return comp;
    }

    private Tile getHighestDoubleTile(Player player) {
        Tile max = null;
        for (Tile tile : player.getHand()) {
            if (isDouble(tile)) {
                if (isGreaterThan(tile, max)) {
                    max = tile;
                }
            }
        }
        return max;
    }

    private boolean isDouble(Tile tile) {
        return tile.getLeft() == tile.getRight();
    }

    private boolean isGreaterThan(Tile a, Tile b) {
        return b == null || b.getLeft() < b.getLeft();
    }

    private int compareNullity(Object a, Object b) {
        if (a != null && b != null) 
            return 0;
        if (a == null) 
            return -1;
        return 1;
    }

    private int compareInt(int a, int b) {
        if (a == b) {
            return 0;
        }
        if (a > b) {
            return 1;
        }
        return -1;
    }
}
