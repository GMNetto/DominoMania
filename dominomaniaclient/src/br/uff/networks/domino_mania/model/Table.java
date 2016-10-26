package br.uff.networks.domino_mania.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class Table implements JSONSerializable {

    private static final String TILES_ATT = "tiles";
    private final List<Tile> tiles;

    public Table() {
        tiles = new LinkedList<>();
    }

    public boolean addLeft(Tile tile) {
        boolean inserted = false;
        if (tiles.size() == 0) {
            tiles.add(0, tile);
            inserted = true;
            return inserted;
        }
        if (tiles.get(0).getLeft() == tile.getRight()) {
            tiles.add(0, tile);
            inserted = true;
            return inserted;
        }
        if (tiles.get(0).getLeft() == tile.getLeft()) {
            tile.invertTile();
            tiles.add(0, tile);
            inserted = true;
            return inserted;
        }
        return inserted;
    }

    public boolean addRight(Tile tile) {
        boolean inserted = false;
        if (tiles.isEmpty()) {
            tiles.add(tile);
            inserted = true;
            return inserted;
        }
        if (tiles.get(tiles.size() - 1).getRight() == tile.getLeft()) {
            tiles.add(tile);
            inserted = true;
            return inserted;
        }
        if (tiles.get(tiles.size() - 1).getRight() == tile.getRight()) {
            tile.invertTile();
            tiles.add(tile);
            inserted = true;
            return inserted;
        }
        return inserted;
    }

    public void clear() {
        if (!tiles.isEmpty()) {
            tiles.removeAll(tiles);
        }
    }

    @Override
    public String toJSON() {
        JSONObject obj = new JSONObject();
        JSONArray array = new JSONArray();
        for(Tile tile : tiles)
            array.put(new JSONObject(tile.toJSON()));
        obj.put(TILES_ATT, array);
        return obj.toString();
    }

    @Override
    public void fromJSON(String json) {
        JSONObject obj = new JSONObject(json);
        JSONArray tilesObj = obj.getJSONArray(TILES_ATT);
        tiles.clear();
        for (int i = 0; i < tilesObj.length(); i++) {
            Tile tile = new Tile();
            tile.fromJSON(tilesObj.getJSONObject(i).toString());
            tiles.add(tile);
        }
    }
    
    public List<Tile> asList() {
	return Collections.unmodifiableList(tiles);
    }
    public int getRight(){
        return tiles.get(tiles.size()-1).getRight();
    }
    
    public int getLeft(){
        return tiles.get(0).getLeft();
    }

    
}
