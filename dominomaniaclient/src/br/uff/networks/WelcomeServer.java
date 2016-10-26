package br.uff.networks;

import java.io.IOException;

public abstract class WelcomeServer implements Runnable {

    private boolean running;
    private final Event<Transport> onNewClient;

    public WelcomeServer() {
        onNewClient = new Event<>();
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            try {
                Transport client = acceptClient();
                onNewClient.update(client);
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }

    public abstract Transport acceptClient() throws IOException;

    public synchronized void requestStop() {
        running = false;
    }

    public void onNewClient(Observer<Transport> observer) {
        onNewClient.add(observer);
    }
}
