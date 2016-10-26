package br.uff.networks;

import java.io.Closeable;
import java.io.IOException;

public interface Transport extends Closeable {

    void open() throws IOException;

    boolean isOpen();

    void send(String message);

    String receive();

}
