//Represents a customer with all their information

public class Customer {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String company;
    private String notes;
    private String createdDate;

    // Constructor for Customer
    public Customer(String id, String name, String email, String phone, 
                   String company, String notes, String createdDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.company = company;
        this.notes = notes;
        this.createdDate = createdDate;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getCompany() { return company; }
    public String getNotes() { return notes; }
    public String getCreatedDate() { return createdDate; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setCompany(String company) { this.company = company; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }

    // Convert customer data to CSV array format
    public String[] toCSVArray() {
        return new String[]{id, name, email, phone, company, notes, createdDate};
    }

    @Override
    public String toString() {
        return String.format("Customer{id='%s', name='%s', email='%s', phone='%s', company='%s'}", 
                           id, name, email, phone, company);
    }
}