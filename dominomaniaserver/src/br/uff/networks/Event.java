package br.uff.networks;

import java.util.HashSet;
import java.util.Set;

public class Event<T> implements Observable<T>, Observer<T> {

    private final Set<Observer<T>> observers;

    public Event() {
        observers = new HashSet<>();
    }

    @Override
    public void add(Observer<T> observer) {
        observers.add(observer);
    }

    @Override
    public void remove(Observer<T> observer) {
        observers.remove(observer);
    }

    @Override
    public void update(T obj) {
        for (Observer<T> observer : observers) {
            observer.update(obj);
        }
    }
}
