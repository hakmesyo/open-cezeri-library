/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.utils;

/**
 *
 * @author BAP1
 */
import java.util.LinkedList;

public final class CQueue {

    private LinkedList<Object> items;

    /**
     * Creats an empty queue
     */
    public CQueue() {
        items = new LinkedList<Object>();
    }

    /**
     * Inserts a new element at the rear of the queue.
     *
     * @param element element to be inserted.
     */
    public Object enqueue(Object element) {
        items.add(element);
        return element;
    }

    /**
     * Removes the element at the top of the queue.
     *
     * @return the removed element.
     * @throws EmptyQueueException if the queue is empty.
     */
    public Object dequeue() {
        if (items.size() == 0) {
            throw new EmptyQueueException();
        }
        return items.removeFirst();
    }

    /**
     * Inspects the element at the top of the queue without removing it.
     *
     * @return the element at the top of the queue.
     * @throws EmptyQueueException if the queue is empty.
     */
    public Object front() {
        if (items.size() == 0) {
            throw new EmptyQueueException();
        }
        return items.getFirst();
    }

    /**
     * @return the number of elements at the queue.
     */
    public int size() {
        return items.size();
    }

    /**
     * @return true if the queue is empty.
     */
    public boolean empty() {
        return (size() == 0);
    }

    /**
     * Removes all elements at the queue.
     */
    public void clear() {
        items.clear();
    }

}
