package org.example.loadBalancer;

public class Task {
    private final int taskId;
    private final String userId;
    private final String requestType;

    public Task(int taskId, String userId, String requestType) {
        this.taskId = taskId;
        this.userId = userId;
        this.requestType = requestType;
    }

    public int getTaskId() {
        return taskId;
    }

    public String getUserId() {
        return userId;
    }

    public String getRequestType() {
        return requestType;
    }

    @Override
    public String toString() {
        return "Task{" + taskId + ", user=" + userId + ", type=" + requestType + '}';
    }
}