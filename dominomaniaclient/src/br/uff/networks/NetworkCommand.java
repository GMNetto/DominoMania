package br.uff.networks;

public interface NetworkCommand {

    void execute(String message, Transport connection);

}
