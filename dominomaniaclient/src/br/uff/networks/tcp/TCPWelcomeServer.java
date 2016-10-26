package br.uff.networks.tcp;

import java.io.IOException;
import java.net.ServerSocket;

import br.uff.networks.Transport;
import br.uff.networks.WelcomeServer;

public class TCPWelcomeServer extends WelcomeServer {

    private final ServerSocket server;

    public TCPWelcomeServer(int port) throws IOException {
        super();
        server = new ServerSocket(port);
    }

    @Override
    public Transport acceptClient() throws IOException {
        return new TCPTransport(server.accept());
    }
}
