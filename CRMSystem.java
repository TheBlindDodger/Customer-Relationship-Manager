import java.util.*;
import java.io.*;

/**
 * CRMSystem implements Singleton pattern, as only one instance exists
 * Central system coordinating everything
 * stores customers, communications and tasks in memory
 * loads/saves data to csv file
 * searches customers, creates tasks, generates reports
 * notifies observer when things happen
 * it is kept a seperate file as core logic is seperate from UI
 */

public class CRMSystem extends Subject {
    private static CRMSystem instance;
    private static boolean initialized = false;
    
    // File paths
    private String dataDir = "crm_data";
    private String customersFile = dataDir + File.separator + "customers.csv";
    private String communicationsFile = dataDir + File.separator + "communications.csv";
    private String tasksFile = dataDir + File.separator + "tasks.csv";
    
    // Factories
    private CustomerFactory customerFactory;
    private CommunicationFactory communicationFactory;
    private TaskFactory taskFactory;
    
    // notification manager
    private NotificationManager notificationManager;
    
    // Application state
    private String currentUser = "admin";
    private boolean sessionActive = true;
    
    // data storage
    private List<Customer> customers;
    private List<Communication> communications;
    private List<Task> tasks;
    
    // private constructor for singleton pattern
    private CRMSystem() {
        if (!initialized) {
            // Initialize factories
            customerFactory = new CustomerFactory();
            communicationFactory = new CommunicationFactory();
            taskFactory = new TaskFactory();
            
            // Initialize notification manager
            notificationManager = new NotificationManager();
            attach(notificationManager);
            
            // Initialize data storage
            customers = new ArrayList<>();
            communications = new ArrayList<>();
            tasks = new ArrayList<>();
            
            ensureDataDirectory();
            ensureCSVFiles();
            loadData();
            
            initialized = true;
        }
    }
    
    /**
     * Get the singleton instance of CRMSystem
     * 
     * @return The singleton CRMSystem instance
     */
    public static CRMSystem getInstance() {
        if (instance == null) {
            instance = new CRMSystem();
        }
        return instance;
    }
    
    // ensure data directory exist
    private void ensureDataDirectory() {
        File dir = new File(dataDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
    
    // ensure csv files exist with correct headers
    private void ensureCSVFiles() {
        try {
            // Create customers.csv if it doesn't exist
            File customersCSV = new File(customersFile);
            if (!customersCSV.exists()) {
                try (PrintWriter writer = new PrintWriter(new FileWriter(customersCSV))) {
                    writer.println("id,name,email,phone,company,notes,created_date");
                }
            }
            
            // Create communications.csv if it doesn't exist
            File communicationsCSV = new File(communicationsFile);
            if (!communicationsCSV.exists()) {
                try (PrintWriter writer = new PrintWriter(new FileWriter(communicationsCSV))) {
                    writer.println("id,customer_id,type,date,subject,notes,tags");
                }
            }
            
            // Create tasks.csv if it doesn't exist
            File tasksCSV = new File(tasksFile);
            if (!tasksCSV.exists()) {
                try (PrintWriter writer = new PrintWriter(new FileWriter(tasksCSV))) {
                    writer.println("id,customer_id,title,description,due_date,status,created_date,assigned_to");
                }
            }
        } catch (IOException e) {
            System.err.println("Error creating CSV files: " + e.getMessage());
        }
    }
    
    //  Load all data from CSV files
    private void loadData() {
        loadCustomers();
        loadCommunications();
        loadTasks();
    }
    
    // loading customers.csv
    private void loadCustomers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(customersFile))) {
            String line;
            boolean isHeader = true;
            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                String[] parts = parseCSVLine(line);
                if (parts.length >= 7) {
                    Customer customer = new Customer(parts[0], parts[1], parts[2], parts[3], 
                                                   parts[4], parts[5], parts[6]);
                    customers.add(customer);
                }
            }
        } catch (IOException e) {
            // File doesn't exist, but it is okay as its first run
        }
    }
    
    // load communications data from csv file
    private void loadCommunications() {
        try (BufferedReader reader = new BufferedReader(new FileReader(communicationsFile))) {
            String line;
            boolean isHeader = true;
            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                String[] parts = parseCSVLine(line);
                if (parts.length >= 7) {
                    try {
                        CommunicationType type = CommunicationType.valueOf(parts[2].toUpperCase());
                        Communication comm = new Communication(parts[0], parts[1], type, parts[3], 
                                                              parts[4], parts[5], parts[6]);
                        communications.add(comm);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Skipping communication with invalid type: " + parts[2]);
                    }
                }
            }
        } catch (IOException e) {
            // File doesn't exist, but it is okay as its first run
        }
    }
    
    // load the tasks data from csv files
    private void loadTasks() {
        try (BufferedReader reader = new BufferedReader(new FileReader(tasksFile))) {
            String line;
            boolean isHeader = true;
            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                String[] parts = parseCSVLine(line);
                if (parts.length >= 8) {
                    try {
                        TaskStatus status = TaskStatus.valueOf(parts[5].toUpperCase());
                        Task task = new Task(parts[0], parts[1], parts[2], parts[3], 
                                            parts[4], status, parts[6], parts[7]);
                        tasks.add(task);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Skipping task with invalid status: " + parts[5]);
                    }
                }
            }
        } catch (IOException e) {
            // File doesn't exist, but it is okay as its first run
        }
    }
    
    // parses CSV line handling quotes and commas
    private String[] parseCSVLine(String line) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder currentField = new StringBuilder();
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(currentField.toString().trim());
                currentField = new StringBuilder();
            } else {
                currentField.append(c);
            }
        }
        result.add(currentField.toString().trim());
        
        return result.toArray(new String[0]);
    }
    
    
    // here are the customer management methods
    // first is creating new customer
    public void createCustomer(String name, String email, String phone, String company, String notes) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("email", email);
        params.put("phone", phone);
        params.put("company", company);
        params.put("notes", notes);
        
        Customer customer = customerFactory.create(params);
        customers.add(customer);
        saveCustomers();
        
        Map<String, Object> notificationData = new HashMap<>();
        notificationData.put("name", customer.getName());
        notifyObservers("customer_created", notificationData);
    }
    
    // Search customers by name, email, or company
    public List<Customer> searchCustomers(String searchTerm) {
        List<Customer> results = new ArrayList<>();
        String term = searchTerm.toLowerCase();
        
        for (Customer customer : customers) {
            if (customer.getName().toLowerCase().contains(term) ||
                customer.getEmail().toLowerCase().contains(term) ||
                customer.getCompany().toLowerCase().contains(term)) {
                results.add(customer);
            }
        }
        return results;
    }
    
    // Get customer by ID
    public Customer getCustomerById(String id) {
        for (Customer customer : customers) {
            if (customer.getId().equals(id)) {
                return customer;
            }
        }
        return null;
    }
    
    // Get all customers
    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customers);
    }
    

    // Communication Management Methods
    
    // Log a new communication
    public void logCommunication(String customerId, CommunicationType type, String subject, 
                                String notes, String tags) {
        Map<String, Object> params = new HashMap<>();
        params.put("customerId", customerId);
        params.put("type", type);
        params.put("subject", subject);
        params.put("notes", notes);
        params.put("tags", tags);
        
        Communication communication = communicationFactory.create(params);
        communications.add(communication);
        saveCommunications();
        
        Customer customer = getCustomerById(customerId);
        Map<String, Object> notificationData = new HashMap<>();
        notificationData.put("customerName", customer != null ? customer.getName() : "Unknown");
        notificationData.put("type", type.toString());
        notifyObservers("communication_logged", notificationData);
    }
    
    // Get communications by customer ID
    public List<Communication> getCommunicationsByCustomer(String customerId) {
        List<Communication> result = new ArrayList<>();
        for (Communication comm : communications) {
            if (comm.getCustomerId().equals(customerId)) {
                result.add(comm);
            }
        }
        return result;
    }
    
    // Task Management Methods
    
    //Create a new task
    public void createTask(String customerId, String title, String description, 
                          String dueDate, String assignedTo) {
        Map<String, Object> params = new HashMap<>();
        params.put("customerId", customerId);
        params.put("title", title);
        params.put("description", description);
        params.put("dueDate", dueDate);
        params.put("assignedTo", assignedTo);
        
        Task task = taskFactory.create(params);
        tasks.add(task);
        saveTasks();
        
        Map<String, Object> notificationData = new HashMap<>();
        notificationData.put("title", task.getTitle());
        notificationData.put("dueDate", task.getDueDate());
        notifyObservers("task_created", notificationData);
    }
    
    // Update task status
    public void updateTaskStatus(String taskId, TaskStatus status) {
        Task task = getTaskById(taskId);
        if (task != null) {
            task.setStatus(status);
            saveTasks();
        }
    }
    
    // Get task by ID
    public Task getTaskById(String id) {
        for (Task task : tasks) {
            if (task.getId().equals(id)) {
                return task;
            }
        }
        return null;
    }
    
    // Get tasks by customer ID
    public List<Task> getTasksByCustomer(String customerId) {
        List<Task> result = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getCustomerId().equals(customerId)) {
                result.add(task);
            }
        }
        return result;
    }
    
    // Get all pending tasks
    public List<Task> getPendingTasks() {
        List<Task> result = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getStatus() == TaskStatus.PENDING) {
                result.add(task);
            }
        }
        return result;
    }
    
    // Reporting Methods
    
    // Generate customer activity report
    public void generateCustomerActivityReport() {
        System.out.println("\n=== CUSTOMER ACTIVITY REPORT ===");
        for (Customer customer : customers) {
            int commCount = getCommunicationsByCustomer(customer.getId()).size();
            int taskCount = getTasksByCustomer(customer.getId()).size();
            
            System.out.printf("Customer: %s (%s)\n", customer.getName(), customer.getCompany());
            System.out.printf("  Communications: %d\n", commCount);
            System.out.printf("  Tasks: %d\n", taskCount);
            System.out.println();
        }
    }
    
    //Generate task completion report
    public void generateTaskCompletionReport() {
        System.out.println("\n=== TASK COMPLETION REPORT ===");
        long pendingTasks = 0;
        long completedTasks = 0;
        long overdueTasks = 0;
        
        for (Task task : tasks) {
            switch (task.getStatus()) {
                case PENDING: pendingTasks++; break;
                case COMPLETED: completedTasks++; break;
                case OVERDUE: overdueTasks++; break;
            }
        }
        
        System.out.printf("Pending Tasks: %d\n", pendingTasks);
        System.out.printf("Completed Tasks: %d\n", completedTasks);
        System.out.printf("Overdue Tasks: %d\n", overdueTasks);
        System.out.printf("Total Tasks: %d\n", tasks.size());
        
        if (tasks.size() > 0) {
            double completionRate = (double) completedTasks / tasks.size() * 100;
            System.out.printf("Completion Rate: %.1f%%\n", completionRate);
        }
    }
    
    // Data Persistence Methods
    
    //Save customers to CSV file
    private void saveCustomers() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(customersFile))) {
            writer.println("id,name,email,phone,company,notes,created_date");
            for (Customer customer : customers) {
                writer.println(formatCSVLine(customer.toCSVArray()));
            }
        } catch (IOException e) {
            System.err.println("Error saving customers: " + e.getMessage());
        }
    }
    
    //Save communications to CSV file
    private void saveCommunications() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(communicationsFile))) {
            writer.println("id,customer_id,type,date,subject,notes,tags");
            for (Communication comm : communications) {
                writer.println(formatCSVLine(comm.toCSVArray()));
            }
        } catch (IOException e) {
            System.err.println("Error saving communications: " + e.getMessage());
        }
    }
    
    //Save tasks to CSV file
    private void saveTasks() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(tasksFile))) {
            writer.println("id,customer_id,title,description,due_date,status,created_date,assigned_to");
            for (Task task : tasks) {
                writer.println(formatCSVLine(task.toCSVArray()));
            }
        } catch (IOException e) {
            System.err.println("Error saving tasks: " + e.getMessage());
        }
    }
    
    //Format array of strings for CSV output
    private String formatCSVLine(String[] fields) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            if (i > 0) sb.append(",");
            String field = fields[i];
            if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
                field = "\"" + field.replace("\"", "\"\"") + "\"";
            }
            sb.append(field);
        }
        return sb.toString();
    }
    
    // Getters for application state
    public String getCurrentUser() { return currentUser; }
    public boolean isSessionActive() { return sessionActive; }
    public void setSessionActive(boolean active) { this.sessionActive = active; }
}