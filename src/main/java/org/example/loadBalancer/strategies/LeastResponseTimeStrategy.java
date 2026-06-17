package org.example.loadBalancer.strategies;

import org.example.loadBalancer.BackendServer;
import org.example.loadBalancer.LoadBalancingStrategy;
import org.example.loadBalancer.Task;
import java.util.List;

public class LeastResponseTimeStrategy implements LoadBalancingStrategy {

    @Override
    public BackendServer getNextServer(List<BackendServer> servers, Task task) {
        if (servers.isEmpty()) {
            throw new IllegalStateException("No servers available");
        }

        BackendServer best = servers.get(0);
        double bestAvgRT = best.getAvgResponseTime();

        for (BackendServer server : servers) {
            // Removed health check since we are not using it now
            if (server.getAvgResponseTime() < bestAvgRT) {
                bestAvgRT = server.getAvgResponseTime();
                best = server;
            }
        }
        return best;
    }
}