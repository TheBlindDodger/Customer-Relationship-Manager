//Task data class representing a task entity
public class Task {
    private String id;
    private String customerId;
    private String title;
    private String description;
    private String dueDate;
    private TaskStatus status;
    private String createdDate;
    private String assignedTo;

    // Constructor for Task
    public Task(String id, String customerId, String title, String description, 
               String dueDate, TaskStatus status, String createdDate, String assignedTo) {
        this.id = id;
        this.customerId = customerId;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = status;
        this.createdDate = createdDate;
        this.assignedTo = assignedTo;
    }

    // Getters
    public String getId() { return id; }
    public String getCustomerId() { return customerId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getDueDate() { return dueDate; }
    public TaskStatus getStatus() { return status; }
    public String getCreatedDate() { return createdDate; }
    public String getAssignedTo() { return assignedTo; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }
    public void setStatus(TaskStatus status) { this.status = status; }
    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }
    public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }

    // Convert task data to CSV array format
    public String[] toCSVArray() {
        return new String[]{id, customerId, title, description, dueDate, status.toString(), createdDate, assignedTo};
    }

    @Override
    public String toString() {
        return String.format("Task{id='%s', customerId='%s', title='%s', status=%s, dueDate='%s'}", 
                           id, customerId, title, status, dueDate);
    }
}