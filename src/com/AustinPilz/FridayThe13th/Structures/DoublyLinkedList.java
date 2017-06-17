package com.AustinPilz.FridayThe13th.Structures;
import java.util.NoSuchElementException;


public class DoublyLinkedList
{
    private Node head;
    private Node tail;
    private int size;

    public DoublyLinkedList() {
        size = 0;
    }

    private class Node {
        Double element;
        Node next;
        Node prev;

        public Node(Double element, Node next, Node prev) {
            this.element = element;
            this.next = next;
            this.prev = prev;
        }
    }

    /**
     * returns the size of the linked list
     *
     * @return
     */
    public int size() {
        return size;
    }

    /**
     * return whether the list is empty or not
     *
     * @return
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * adds element at the starting of the linked list
     *
     * @param element
     */
    public void addFirst(Double element) {
        Node tmp = new Node(element, head, null);
        if (head != null) {
            head.prev = tmp;
        }
        head = tmp;
        if (tail == null) {
            tail = tmp;
        }
        size++;
    }

    /**
     * adds element at the end of the linked list
     *
     * @param element
     */
    public void addLast(Double element) {

        Node tmp = new Node(element, null, tail);
        if (tail != null) {
            tail.next = tmp;
        }
        tail = tmp;
        if (head == null) {
            head = tmp;
        }
        size++;
    }

    /**
     * this method walks forward through the linked list
     */
    public void iterateForward() {

        Node tmp = head;
        while (tmp != null) {
            System.out.println(tmp.element);
            tmp = tmp.next;
        }
    }

    public Double calculateAverage()
    {
        Node tmp = head;
        double sum = 0;
        while (tmp != null)
        {
            sum += tmp.element;
            tmp = tmp.next;
        }

        return (sum/size());
    }

    /**
     * this method walks backward through the linked list
     */
    public void iterateBackward() {

        Node tmp = tail;
        while (tmp != null) {
            System.out.println(tmp.element);
            tmp = tmp.prev;
        }
    }

    /**
     * this method removes element from the start of the linked list
     *
     * @return
     */
    public Double removeFirst() {
        if (size == 0) throw new NoSuchElementException();
        Node tmp = head;
        head = head.next;
        head.prev = null;
        size--;
        return tmp.element;
    }

    /**
     * this method removes element from the end of the linked list
     *
     * @return
     */
    public Double removeLast() {
        if (size == 0) throw new NoSuchElementException();
        Node tmp = tail;
        tail = tail.prev;
        tail.next = null;
        size--;
        return tmp.element;
    }
}