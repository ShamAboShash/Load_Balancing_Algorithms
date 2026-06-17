package org.example.loadBalancer;

import java.util.List;

public interface LoadBalancingStrategy {
    BackendServer getNextServer(List<BackendServer> servers, Task task);
}