package br.uff.networks.domino_mania.model;

import org.json.JSONObject;

public class Player implements JSONSerializable {

    private static final String NAME_ATT = "name";
    private static final String TEAM_ATT = "team";
    private String name;
    private final Hand hand;
    private int team;

    public Player(String name) {
        this.name = name;
        hand = new Hand();
    }

    public Player() {
        hand = new Hand();
    }

    public String getName() {
        return name;
    }

    public Hand getHand() {
        return hand;
    }

    public void setTeam(int team) {
        this.team = team;
    }

    public int getTeam() {
        return team;
    }

    @Override
    public String toJSON() {
        JSONObject obj = new JSONObject();
        obj.put(NAME_ATT, name);
        obj.put(TEAM_ATT, team);
        return obj.toString();
    }

    @Override
    public void fromJSON(String json) {
        JSONObject obj = new JSONObject(json);
        name = obj.getString(NAME_ATT);
        team = obj.getInt(TEAM_ATT);
    }
}
