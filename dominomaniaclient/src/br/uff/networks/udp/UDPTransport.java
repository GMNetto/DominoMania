/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.networks.udp;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.uff.networks.Transport;

/**
 *
 * @author Casa
 */
public class UDPTransport implements Transport {

    private UDPProtocolReceiver receiver;
    private UDPProtocolSender sender;
    private byte[] hostIP;
    private int portToListen, portToSend, portOfSender;
    private boolean open;

    public UDPTransport(byte[] hostIP, int portToListen, int portToSend,int portOfSender) {
        this.hostIP = hostIP;
        this.portToListen = portToListen;
        this.portToSend = portToSend;
    }

    @Override
    public void open() {
        try {
            sender=new UDPOurProtocolSender(hostIP, portToSend,portOfSender);
            receiver=new UDPOurProtocolReceiver(portToListen);
            open=true;
        } catch (UnknownHostException | SocketException ex) {
            Logger.getLogger(UDPTransport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void close() {
        open = false;
        receiver.closeConnection();
        sender.closeConnection();
    }

    @Override
    public String receive() {
        return receiver.receive();
    }

    @Override
    public void send(String message) {
       sender.send(message);
    }

    @Override
    public boolean isOpen() {
        return open;
    }
}
