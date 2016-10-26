package br.uff.networks.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import br.uff.networks.Transport;

public class TCPTransport implements Transport {

    private final Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    public TCPTransport(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void open() throws IOException {
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new PrintWriter(socket.getOutputStream());
    }

    @Override
    public boolean isOpen() {
        return !socket.isClosed();
    }

    @Override
    public void send(String message) {
        output.println(message);
        output.flush();
    }

    @Override
    public String receive(){
	String message = null;
	while(message == null){
	    try {
		Thread.sleep(10);
		message = input.readLine();
	    } catch (IOException | InterruptedException e) {
	    }
	}
	return message;
    }

    @Override
    public void close() throws IOException {
        socket.close();
        input.close();
        output.close();
    }
}
