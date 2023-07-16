package com.kasper.commons.Parser;

public class LinkedStringBuilder {
    private Node head;
    private Node tail;
    private int length;

    public LinkedStringBuilder() {
        head = null;
        tail = null;
        length = 0;
    }

    public LinkedStringBuilder appendToFront(String str) {
        if (str == null) {
            return appendToFront("null");
        }
        for (int i = str.length() - 1; i >= 0; i--) {
            appendToFront(str.charAt(i));
        }
        return this;
    }

    public LinkedStringBuilder appendToFront(int num) {
        return appendToFront(String.valueOf(num));
    }

    public LinkedStringBuilder appendToFront(char c) {
        Node newNode = new Node(c);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
        length++;
        return this;
    }

    public LinkedStringBuilder appendToBack(String str) {
        if (str == null) {
            return appendToBack("null");
        }
        for (int i = 0; i < str.length(); i++) {
            appendToBack(str.charAt(i));
        }
        return this;
    }

    public LinkedStringBuilder appendToBack(int num) {
        return appendToBack(String.valueOf(num));
    }

    public LinkedStringBuilder appendToBack(char c) {
        Node newNode = new Node(c);
        if (tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.prev = tail;
            tail.next = newNode;
            tail = newNode;
        }
        length++;
        return this;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node current = head;
        while (current != null) {
            sb.append(current.data);
            current = current.next;
        }
        return sb.toString();
    }

    private class Node {
        private char data;
        private Node prev;
        private Node next;

        public Node(char data) {
            this.data = data;
            this.prev = null;
            this.next = null;
        }
    }
}
