package br.uff.networks.udp;

import java.io.IOException;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UDPOurProtocolReceiver implements UDPWelcomeSocketReceiver{
    
    private UDPWelcomeReceiver receiver;

    public UDPOurProtocolReceiver(int portToListen) throws SocketException {
        receiver=new UDPReceiverSendWait(portToListen);
    }
    
    

    @Override
    public String receive() {
        byte[] data;
        try {
            data = receiver.receive();
            int size = Util.getInt(data);
            StringBuilder stringBuilder = new StringBuilder();

            for (int i = 0; i < size; i++) {
                stringBuilder.append(new String(receiver.receive()));
            }
            return stringBuilder.toString().trim();
        } catch (IOException ex) {
            Logger.getLogger(UDPTransport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public int getPortOfSender() {
        return receiver.getPortOfSender();
    }

    @Override
    public byte[] getIPOfSender() {
        return receiver.getIPOfSender();
    }

    @Override
    public void closeConnection() {
        receiver.closeConnection();
    }
    
}
