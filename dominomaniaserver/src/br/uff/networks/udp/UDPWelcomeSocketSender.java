/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.networks.udp;

/**
 *
 * @author Gustavo
 */
public interface UDPWelcomeSocketSender extends UDPProtocolSender {
    public void setPortToSend(int port);
    public void setIPtoSend(byte[] ip);
}
