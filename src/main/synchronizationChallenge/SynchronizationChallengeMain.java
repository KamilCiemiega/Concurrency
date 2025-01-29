package main.synchronizationChallenge;

public class SynchronizationChallengeMain {
    public static void main(String[] args) {
        ShouWereHouse houWereHouse = new ShouWereHouse("Produkty");

        houWereHouse.addOrder("Zamówienie 1");
        houWereHouse.addOrder("Zamówienie 2");
        houWereHouse.addOrder("Zamówienie 3");

        houWereHouse.processOrders();
    }
}
