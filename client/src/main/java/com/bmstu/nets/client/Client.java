package com.bmstu.nets.client;

public class Client {

    public static void main(String[] args) {
        System.out.println("Client started");
        try {
            new StartupService().start();

            Thread.currentThread().join();
        } catch (InterruptedException e) {
            System.out.println("Client stopped");
        }
    }
}
