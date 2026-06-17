package org.example.loadBalancer;

public class BackendServer {
    private final String id;
    private final int weight;
    private final int baseLatency;           // New: Natural server speed
    private int currentConnections = 0;
    private long totalResponseTime = 0;
    private int totalRequests = 0;
    private boolean healthy = true;          // ← Restored

    public BackendServer(String id, int weight, int baseLatency) {
        this.id = id;
        this.weight = weight;
        this.baseLatency = baseLatency;
    }

    public void handleRequest(Task task, long responseTime) {
        currentConnections++;
        totalRequests++;
        totalResponseTime += responseTime;
        System.out.println("✅ " + id + " handled task " + task.getTaskId() +
                " | Conn: " + currentConnections + " | RT: " + responseTime + "ms");
    }

    public void completeRequest() {
        if (currentConnections > 0) currentConnections--;
    }

    // Calculate realistic response time based on current load
    public long calculateResponseTime() {
        // Base latency + penalty for high load
        return baseLatency + (currentConnections * 15L);
    }

    public String getId() { return id; }
    public int getWeight() { return weight; }
    public int getCurrentConnections() { return currentConnections; }

    public double getAvgResponseTime() {
        return totalRequests == 0 ? baseLatency : (double) totalResponseTime / totalRequests;
    }

    public boolean isHealthy() { return healthy; }
    public void setHealthy(boolean healthy) { this.healthy = healthy; }

    @Override
    public String toString() {
        return String.format("Server %-10s | Weight: %d | Conn: %d | AvgRT: %.1fms | Total: %d",
                id, weight, currentConnections, getAvgResponseTime(), totalRequests);
    }
}