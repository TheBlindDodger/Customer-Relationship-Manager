import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * CustomerFactory implements Factory pattern for creating Customer objects
 */
public class CustomerFactory extends EntityFactory<Customer> {
    
    /**
     * Create a new Customer object with auto-generated ID and timestamp
     * 
     * @param params Map containing customer parameters (name, email, phone, company, notes)
     * @return A new Customer object
     */
    @Override
    public Customer create(Map<String, Object> params) {
        String id = generateId("CUST");
        String createdDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        
        return new Customer(
            id,
            (String) params.getOrDefault("name", ""),
            (String) params.getOrDefault("email", ""),
            (String) params.getOrDefault("phone", ""),
            (String) params.getOrDefault("company", ""),
            (String) params.getOrDefault("notes", ""),
            createdDate
        );
    }
}