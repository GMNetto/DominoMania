package br.uff.networks.udp;

import br.uff.networks.Transport;
import br.uff.networks.WelcomeServer;
import java.io.IOException;
import java.net.DatagramSocket;

import java.net.BindException;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UDPWelcomeServer extends WelcomeServer  {

    private UDPWelcomeSocketReceiver receiver;
    private UDPWelcomeSocketSender sender;
    private int nextPortNumber = 2000;

    public UDPWelcomeServer(int udpReceive) {
        try {
            receiver=new UDPOurProtocolReceiver(udpReceive);
            sender=new UDPOurProtocolSender();
        } catch (SocketException ex) {
            Logger.getLogger(UDPWelcomeServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Transport acceptClient() throws IOException {
        String ip = receiver.receive();
        String port=receiver.receive();
        int portToSend=Integer.parseInt(port);
        sender.setIPtoSend(receiver.getIPOfSender());
        sender.setPortToSend(portToSend);
        int freePort=getNextPortNumber();
        sender.send(freePort+"");
        return new UDPTransport(receiver.getIPOfSender(), freePort, portToSend);
    }

    private int getNextPortNumber() throws SocketException {
        int nextPortToCompare=nextPortNumber-1;
        while (nextPortNumber!=nextPortToCompare) {
            try (DatagramSocket socket = new DatagramSocket(nextPortNumber)) {
                socket.close();
                int resp=nextPortNumber;
                nextPortNumber++;
                return resp;
            } catch (BindException ex) {
                if(nextPortNumber==65535){
                    nextPortNumber=1999;
                }else{
                    nextPortNumber++;
                }
            } 
        }
        throw new SocketException("não tem porta livre");
    }
}
