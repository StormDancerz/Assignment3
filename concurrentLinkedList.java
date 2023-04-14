// concurrent Linkedlist
import java.util.concurrent.locks.*;

class Node {
    int data;
    Node next;
    Node prev;
    Lock lock;

    Node(int n) {
        data = n;
        next = null;
        prev = null;
        lock = new ReentrantLock();
    }

    @Override
    public String toString() {
        return Integer.toString(data);
    }
}

public class concurrentLinkedList {
    private Node head;
    private Node tail;
    private int size;
    private Lock lock;

    public concurrentLinkedList() {
        head = null;
        tail = null;
        size = 0;
        lock = new ReentrantLock();
    }

    public void insert(int data) {
        Node newNode = new Node(data);

        lock.lock();

        if (head == null) {
            head = newNode;
            tail = newNode;
        } else if (head.data >= newNode.data) {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        } else {
            Node curr = head;

            while (curr.next != null && curr.next.data < newNode.data) {
                curr = curr.next;
            }

            newNode.next = curr.next;

            if (curr.next != null) {
                newNode.next.prev = newNode;
            }

            curr.next = newNode;
            newNode.prev = curr;

            if (newNode.next == null) {
                tail = newNode;
            }
        }

        size++;
        lock.unlock();
    }

    public void remove(int key) {
        if (head == null) {
            return;
        }

        Node curr = head;
        lock.lock();

        if (curr.data == key) {
            Node temp = head;
            head = head.next;

            if (head != null) {
                head.prev = null;
            }

            size--;
            lock.unlock();
            return;
        }

        while (curr.next != null) {
            if (curr.next.data == key) {
                Node temp = curr.next;
                curr.next = curr.next.next;

                if (curr.next != null) {
                    curr.next.prev = curr;
                }

                size--;
                lock.unlock();
                return;
            }

            curr = curr.next;
        }

        lock.unlock();
    }

    public boolean contains(int key) {
        lock.lock();

        Node temp = head;

        while (temp != null) {
            if (temp.data == key) {
                lock.unlock();
                return true;
            }

            temp = temp.next;
        }

        lock.unlock();
        return false;
    }

    public int removeHead() {
        lock.lock();

        if (head == null) {
            lock.unlock();
            return Integer.MIN_VALUE;
        }

        int value = head.data;
        Node temp = head;

        head = head.next;

        if (head != null) {
            head.prev = null;
        }

        size--;
        lock.unlock();
        return value;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return head == null;
    }

    @Override
    public String toString() {
        if (head == null) {
            return "(null)";
        }

        StringBuilder sb = new StringBuilder();
        Node temp = head;

        while (temp != null) {
            sb.append("[").append(temp).append("]->");
            temp = temp.next;
        }

        sb.append("(null)");

        return sb.toString();
    }
}
