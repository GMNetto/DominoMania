package br.uff.networks.domino_mania.model;

import org.json.JSONObject;

public class Tile implements JSONSerializable {

    private static final String LEFT_ATT = "left";
    private static final String RIGHT_ATT = "right";
    private int left;
    private int right;

    public Tile(int left, int right) {
	this.left = left;
	this.right = right;
    }

    public Tile() {
    }

    public int getLeft() {
	return left;
    }

    public int getRight() {
	return right;
    }

    @Override
    public boolean equals(Object obj) {
	if (obj instanceof Tile) {
	    Tile other = (Tile) obj;
	    return (left == other.left && right == other.right) ||
		    (left == other.right && right == other.left);
	}
	return false;
    }

    public void invertTile() {
	int temp;
	temp = this.left;
	this.left = this.right;
	this.right = temp;
    }

    @Override
    public String toJSON() {
	JSONObject obj = new JSONObject();
	obj.put(LEFT_ATT, left);
	obj.put(RIGHT_ATT, right);
	return obj.toString();
    }

    @Override
    public void fromJSON(String json) {
	JSONObject obj = new JSONObject(json);
	left = obj.getInt(LEFT_ATT);
	right = obj.getInt(RIGHT_ATT);
    }

    public boolean isWagon() {
	return left == right;
    }

}
