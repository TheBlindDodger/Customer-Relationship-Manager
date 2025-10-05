import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Observer;

//NotificationManager implements Observer pattern 
//Handles notifications for various CRM system events
public class NotificationManager implements Observer {
    
    /**
     * Update method called when events occur in the CRM system
     * 
     * @param eventType The type of event that occurred
     * @param data Additional data about the event
     */
    @Override
    public void update(String eventType, Map<String, Object> data) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        
        switch (eventType) {
            case "task_created":
                System.out.printf("\n[%s] New task created: %s (Due: %s)\n", 
                                timestamp, data.get("title"), data.get("dueDate"));
                break;
            case "task_due_soon":
                System.out.printf("\n[%s] Task due soon: %s (Due: %s)\n", 
                                timestamp, data.get("title"), data.get("dueDate"));
                break;
            case "customer_created":
                System.out.printf("\n[%s] New customer added: %s\n", 
                                timestamp, data.get("name"));
                break;
            case "communication_logged":
                System.out.printf("\n[%s] Communication logged with %s: %s\n", 
                                timestamp, data.get("customerName"), data.get("type"));
                break;
            default:
                System.out.printf("\n[%s] System event: %s\n", timestamp, eventType);
                break;
        }
    }
}