package br.uff.networks.domino_mania.model;

import java.util.ArrayList;
import java.util.Random;

public class Deck {

    private final ArrayList<Tile> tiles;
    private final Random random;

    public Deck() {
        random = new Random();
        tiles = new ArrayList<>();
        fill();
    }

    private void fill() {
        for (int i = 6; i >= 0; i--) 
            for (int j = i; j >= 0; j--)
                tiles.add(new Tile(i, j));
    }

    public boolean isEmpty() {
        return tiles.size() == 0;
    }

    public Tile draw() {
        int index = random.nextInt(tiles.size());
        return tiles.remove(index);
    }
}
