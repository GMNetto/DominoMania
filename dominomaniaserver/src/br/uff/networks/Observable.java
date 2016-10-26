package br.uff.networks;

public interface Observable<T> {

    void add(Observer<T> observer);

    void remove(Observer<T> observer);

}
