package br.uff.networks.domino_mania.server;

import java.io.IOException;

import br.uff.networks.WelcomeServer;
import br.uff.networks.tcp.TCPWelcomeServer;
import br.uff.networks.udp.UDPWelcomeServer;

public class ServerLauncher {

    public static void main(String[] args) throws IOException {
        args = new String[]{"55555", "3000"};

        int tcpPort = Integer.parseInt(args[0]);
        int udpPort = Integer.parseInt(args[1]);
        
        WelcomeServer tcpServer = new TCPWelcomeServer(tcpPort);
        WelcomeServer udpServer = new UDPWelcomeServer(udpPort);

        MatchStarter matchStarter = new MatchStarter();
        tcpServer.onNewClient(matchStarter);
        udpServer.onNewClient(matchStarter);

        new Thread(tcpServer).start();
        new Thread(udpServer).start();
    }
}
