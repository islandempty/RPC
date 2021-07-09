package com.zfoo.protocol.collection;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author islandempty
 * @since 2021/7/8
 **/
public class ConcurrentArrayList<E> implements List<E> {

    private ReentrantLock lock;

    private ArrayList<E> list;

    public ConcurrentArrayList() {
        this.lock = new ReentrantLock();
        this.list = new ArrayList<>();
    }

    public ConcurrentArrayList(int initialCapacity) {
        this.lock = new ReentrantLock();
        this.list = new ArrayList<>(initialCapacity);
    }


    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    public List<E> clearAndReturn() {
        lock.lock();
        try {
            var newList = (ArrayList<E>) list.clone();
            list.clear();
            return newList;
        } finally {
            lock.unlock();
        }
    }
    @Override
    public boolean contains(Object o) {
        lock.lock();
        try {
            return list.contains(o);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Iterator<E> iterator() {
        lock.lock();
        try {
            var newList = (ArrayList<E>) list.clone();
            return newList.iterator();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Object[] toArray() {
        lock.lock();
        try {
            return list.toArray();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        lock.lock();
        try {
            return list.toArray(ts);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean add(E e) {
        lock.lock();
        try {
            return list.add(e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean remove(Object o) {
        lock.lock();
        try {
            return list.remove(o);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        lock.lock();
        try {
            return list.containsAll(collection);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        lock.lock();
        try {
            return list.addAll(collection);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean addAll(int i, Collection<? extends E> collection) {
        lock.lock();
        try {
            return list.addAll(i, collection);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        lock.lock();
        try {
            return list.removeAll(collection);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        lock.lock();
        try {
            return list.retainAll(collection);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void clear() {
        lock.lock();
        try {
            list.clear();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public E get(int index) {
        return list.get(index);
    }

    @Override
    public E set(int index, E element) {
        lock.lock();
        try {
            return list.set(index, element);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void add(int index, E element) {
        lock.lock();
        try {
            list.add(index, element);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public E remove(int index) {
        lock.lock();
        try {
            return list.remove(index);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int indexOf(Object o) {
        lock.lock();
        try {
            return list.indexOf(o);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int lastIndexOf(Object o) {
        lock.lock();
        try {
            return list.lastIndexOf(o);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public ListIterator<E> listIterator() {
        lock.lock();
        try {
            var newList = (ArrayList<E>) list.clone();
            return newList.listIterator();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        lock.lock();
        try {
            var newList = (ArrayList<E>) list.clone();
            return newList.listIterator(index);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        lock.lock();
        try {
            return list.subList(fromIndex, toIndex);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (this != o) {
            return false;
        }

        return list.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}

