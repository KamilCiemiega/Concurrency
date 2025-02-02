package main.synchronizationChallenge;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

record Order(int orderID, String shoeType, int orderQuantity){}

class ShouWereHouse {
    public final static List<String> products = new LinkedList<>();
    private Queue<Order> orders = new LinkedList<>();
    boolean fullListOfOrders = false;
    boolean listOfOrdersIsEmpty = true;

    public ShouWereHouse() {}

    public synchronized void receiveOrder(Order order){
//        while (fullListOfOrders){
//            try {
//                wait();
//            }catch(InterruptedException e){
//                throw new RuntimeException(e);
//            }
//        }

        orders.add(order);
        System.out.println("Received order: " + order);

        if (orders.size() == 10) {
            fullListOfOrders = true;
            System.out.println("Orders list is full!");
        }
        listOfOrdersIsEmpty = false;

        notifyAll();
    }

    public synchronized void fulfillOrder() {
        while (listOfOrdersIsEmpty){
            try {
                wait();
            }catch (InterruptedException e){
                throw new RuntimeException(e);
            }
        }

        Order processedOrder = orders.poll();
        System.out.println("Processed order: " + processedOrder);

        if (orders.isEmpty()){
            listOfOrdersIsEmpty = true;
            System.out.println("All orders are processed");
        }

        fullListOfOrders = false;
        notifyAll();
    }
}

class Producer implements Runnable{
    private ShouWereHouse shouWereHouse;

    public Producer(ShouWereHouse shouWereHouse) {
        this.shouWereHouse = shouWereHouse;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++){
            Order newOrder = new Order(i+1, ShouWereHouse.products.get(i), 2);
            shouWereHouse.receiveOrder(newOrder);
           try {
               Thread.sleep(1000);
           }catch (InterruptedException e){
               throw new RuntimeException(e);
           }
        }
    }
}

class Consumer implements Runnable {
    private final ShouWereHouse shouWereHouse;

    public Consumer(ShouWereHouse shouWereHouse) {
        this.shouWereHouse = shouWereHouse;
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            shouWereHouse.fulfillOrder();

            try {
                Thread.sleep(700);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}


public class SynchronizationChallengeMain {
    public static void main(String[] args) {
        ShouWereHouse shouWereHouse = new ShouWereHouse();

        ShouWereHouse.products.add("Reebok");
        ShouWereHouse.products.add("Nike dunk");
        ShouWereHouse.products.add("Nike shox");
        ShouWereHouse.products.add("Adidas sambas");
        ShouWereHouse.products.add("Adidas campus");
        ShouWereHouse.products.add("Nike2");
        ShouWereHouse.products.add("Nike3");
        ShouWereHouse.products.add("Nike4");
        ShouWereHouse.products.add("Nike5");
        ShouWereHouse.products.add("Nike6");

        Thread producer = new Thread(new Producer(shouWereHouse));
        producer.start();

        Thread consumer1 = new Thread(new Consumer(shouWereHouse));
        Thread consumer2 = new Thread(new Consumer(shouWereHouse));
        consumer1.start();
        consumer2.start();

        try {
            producer.join();
            consumer1.join();
            consumer2.join();
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }

        System.out.println("All orders are received");
    }
}
