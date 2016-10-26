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
public interface UDPWelcomeReceiver extends UDPReceiver{
    public int getPortOfSender();
    public byte[] getIPOfSender();
}
