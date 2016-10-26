/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.networks.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gustavo
 */
public class UDPSenderSendWait implements Sender,UDPWelcomeSender {

    private int state;
    private Timer timer;
    private int portToSend;
    private InetAddress adress;
    private DatagramSocket socket;
    private byte[] lastMessage;

    public UDPSenderSendWait(byte[] ip, int portToSend, int portToListen) throws UnknownHostException, SocketException {
        adress = InetAddress.getByAddress(ip);
        this.portToSend = portToSend;
        timer = new Timer(1000000000, this);
        state = 0;
        socket = new DatagramSocket(portToListen);
    }

    public UDPSenderSendWait(int portToListen) throws SocketException {
        timer = new Timer(1000000000, this);
        state = 0;
        socket = new DatagramSocket(portToListen);
    }

    @Override
    public void setPortToSend(int port) {
        this.portToSend = port;
    }

    @Override
    public void setIPtoSend(byte[] ip) {
        try {
            adress = InetAddress.getByAddress(ip);
        } catch (UnknownHostException ex) {
            Logger.getLogger(UDPSenderSendWait.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void send(byte[] message) {
        lastMessage=Util.getBytes(message,state);
        DatagramPacket dataPacket = new DatagramPacket(lastMessage, lastMessage.length, adress, portToSend);
        try {
            //System.out.println("Enviando pacote");
                socket.send(dataPacket);
            timer.start();
            byte[] data=new byte[1024];
            dataPacket=new DatagramPacket(data, data.length);
            do{
                socket.receive(dataPacket);
                //System.out.println("rebendo ACK");
            }while(Util.getInt(data)!=state);
            timer.stop();
            state=Math.abs(state-1);
        } catch (IOException ex) {
            Logger.getLogger(UDPSenderSendWait.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

   

    @Override
    public void sendOfLast() {
        DatagramPacket dataPacket = new DatagramPacket(lastMessage, lastMessage.length, adress, portToSend);
        try {
            //System.out.println("reenviando");
            socket.send(dataPacket);
        } catch (IOException ex) {
            Logger.getLogger(UDPSenderSendWait.class.getName()).log(Level.SEVERE, null, ex);
        }
        timer.start();
    }

    @Override
    public void closeConnection() {
        socket.close();
    }

}
