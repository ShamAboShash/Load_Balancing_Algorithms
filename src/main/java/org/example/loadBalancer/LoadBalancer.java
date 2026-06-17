package org.example.loadBalancer;
import java.util.ArrayList;
import java.util.List;

public class LoadBalancer {
    private final List<BackendServer> servers = new ArrayList<>();
    private LoadBalancingStrategy strategy;

    public LoadBalancer(LoadBalancingStrategy initialStrategy) {
        this.strategy = initialStrategy;
    }

    public void setStrategy(LoadBalancingStrategy strategy) {
        this.strategy = strategy;
        System.out.println("🔀 Strategy changed to: " + strategy.getClass().getSimpleName());
    }

    public void addServer(BackendServer server) {
        servers.add(server);
        System.out.println("➕ Added server: " + server.getId());
    }

    public BackendServer getNextServer(Task task) {
        return strategy.getNextServer(servers, task);
    }

    public void printStats() {
        System.out.println("\n📊 === Load Balancer Statistics ===");
        for (BackendServer s : servers) {
            System.out.println(s);
        }
    }

    public List<BackendServer> getServers() {
        return servers;
    }
}