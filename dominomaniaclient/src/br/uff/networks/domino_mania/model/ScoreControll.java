/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.networks.domino_mania.model;

import org.json.JSONObject;

/**
 * 
 * @author Gustavo
 */
public class ScoreControll implements JSONSerializable {

    private static final String TEAM_0_POINTS_ATT = "team0Points";
    private static final String TEAM_1_POINTS_ATT = "team1Points";
    private Tile lastTile;
    private Table table;
    private Player currentPlayer;
    private int team1Points;
    private int team0Points;

    public void setCurrentPlayer(Player currentPlayer) {
	this.currentPlayer = currentPlayer;
    }

    public void setLastTile(Tile lastTile) {
	this.lastTile = lastTile;
    }

    public ScoreControll(Table table) {
	this.table = table;
    }

    public ScoreControll() {
    }

    public void verifyScore() {
	if (lastTile.isWagon() && lastTile.getLeft() == table.getRight()
		&& lastTile.getLeft() == table.getLeft()) {
	    assingScore(currentPlayer.getTeam(), 4);
	    // caso de 4 pontos
	} else if (table.getRight() == table.getLeft()) {
	    // caso 3 pontos
	    if (table.getRight() == table.getLeft()) {
		assingScore(currentPlayer.getTeam(), 3);
	    }
	} else if (lastTile.isWagon()) {
	    // caso 2 pontos
	    assingScore(currentPlayer.getTeam(), 2);
	} else {
	    // caso 1 ponto
	    assingScore(currentPlayer.getTeam(), 1);
	}
    }

    private void assingScore(int team, int score) {
	if (team == 0) {
	    team0Points += score;
	} else {
	    team1Points += score;
	}
    }

    public int getWinnerTeam() {
	if (team0Points == 7)
	    return 0;
	if (team1Points == 7)
	    return 1;
	return -1;
    }

    @Override
    public String toJSON() {
	JSONObject obj = new JSONObject();
	obj.put(TEAM_0_POINTS_ATT, team0Points);
	obj.put(TEAM_1_POINTS_ATT, team1Points);
	return obj.toString();
    }

    @Override
    public void fromJSON(String json) {
	JSONObject obj = new JSONObject(json);
	team0Points = obj.getInt(TEAM_0_POINTS_ATT);
	team1Points = obj.getInt(TEAM_1_POINTS_ATT);
    }

    public int getTeam0Points() {
	return team0Points;
    }
    
    public int getTeam1Points() {
	return team1Points;
    }
}
