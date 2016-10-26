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
public class Util {

    static byte[] getBytes(byte[] message, int state) {
        byte[] resp = new byte[1024];
        System.arraycopy(message, 0, resp, 4, message.length);
        resp[3] = (byte) (state & 0xFF);
        resp[2] = (byte) ((state >> 8) & 0xFF);
        resp[1] = (byte) ((state >> 16) & 0xFF);
        resp[0] = (byte) ((state >> 24) & 0xFF);
        return resp;
    }

    static int getInt(byte[] data) {
        int resp = 0;
        int aux;
        for (int i = 3; i < data.length && i > -1; i--) {
            aux = data[i];
            resp += (aux << ((3 - i) * 8));
        }
        return resp;
    }

    static byte[] getBytes(int id) {
        byte[] resp = new byte[4];
        resp[3] = (byte) (id & 0xFF);
        resp[2] = (byte) ((id >> 8) & 0xFF);
        resp[1] = (byte) ((id >> 16) & 0xFF);
        resp[0] = (byte) ((id >> 24) & 0xFF);
        return resp;
    }

}
