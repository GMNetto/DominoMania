package br.uff.networks.domino_mania.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import br.uff.networks.Transport;
import br.uff.networks.domino_mania.Default;
import br.uff.networks.domino_mania.MessagesToClient;
import br.uff.networks.domino_mania.MessagesToServer;
import br.uff.networks.domino_mania.model.Hand;
import br.uff.networks.domino_mania.model.Player;
import br.uff.networks.domino_mania.model.ScoreControll;
import br.uff.networks.domino_mania.model.Table;
import br.uff.networks.domino_mania.model.Tile;
import br.uff.networks.tcp.TCPTransport;
import br.uff.networks.udp.UDPWelcomeClient;

public class DominoManiaClientConsole {

    private final Scanner in;
    private Transport server;
    private Player[] allPlayers;
    private Player player;
    private Table table;
    private String baseMessage;
    private String otherPlayers;

    public DominoManiaClientConsole() {
	in = new Scanner(System.in);
	baseMessage = "";
	table = new Table();
    }

    public void start() {
	header();
	try {
	    connectToServer();
	    player = new Player(readName());
	    server.send(player.getName());
	    readAllPlayers();
	    while (true) {
		clear();
		baseMessage = server.receive();
		if(gameOver())
		    break;
		if(roundOver())
		    continue;
		readNewData();
		displayData();
		sendMovement();
	    }
	    server.close();
	} catch (Exception e) {
	    e.printStackTrace();
	    System.exit(1);
	}
    }

    private void header() {
	System.out.println("<Domino Mania>");
    }

    private void connectToServer() throws UnknownHostException, IOException {
	if (readProtocolOption())
	    server = createTCPTransport();
	else
	    server = createUDPTransport();
    }

    private boolean readProtocolOption() {
	System.out.println("escolha o protocolo de transporte:\n1-TCP\n2-UDP");
	return in.nextInt() == 1;
    }

    private Transport createTCPTransport() throws UnknownHostException,
	    IOException {
	Socket socket = new Socket(Default.SERVER_IP, Default.SERVER_TCP_PORT);
	Transport transport = new TCPTransport(socket);
	transport.open();
	return transport;
    }

    private Transport createUDPTransport() throws UnknownHostException,
	    IOException {
	byte[] adress = InetAddress.getByName(Default.SERVER_IP).getAddress();
	UDPWelcomeClient welcome = new UDPWelcomeClient(adress, Default.SERVER_UDP_WELCOME_PORT);
	Transport transport = welcome.getClientTransport();
	transport.open();
	return transport;
    }

    private String readName() {
	System.out.println("Nome: ");
	return in.next();
    }

    private void readAllPlayers() {
	allPlayers = new Player[Default.MATCH_SIZE];
	for (int i = 0; i < allPlayers.length; i++) {
	    allPlayers[i] = new Player();
	    allPlayers[i].fromJSON(server.receive());
	}
    }

    private void clear() {
	System.out.println("----------------------------------");
    }
    
    private boolean gameOver(){
	if(baseMessage.equals(MessagesToClient.GAME_OVER_HEAD)){
	    System.out.println("Fim do jogo\nEquipe" + server.receive() + " venceu!");
	    return true;
	}
	return false;
    }
    
    private boolean roundOver(){
	if(baseMessage.equals(MessagesToClient.ROUND_OVER)){
	    ScoreControll score = new ScoreControll();
	    score.fromJSON(server.receive());
	    System.out.println("final do round\nEquipe 0:" + score.getTeam0Points()+"\nEquipe 1:" +score.getTeam1Points());
	    return true;
	}
	return false;
    }

    private void readNewData() {
	otherPlayers = parseOtherPlayersJSON(server.receive());
	player.getHand().fromJSON(server.receive());
	table.fromJSON(server.receive());
    }
    
    private String parseOtherPlayersJSON(String json){
	StringBuilder string = new StringBuilder();
	JSONObject obj = new JSONObject(json);
	JSONArray array = obj.getJSONArray("players");
	for(int i = 0; i < array.length(); i++){
	    JSONObject item = array.getJSONObject(i);
	    string.append("(");
	    string.append(item.getString("name"));
	    string.append(" da equipe ");
	    string.append(item.getInt("team"));
	    string.append(" tem ");
	    string.append(item.getInt("hand"));
	    string.append(" peça(s))  ");
	}
	return string.toString();
    }

    private void displayData() {
	System.out.println(otherPlayers);
	display(player.getHand());
	display(table);
    }

    private void display(Hand hand) {
	System.out.println("mão: ");
	int index = 0;
	for (Tile tile : hand.asList())
	    System.out.println(++index + " - [" + tile.getLeft() + "|"
		    + tile.getRight() + "]");
    }

    private void display(Table table) {
	System.out.println("mesa: ");
	for (Tile tile : table.asList())
	    System.out
		    .print("[" + tile.getLeft() + "|" + tile.getRight() + "]");
	System.out.println();
    }

    private void sendMovement() {
	if (!baseMessage.equals(MessagesToClient.YOUR_TURN)){
	    Player other = new Player();
	    other.fromJSON(baseMessage);
	    System.out.println("turno de " + other.getName() + " da equipe " + other.getTeam());
	    return;
	}
	System.out
		.println("sua vez\np - passar\nc - comprar\n(e - esquerda ou d - direita) índice da peça");
	String movement = in.next();
	if (movement.equals("p")) {
	    server.send(MessagesToServer.PASS);
	    server.receive();
	    return;
	}
	if(movement.equals("c")){
	    server.send(MessagesToServer.DRAW);
	    server.receive();
	    return;
	}
	try {
	    int i = in.nextInt();
	    Tile tile = player.getHand().asList().get(i - 1);
	    if (movement.equals("e"))
		server.send(MessagesToServer.PLAY_LEFT);
	    else if (movement.equals("d"))
		server.send(MessagesToServer.PLAY_RIGHT);
	    else
		throw new Exception();
	    server.send(tile.toJSON());
	    String confirm = server.receive();
	    if (confirm.equals(MessagesToClient.INVALID_MOVEMENT))
		throw new Exception();
	} catch (Exception e) {
	    System.err.println("movimento inválido, tente outra vez");
	}
    }
}
