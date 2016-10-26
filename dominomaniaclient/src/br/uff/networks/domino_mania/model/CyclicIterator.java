package br.uff.networks.domino_mania.model;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public class CyclicIterator<T> implements ListIterator<T> {

    private ListIterator<T> iterator;
    private List<T> collection;

    public CyclicIterator(T[] iterable) {
        this(Arrays.asList(iterable));
    }

    CyclicIterator(List<T> collection) {
        this.collection = collection;
        iterator = collection.listIterator();
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public T next() {
        if (iterator.hasNext()) {
            return iterator.next();
        }
        iterator = collection.listIterator();
        return next();
    }

    @Override
    public void remove() {
        iterator.remove();
    }

    @Override
    public boolean hasPrevious() {
        return iterator.hasPrevious();
    }

    @Override
    public T previous() {
        if (iterator.hasPrevious()) {
            return iterator.previous();
        }
        iterator = collection.listIterator();
        return previous();
    }

    @Override
    public int nextIndex() {
        if (iterator.hasNext()) {
            return iterator.nextIndex();
        }
        iterator = collection.listIterator();
        return nextIndex();
    }

    @Override
    public int previousIndex() {
        if (iterator.hasPrevious()) {
            return iterator.previousIndex();
        }
        iterator = collection.listIterator();
        return previousIndex();
    }

    @Override
    public void set(T e) {
        iterator.set(e);
    }

    @Override
    public void add(T e) {
        iterator.add(e);
    }
}
