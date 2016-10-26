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

/**
 *
 * @author Gustavo
 */
public class UDPReceiverSendWait implements UDPWelcomeReceiver{

    private DatagramSocket socket;
    private int portOfSender;
    private byte[] ipOfSender;
    private int state;

    public UDPReceiverSendWait(int portToListen) throws SocketException {
        socket = new DatagramSocket(portToListen);
        state = 0;
    }

    @Override
    public byte[] receive() throws IOException {
        do {
            byte[] data = new byte[1024];
            DatagramPacket dataPacket = new DatagramPacket(data, data.length);
            socket.receive(dataPacket);
            data=dataPacket.getData();
            portOfSender = dataPacket.getPort();
            ipOfSender = dataPacket.getAddress().getAddress();
            if (Util.getInt(data) == state) {
                dataPacket = new DatagramPacket(data, data.length, InetAddress.getByAddress(ipOfSender), portOfSender);
                socket.send(dataPacket);
                byte[] answer = new byte[1020];
		System.arraycopy(data, 4, answer, 0, answer.length);
                state=Math.abs(state-1);
                return answer;
            }else{
                byte[] aux=new byte[1020];
                data=Util.getBytes(aux, Math.abs(state-1));
                dataPacket = new DatagramPacket(data, data.length, InetAddress.getByAddress(ipOfSender), portOfSender);
                socket.send(dataPacket);
            }
        } while (true);
    }

    @Override
    public int getPortOfSender() {
        return portOfSender;
    }

    @Override
    public byte[] getIPOfSender() {
        return ipOfSender;
    }

    @Override
    public void closeConnection() {
        socket.close();
    }

}
