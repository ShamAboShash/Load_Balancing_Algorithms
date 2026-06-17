package org.example.loadBalancer.strategies;

import org.example.loadBalancer.BackendServer;
import org.example.loadBalancer.LoadBalancingStrategy;
import org.example.loadBalancer.Task;
import java.util.List;

public class WeightedLeastConnectionsStrategy implements LoadBalancingStrategy {

    @Override
    public BackendServer getNextServer(List<BackendServer> servers, Task task) {
        if (servers.isEmpty()) {
            throw new IllegalStateException("No servers available");
        }

        BackendServer best = null;
        double bestScore = Double.MAX_VALUE;

        for (BackendServer server : servers) {
            if (!server.isHealthy()) continue;

            // Improved score: connections / weight + small tie breaker
            double score = (double) server.getCurrentConnections() / server.getWeight()
                    + (server.getCurrentConnections() * 0.01);

            if (score < bestScore) {
                bestScore = score;
                best = server;
            }
        }
        return best != null ? best : servers.get(0);
    }
}