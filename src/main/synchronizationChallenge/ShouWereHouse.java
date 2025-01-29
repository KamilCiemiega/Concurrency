package main.synchronizationChallenge;

import java.util.LinkedList;
import java.util.Queue;

public class ShouWereHouse {
    private String product;
    private Queue<String> orders;

    public ShouWereHouse(String product) {
        this.product = product;
        this.orders = new LinkedList<>();
    }

    public void addOrder(String order) {
        orders.add(order);
        System.out.println("Dodano zamówienie: " + order);
    }

    public void processOrders() {
        while (!orders.isEmpty()) {
            String order = orders.poll();
            System.out.println("Przetwarzanie: " + order);
        }
    }
}
