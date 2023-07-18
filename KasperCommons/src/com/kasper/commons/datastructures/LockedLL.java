package com.kasper.commons.datastructures;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LockedLL<T> implements Iterable<T> {
    LinkedList<T> internalArray;
    ReadWriteLock lock;

    public LockedLL () {
        internalArray = new LinkedList<>();
        lock = new ReentrantReadWriteLock();
    }



    public int size() {
        lock.readLock().lock();
        try {
            return internalArray.size();
        } finally {
            lock.readLock().unlock();
        }
    }


    public boolean isEmpty() {
        lock.readLock().lock();
        try {
            return internalArray.isEmpty();
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean contains(Object o) {
        lock.readLock().lock();
        try {
            return internalArray.contains(o);
        } finally {
            lock.readLock().unlock();
        }
    }


    public T get(int index) {
        lock.readLock().lock();
        try {
            return internalArray.get(index);
        } finally {
            lock.readLock().unlock();
        }
    }


    public T set(int index, T element) {
        lock.writeLock().lock();
        try {
            return internalArray.set(index, element);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void add(int index, T element) {
        lock.writeLock().lock();
        try {
            internalArray.add(index, element);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void add (T element) {
        lock.writeLock().lock();
        try {
             internalArray.add(element);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public T remove(int index) {
        lock.writeLock().lock();
        try {
            return internalArray.remove(index);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        lock.readLock().lock();
        try {
            return internalArray.iterator();
        } finally {
            lock.readLock().unlock();
        }

    }

    public void addLast (T object){
        lock.writeLock().lock();
        try {
            internalArray.add(internalArray.size()-1, object);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void clear () {
        lock.writeLock().lock();
        try {
            internalArray.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void addAll (LockedLL<T> objects) {
        lock.writeLock().lock();
        try {
            for (T x : objects) {
                internalArray.add(x);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public ArrayList<T> getInternalArray () {
        lock.readLock().lock();
        try {
            return new ArrayList<>(internalArray);
        } finally {
            lock.readLock().unlock();
        }
    }

    public T getFirst() {
        lock.readLock().lock();
        try {
            return internalArray.getFirst();
        } finally {
            lock.readLock().lock();
        }
    }

    public T getLast(){
        lock.writeLock().lock();
        try {
            return internalArray.getLast();
        } finally {
            lock.writeLock().lock();
        }
    }

    public void addFirst(T obj){
        lock.writeLock().lock();
        try {
            internalArray.addFirst(obj);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public String toString(){
        lock.readLock().lock();
        try {
            return internalArray.toString();
        } finally {
            lock.readLock().unlock();
        }
    }

    public void removeFirst(){
        lock.writeLock().lock();
        try {
            internalArray.removeFirst();
        } finally {
            lock.writeLock().lock();
        }
    }

    public void removeLast(){
        lock.writeLock().lock();
        try{
            internalArray.removeLast();
        } finally {
            lock.writeLock().lock();
        }
    }
}