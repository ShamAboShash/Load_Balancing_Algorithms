package org.example.loadBalancer.strategies;


import java.util.List;
import java.util.Random;
import org.example.loadBalancer.BackendServer;
import org.example.loadBalancer.LoadBalancingStrategy;
import org.example.loadBalancer.Task;

public class PowerOfTwoChoicesStrategy implements LoadBalancingStrategy {
    private final Random random = new Random();

    @Override
    public BackendServer getNextServer(List<BackendServer> servers, Task task) {
        if (servers.size() < 2) {
            return servers.get(0);
        }

        int i = random.nextInt(servers.size());
        int j = random.nextInt(servers.size());

        BackendServer server1 = servers.get(i);
        BackendServer server2 = servers.get(j);

        return server1.getCurrentConnections() <= server2.getCurrentConnections() ? server1 : server2;
    }
}