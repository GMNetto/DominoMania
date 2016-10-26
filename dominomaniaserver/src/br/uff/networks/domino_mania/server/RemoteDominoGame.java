package br.uff.networks.domino_mania.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import br.uff.networks.Transport;
import br.uff.networks.domino_mania.MessagesToClient;
import br.uff.networks.domino_mania.MessagesToServer;
import br.uff.networks.domino_mania.model.DominoGame;
import br.uff.networks.domino_mania.model.Player;
import br.uff.networks.domino_mania.model.Tile;
import br.uff.networks.domino_mania.model.movements.DrawMovement;
import br.uff.networks.domino_mania.model.movements.InvalidMovementException;
import br.uff.networks.domino_mania.model.movements.NullMovement;
import br.uff.networks.domino_mania.model.movements.PlayLeft;
import br.uff.networks.domino_mania.model.movements.PlayRight;
import br.uff.networks.domino_mania.model.movements.PlayerMovement;

public class RemoteDominoGame extends DominoGame implements Runnable {

    private final Map<Player, Transport> remotePlayers;
    private final Transport[] transports;

    public RemoteDominoGame(Transport[] transports) {
        super();
        remotePlayers = new HashMap<>();
        this.transports = transports;
    }
    
    @Override
    protected Player[] createPlayers() throws IOException {
        Player[] playerArray = new Player[transports.length];
        for (int i = 0; i < playerArray.length; i++) {
            Transport client = transports[i];
            Player newPlayer = new Player(readName(client));
            playerArray[i] = newPlayer;
            remotePlayers.put(newPlayer, client);
        }
        return playerArray;
    }

    private String readName(Transport client) {
        return client.receive();
    }

    @Override
    protected void setupGame() {
	super.setupGame();
        for (Player player : allPlayers)
            broadcast(player.toJSON());
    }
    
    @Override
    protected PlayerMovement nextMovement(Player player) {
        updateClientData(player);
        return readMovement(player);
    }

    private void updateClientData(Player currentPlayer) {
        String tableJSON = table.toJSON();
        String playersJSON = createPlayersJSON();
        for (Player player : remotePlayers.keySet()) {
            Transport transport = remotePlayers.get(player);
            String turnMessage = player == currentPlayer ? MessagesToClient.YOUR_TURN
                    : MessagesToClient.turn(currentPlayer.toJSON());
            String handJSON = player.getHand().toJSON();
            transport.send(turnMessage);
            transport.send(playersJSON);
            transport.send(handJSON);
            transport.send(tableJSON);
        }
    }
    
    private String createPlayersJSON(){
	JSONObject obj = new JSONObject();
	JSONArray array = new JSONArray();
	for(Player player : allPlayers){
	    JSONObject item = new JSONObject();
	    item.put("name", player.getName());
	    item.put("team", player.getTeam());
	    item.put("hand", player.getHand().asList().size());
	    array.put(item);
	}
	obj.put("players", array);
	return obj.toString();
    }

    private PlayerMovement readMovement(Player player) {
        Transport transport = remotePlayers.get(player);
        String command = transport.receive();
        if (command.equalsIgnoreCase(MessagesToServer.PASS))
            return new NullMovement();
        
        if(command.equalsIgnoreCase(MessagesToServer.DRAW))
            return new DrawMovement(deck, iterator);

        Tile tile = new Tile();
        tile.fromJSON(transport.receive());
        if (!player.getHand().contains(tile)) {
            return new NullMovement();
        }
        if (command.equalsIgnoreCase(MessagesToServer.PLAY_RIGHT)) {
            return new PlayRight(tile);
        }
        if (command.equalsIgnoreCase(MessagesToServer.PLAY_LEFT)) {
            return new PlayLeft(tile);
        }
        return new NullMovement();
    }

    @Override
    protected void handleInvalidMovement(Player currentPlayer, InvalidMovementException e) {
        remotePlayers.get(currentPlayer).send(MessagesToClient.INVALID_MOVEMENT);
    }

    @Override
    protected void handleValidMovement(Player currentPlayer) {
        remotePlayers.get(currentPlayer).send(MessagesToClient.VALID_MOVEMENT);
    }
    
    @Override
    protected void finalizeRound() {
	broadcast(MessagesToClient.ROUND_OVER);
	broadcast(score.toJSON());
    }
    
    @Override
    protected void finalizeGame() {
	int winner = score.getWinnerTeam();
        for (Transport transport : transports) {
            try {
        	transport.send(MessagesToClient.gameOver(winner));
                transport.close();
            } catch (IOException e) {
            }
        }
    }
    
    private void broadcast(String message){
	for(Player player : remotePlayers.keySet())
	    remotePlayers.get(player).send(message);
    }

    @Override
    public void run() {
        try {
            startGame();
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
