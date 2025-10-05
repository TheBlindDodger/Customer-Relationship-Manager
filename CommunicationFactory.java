import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

// Factory pattern for creating Communication objects
public class CommunicationFactory extends EntityFactory<Communication> {
    
    /**
     * Create a new Communication object with auto generated ID and timestamp
     * 
     * @param params Map with communication parametres (customerId, type, subject, notes, tags)
     * @return A new Communication object
     */
    @Override
    public Communication create(Map<String, Object> params) {
        String id = generateId("COMM");
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        
        return new Communication(
            id,
            (String) params.getOrDefault("customerId", ""),
            (CommunicationType) params.getOrDefault("type", CommunicationType.OTHER),
            date,
            (String) params.getOrDefault("subject", ""),
            (String) params.getOrDefault("notes", ""),
            (String) params.getOrDefault("tags", "")
        );
    }
}