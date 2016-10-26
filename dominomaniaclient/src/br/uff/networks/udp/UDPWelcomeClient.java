/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.networks.udp;

import java.io.IOException;
import java.net.BindException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.uff.networks.Transport;

/**
 *
 * @author Gustavo
 */
public class UDPWelcomeClient {

    private UDPWelcomeSocketReceiver receiver;
    private UDPWelcomeSocketSender sender;
    private int nextPortNumber = 2000;
    private int portOfSender;
    private int portToListen;

    public UDPWelcomeClient(byte[] ip,int portToSend) {
        try {
            portToListen=getNextPortNumber();
            portOfSender=getNextPortNumber();
            receiver = new UDPOurProtocolReceiver(portToListen);
            sender = new UDPOurProtocolSender(ip, portToSend, portOfSender);
        } catch (SocketException ex) {
            Logger.getLogger(UDPWelcomeClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownHostException ex) {
            Logger.getLogger(UDPWelcomeClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Transport getClientTransport() throws IOException {
        sender.send(String.valueOf(InetAddress.getLocalHost().getHostAddress()));
        sender.send(portToListen+"");
        String tempPort=receiver.receive();
        int portToSend=Integer.parseInt(tempPort);
        sender.setPortToSend(portToSend);   
        sender.closeConnection();
        receiver.closeConnection();
        return new UDPTransport(receiver.getIPOfSender(), portToListen, portToSend,portOfSender);
    }

    private int getNextPortNumber() throws SocketException {
        int nextPortToCompare = nextPortNumber - 1;
        while (nextPortNumber != nextPortToCompare) {
            try (DatagramSocket socket = new DatagramSocket(nextPortNumber)) {
                socket.close();
                int resp = nextPortNumber;
                nextPortNumber++;
                return resp;
            } catch (BindException ex) {
                if (nextPortNumber == 65535) {
                    nextPortNumber = 1999;
                } else {
                    nextPortNumber++;
                }
            }
        }
        throw new SocketException("não tem porta livre");
    }
}
