package br.uff.networks.domino_mania.model;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class Hand implements JSONSerializable, Iterable<Tile> {
    
    private static final String TILES_ATT = "tiles";
    private final List<Tile> tiles;
    
    public Hand() {
	tiles = new LinkedList<>();
    }
    
    public void add(Tile tile){
	tiles.add(tile);
    }
    
    public boolean remove(Tile tile){
	return tiles.remove(tile);
    }
    
    public boolean isEmpty(){
	return tiles.isEmpty();
    }
    
    @Override
    public Iterator<Tile> iterator() {
	return tiles.iterator();
    }

    @Override
    public String toJSON() {
	JSONObject obj = new JSONObject();
	JSONArray tilesObj = new JSONArray();
	for(Tile tile : tiles)
	    tilesObj.put(new JSONObject(tile.toJSON()));
	obj.put(TILES_ATT, tilesObj);
	return obj.toString();
    }

    @Override
    public void fromJSON(String json) {
	JSONObject obj = new JSONObject(json);
	JSONArray tilesObj = obj.getJSONArray(TILES_ATT);
	tiles.clear();
	for(int i = 0; i < tilesObj.length(); i++){
	    Tile tile = new Tile();
	    tile.fromJSON(tilesObj.getJSONObject(i).toString());
	    tiles.add(tile);
	}
    }

    public boolean contains(Tile tile) {
	return tiles.contains(tile);
    }
    

    public List<Tile> asList(){
	return Collections.unmodifiableList(tiles);
    }

}
