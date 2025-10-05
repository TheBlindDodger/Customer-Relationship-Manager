import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Abstract EntityFactory for the Factory design pattern
 * Provides common functionality for creating entities
 * 
 * @param <T> The type of entity this factory creates
 */
public abstract class EntityFactory<T> {
    
    /**
     * Abstract method to create an entity
     * 
     * @param params Parameters needed to create the entity
     * @return The created entity
     */
    public abstract T create(Map<String, Object> params);
    
    /**
     * Generate a unique ID with a prefix and timestamp
     * 
     * @param prefix The prefix for the ID
     * @return A unique ID string
     */
    protected String generateId(String prefix) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return prefix + "_" + timestamp;
    }
}