/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.networks.udp;

import java.net.SocketException;
import java.net.UnknownHostException;

/**
 *
 * @author Gustavo
 */
public class UDPOurProtocolSender implements UDPWelcomeSocketSender{
    
    private UDPWelcomeSender sender;
    
    UDPOurProtocolSender(byte[] ip,int portToSend) throws UnknownHostException, SocketException{
        sender=new UDPSenderSendWait(ip, portToSend);
    }

    public UDPOurProtocolSender() throws SocketException {
        sender=new UDPSenderSendWait();
    }
    
    

    @Override
    public void send(String message) {
        byte[] data = message.getBytes();
        byte[] dataPacket = new byte[1020];
        int generalindex = 0;
        //System.out.println(data.length / 1020 + ((data.length % 1020) == 0 ? 0 : 1)+" "+data.length);
        sender.send(Util.getBytes(data.length / 1020 + ((data.length % 1020) == 0 ? 0 : 1)));
        while (generalindex < data.length) {
            int indexOfArray = 0;
            for (int i = generalindex; i < data.length && indexOfArray < 1020; i++) {
                dataPacket[indexOfArray] = data[i];
                indexOfArray++;
            }
            for (int i = indexOfArray; i < dataPacket.length; i++) {
                dataPacket[indexOfArray] = 0;
                indexOfArray++;
            }
            generalindex += indexOfArray;
            sender.send(dataPacket);
            dataPacket = new byte[1020];
        }
    }

    @Override
    public void setPortToSend(int port) {
        sender.setPortToSend(port);
    }

    @Override
    public void setIPtoSend(byte[] ip) {
        sender.setIPtoSend(ip);
    }

    @Override
    public void closeConnection() {
        sender.closeConnection();
    }
    
}
