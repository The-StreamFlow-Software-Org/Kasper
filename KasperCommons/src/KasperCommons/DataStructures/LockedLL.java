package KasperCommons.DataStructures;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class LockedLL< T > extends LinkedList < T > {
    private ReentrantLock lock = new ReentrantLock();

    @Override
    public T getFirst() {
        lock.lock();
        try {
            return super.getFirst();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public T getLast() {
        lock.lock();
        try {
            return super.getLast();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public T removeFirst() {
        lock.lock();
        try {
            return super.removeFirst();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public T removeLast() {
        lock.lock();
        try {
            return super.removeLast();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void addFirst(T t) {
        lock.lock();
        try {
            super.addFirst(t);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void addLast(T t) {
        lock.lock();
        try {
            super.addLast(t);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean contains(Object o) {
        lock.lock();
        try {
            return super.contains(o);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int size() {
        lock.lock();
        try {
            return super.size();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean add(T t) {
        lock.lock();
        try {
            return super.add(t);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean remove(Object o) {
        lock.lock();
        try {
            return super.remove(o);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean addAll(Collection < ? extends T > c) {
        lock.lock();
        try {
            return super.addAll(c);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean addAll(int index, Collection < ? extends T > c) {
        lock.lock();
        try {
            return super.addAll(index, c);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void clear() {
        lock.lock();
        try {
            super.clear();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public T get(int index) {
        lock.lock();
        try {
            return super.get(index);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public T set(int index, T element) {
        lock.lock();
        try {
            return super.set(index, element);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void add(int index, T element) {
        lock.lock();
        try {
            super.add(index, element);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public T remove(int index) {
        lock.lock();
        try {
            return super.remove(index);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int indexOf(Object o) {
        lock.lock();
        try {
            return super.indexOf(o);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int lastIndexOf(Object o) {
        lock.lock();
        try {
            return super.lastIndexOf(o);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public T peek() {
        lock.lock();
        try {
            return super.peek();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public T element() {
        lock.lock();
        try {
            return super.element();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public T poll() {
        lock.lock();
        try {
            return super.poll();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public T remove() {
        lock.lock();
        try {
            return super.remove();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean offer(T t) {
        lock.lock();
        try {
            return super.offer(t);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean offerFirst(T t) {
        lock.lock();
        try {
            return super.offerFirst(t);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean offerLast(T t) {
        lock.lock();
        try {
            return super.offerLast(t);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public T peekFirst() {
        lock.lock();
        try {
            return super.peekFirst();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public T peekLast() {
        lock.lock();
        try {
            return super.peekLast();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public T pollFirst() {
        lock.lock();
        try {
            return super.pollFirst();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public T pollLast() {
        lock.lock();
        try {
            return super.pollLast();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void push(T t) {
        lock.lock();
        try {
            super.push(t);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public T pop() {
        lock.lock();
        try {
            return super.pop();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        lock.lock();
        try {
            return super.removeFirstOccurrence(o);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        lock.lock();
        try {
            return super.removeLastOccurrence(o);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public ListIterator < T > listIterator(int index) {
        lock.lock();
        try {
            return super.listIterator(index);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Iterator < T > descendingIterator() {
        lock.lock();
        try {
            return super.descendingIterator();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Object clone() {
        lock.lock();
        try {
            return super.clone();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Object[] toArray() {
        lock.lock();
        try {
            return super.toArray();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public < T1 > T1[] toArray(T1[] a) {
        lock.lock();
        try {
            return super.toArray(a);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Spliterator < T > spliterator() {
        lock.lock();
        try {
            return super.spliterator();
        } finally {
            lock.unlock();
        }
    }
}