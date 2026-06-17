package org.example.loadBalancer;

import org.example.loadBalancer.strategies.LeastResponseTimeStrategy;
import org.example.loadBalancer.strategies.PowerOfTwoChoicesStrategy;
import org.example.loadBalancer.strategies.WeightedLeastConnectionsStrategy;

import java.util.Scanner;

public class Simulator {

    private final LoadBalancer loadBalancer;
    private final Scanner scanner;

    public Simulator() {
        this.loadBalancer = new LoadBalancer(new WeightedLeastConnectionsStrategy());
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("Welcome !");

        int numServers = getNumberOfServers();
        addServers(numServers);

        boolean running = true;
        while (running) {
            printHeader();
            printMenu();

            int choice = scanner.nextInt();

            if (choice == 0) {
                System.out.println("👋 Thank you !");
                break;
            }

            selectStrategy(choice);
            runSimulation();
        }
        scanner.close();
    }

    private int getNumberOfServers() {
        int num;
        do {
            System.out.print("How many servers do you want? (minimum 2): ");
            num = scanner.nextInt();
            if (num < 2) {
                System.out.println("❌ Please enter at least 2 servers.");
            }
        } while (num < 2);
        return num;
    }

    private void addServers(int count) {
        output("➕ Initializing " + count + " servers...\n");

        for (int i = 1; i <= count; i++) {
            String name = "Server-" + (char) ('A' + (i - 1) % 26);  // A, B, C..., Z, AA, AB...
            int weight = 1 + (i % 5);           // weights between 1 and 5
            int baseLatency = 40 + (i * 12);    // different speeds

            BackendServer server = new BackendServer(name, weight, baseLatency);
            loadBalancer.addServer(server);
            output("   ✅ Added: " + name + " (Weight: " + weight + ", BaseRT: " + baseLatency + "ms)\n");
        }
    }

    private void printHeader() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("           Load Balancer Simulator");
        System.out.println("=".repeat(70));
    }

    private void printMenu() {
        System.out.println("1. Weighted Least Connections");
        System.out.println("2. Least Response Time");
        System.out.println("3. Power of Two Choices");
        System.out.println("0. Exit");
        System.out.print("Choose algorithm: ");
    }

    private void selectStrategy(int choice) {
        switch (choice) {
            case 1 -> loadBalancer.setStrategy(new WeightedLeastConnectionsStrategy());
            case 2 -> loadBalancer.setStrategy(new LeastResponseTimeStrategy());
            case 3 -> loadBalancer.setStrategy(new PowerOfTwoChoicesStrategy());
        }
    }

    private void runSimulation() {
        System.out.print("\nHow many tasks do you want to send? : ");
        int numTasks = scanner.nextInt();

        System.out.println("\n🚀 Sending " + numTasks + " tasks...\n");

        for (int i = 1; i <= numTasks; i++) {
            Task task = new Task(i, "user" + (i % 5 + 1), "compute");
            BackendServer server = loadBalancer.getNextServer(task);
            long responseTime = server.calculateResponseTime();

            server.handleRequest(task, responseTime);

            try {
                Thread.sleep(45);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("\n⏳ Completing all requests...\n");
        for (BackendServer server : loadBalancer.getServers()) {
            while (server.getCurrentConnections() > 0) {
                server.completeRequest();
            }
        }

        printSeparator();
        loadBalancer.printStats();
        printSeparator();
    }

    private void printSeparator() {
        System.out.println("-".repeat(70));
    }

    private void output(String text) {
        System.out.print(text);
    }
}