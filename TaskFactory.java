import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * TaskFactory implements Factory pattern for creating Task objects
 */
public class TaskFactory extends EntityFactory<Task> {
    
    /**
     * Create a new Task object with auto-generated ID and timestamp
     * 
     * @param params Map containing task parameters (customerId, title, description, dueDate, assignedTo)
     * @return A new Task object
     */
    @Override
    public Task create(Map<String, Object> params) {
        String id = generateId("TASK");
        String createdDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        
        return new Task(
            id,
            (String) params.getOrDefault("customerId", ""),
            (String) params.getOrDefault("title", ""),
            (String) params.getOrDefault("description", ""),
            (String) params.getOrDefault("dueDate", ""),
            TaskStatus.PENDING,
            createdDate,
            (String) params.getOrDefault("assignedTo", "")
        );
    }
}