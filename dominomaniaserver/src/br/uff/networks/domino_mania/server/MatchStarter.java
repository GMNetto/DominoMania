package br.uff.networks.domino_mania.server;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import br.uff.networks.Transport;
import br.uff.networks.Observer;
import br.uff.networks.domino_mania.Default;

public class MatchStarter implements Observer<Transport> {

    private final Queue<Transport> clients;

    public MatchStarter() {
        clients = new LinkedList<>();
    }

    @Override
    public synchronized void update(Transport obj) {
	try{
	    obj.open();
	    clients.add(obj);
	    nextMatch();
	}catch(IOException e){
	}
    }

    private void nextMatch() {
        if (clients.size() >= Default.MATCH_SIZE)
            new Thread(new RemoteDominoGame(nextClients())).start();
    }

    private Transport[] nextClients() {
        Transport[] nexts = new Transport[Default.MATCH_SIZE];
        int counter = 0;
        while (counter < Default.MATCH_SIZE) {
            Transport client = clients.poll();
            nexts[counter++] = client;
        }
        return nexts;
    }
}
