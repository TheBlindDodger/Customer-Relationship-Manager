//Communication data class representing a communication log entry
public class Communication {
    private String id;
    private String customerId;
    private CommunicationType type;
    private String date;
    private String subject;
    private String notes;
    private String tags;

    //Constructor for Communication
    public Communication(String id, String customerId, CommunicationType type, 
                        String date, String subject, String notes, String tags) {
        this.id = id;
        this.customerId = customerId;
        this.type = type;
        this.date = date;
        this.subject = subject;
        this.notes = notes;
        this.tags = tags;
    }

    // Getters
    public String getId() { return id; }
    public String getCustomerId() { return customerId; }
    public CommunicationType getType() { return type; }
    public String getDate() { return date; }
    public String getSubject() { return subject; }
    public String getNotes() { return notes; }
    public String getTags() { return tags; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public void setType(CommunicationType type) { this.type = type; }
    public void setDate(String date) { this.date = date; }
    public void setSubject(String subject) { this.subject = subject; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setTags(String tags) { this.tags = tags; }

    //Convert communication data to CSV array format
    public String[] toCSVArray() {
        return new String[]{id, customerId, type.toString(), date, subject, notes, tags};
    }

    @Override
    public String toString() {
        return String.format("Communication{id='%s', customerId='%s', type=%s, subject='%s'}", 
                           id, customerId, type, subject);
    }
}