import java.util.Map;

// Observer interface for the Observer design pattern
// Defines which objects need to be notified of changes
public interface Observer {
    /**
     * Update method called when the subject notifies observers
     * 
     * @param eventType The type of event that occurred
     * @param data Additional data about the event
     */
    void update(String eventType, Map<String, Object> data);
}