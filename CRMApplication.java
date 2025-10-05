import java.util.*;

// Main CRM Application class providing command-line interface
// start of the Customer Relations Manager system

public class CRMApplication {
    private static Scanner scanner = new Scanner(System.in);
    private static CRMSystem crm = CRMSystem.getInstance();
    
    // start of the Customer Relations Manager system
    public static void main(String[] args) {
        System.out.println("Welcome to Customer Relations Manager (CRM) System");
        System.out.println("===================================================");
        
        while (crm.isSessionActive()) {
            displayMainMenu();
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    customerManagementMenu();
                    break;
                case 2:
                    communicationManagementMenu();
                    break;
                case 3:
                    taskManagementMenu();
                    break;
                case 4:
                    reportingMenu();
                    break;
                case 5:
                    System.out.println("Thank you for using CRM System. Goodbye!");
                    crm.setSessionActive(false);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    //Display the main menu
    private static void displayMainMenu() {
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("1. Customer Management");
        System.out.println("2. Communication Tracking");
        System.out.println("3. Task Management");
        System.out.println("4. Reports");
        System.out.println("5. Exit");
    }
    
    //Customer management submenu
    private static void customerManagementMenu() {
        while (true) {
            System.out.println("\n=== CUSTOMER MANAGEMENT ===");
            System.out.println("1. Add New Customer");
            System.out.println("2. Search Customers");
            System.out.println("3. View All Customers");
            System.out.println("4. View Customer Details");
            System.out.println("5. Back to Main Menu");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    addNewCustomer();
                    break;
                case 2:
                    searchCustomers();
                    break;
                case 3:
                    viewAllCustomers();
                    break;
                case 4:
                    viewCustomerDetails();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    //Add a new customer
    private static void addNewCustomer() {
        System.out.println("\n--- Add New Customer ---");
        String name = getStringInput("Name: ");
        String email = getStringInput("Email: ");
        String phone = getStringInput("Phone: ");
        String company = getStringInput("Company: ");
        String notes = getStringInput("Notes: ");
        
        crm.createCustomer(name, email, phone, company, notes);
        System.out.println("Customer added successfully!");
    }
    
    //Search for customers
    private static void searchCustomers() {
        System.out.println("\n--- Search Customers ---");
        String searchTerm = getStringInput("Enter search term (name, email, or company): ");
        List<Customer> results = crm.searchCustomers(searchTerm);
        
        if (results.isEmpty()) {
            System.out.println("No customers found matching your search.");
        } else {
            System.out.println("\nSearch Results:");
            for (Customer customer : results) {
                System.out.printf("ID: %s | Name: %s | Email: %s | Company: %s\n",
                                customer.getId(), customer.getName(), customer.getEmail(), customer.getCompany());
            }
        }
    }
    
    //View all customers
    private static void viewAllCustomers() {
        System.out.println("\n--- All Customers ---");
        List<Customer> customers = crm.getAllCustomers();
        
        if (customers.isEmpty()) {
            System.out.println("No customers found.");
        } else {
            for (Customer customer : customers) {
                System.out.printf("ID: %s | Name: %s | Email: %s | Company: %s\n",
                                customer.getId(), customer.getName(), customer.getEmail(), customer.getCompany());
            }
        }
    }
    
    //View detailed customer information
    private static void viewCustomerDetails() {
        System.out.println("\n--- Customer Details ---");
        String customerId = getStringInput("Enter Customer ID: ");
        Customer customer = crm.getCustomerById(customerId);
        
        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }
        
        System.out.println("\nCustomer Information:");
        System.out.printf("ID: %s\n", customer.getId());
        System.out.printf("Name: %s\n", customer.getName());
        System.out.printf("Email: %s\n", customer.getEmail());
        System.out.printf("Phone: %s\n", customer.getPhone());
        System.out.printf("Company: %s\n", customer.getCompany());
        System.out.printf("Notes: %s\n", customer.getNotes());
        System.out.printf("Created: %s\n", customer.getCreatedDate());
        
        // Show communications
        List<Communication> communications = crm.getCommunicationsByCustomer(customerId);
        System.out.printf("\nCommunications (%d):\n", communications.size());
        for (Communication comm : communications) {
            System.out.printf("  - %s | %s | %s\n", comm.getDate(), comm.getType(), comm.getSubject());
        }
        
        // Show tasks
        List<Task> tasks = crm.getTasksByCustomer(customerId);
        System.out.printf("Tasks (%d):\n", tasks.size());
        for (Task task : tasks) {
            System.out.printf("  - %s | %s | %s | Due: %s\n", 
                            task.getTitle(), task.getStatus(), task.getAssignedTo(), task.getDueDate());
        }
    }
    
    //Communication management submenu
    private static void communicationManagementMenu() {
        while (true) {
            System.out.println("\n=== COMMUNICATION TRACKING ===");
            System.out.println("1. Log New Communication");
            System.out.println("2. View Communications by Customer");
            System.out.println("3. Back to Main Menu");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    logNewCommunication();
                    break;
                case 2:
                    viewCommunicationsByCustomer();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    //Log a new communication
    private static void logNewCommunication() {
        System.out.println("\n--- Log New Communication ---");
        String customerId = getStringInput("Customer ID: ");
        
        // Verify customer exists
        Customer customer = crm.getCustomerById(customerId);
        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }
        
        System.out.printf("Customer: %s (%s)\n", customer.getName(), customer.getCompany());
        
        System.out.println("Communication Types:");
        System.out.println("1. PHONE");
        System.out.println("2. EMAIL");
        System.out.println("3. MEETING");
        System.out.println("4. OTHER");
        
        int typeChoice = getIntInput("Select communication type: ");
        CommunicationType type;
        switch (typeChoice) {
            case 1: type = CommunicationType.PHONE; break;
            case 2: type = CommunicationType.EMAIL; break;
            case 3: type = CommunicationType.MEETING; break;
            case 4: type = CommunicationType.OTHER; break;
            default:
                System.out.println("Invalid choice. Using OTHER.");
                type = CommunicationType.OTHER;
        }
        
        String subject = getStringInput("Subject: ");
        String notes = getStringInput("Notes: ");
        String tags = getStringInput("Tags (comma-separated): ");
        
        crm.logCommunication(customerId, type, subject, notes, tags);
        System.out.println("Communication logged successfully!");
    }
    
    //View communications for a specific customer
    private static void viewCommunicationsByCustomer() {
        System.out.println("\n--- View Communications by Customer ---");
        String customerId = getStringInput("Customer ID: ");
        
        Customer customer = crm.getCustomerById(customerId);
        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }
        
        List<Communication> communications = crm.getCommunicationsByCustomer(customerId);
        
        System.out.printf("\nCommunications for %s (%d total):\n", customer.getName(), communications.size());
        if (communications.isEmpty()) {
            System.out.println("No communications found.");
        } else {
            for (Communication comm : communications) {
                System.out.printf("\nID: %s\n", comm.getId());
                System.out.printf("Type: %s\n", comm.getType());
                System.out.printf("Date: %s\n", comm.getDate());
                System.out.printf("Subject: %s\n", comm.getSubject());
                System.out.printf("Notes: %s\n", comm.getNotes());
                System.out.printf("Tags: %s\n", comm.getTags());
                System.out.println("---");
            }
        }
    }
    
    //Task management submenu
    private static void taskManagementMenu() {
        while (true) {
            System.out.println("\n=== TASK MANAGEMENT ===");
            System.out.println("1. Create New Task");
            System.out.println("2. View Tasks by Customer");
            System.out.println("3. View All Pending Tasks");
            System.out.println("4. Update Task Status");
            System.out.println("5. Back to Main Menu");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    createNewTask();
                    break;
                case 2:
                    viewTasksByCustomer();
                    break;
                case 3:
                    viewAllPendingTasks();
                    break;
                case 4:
                    updateTaskStatus();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    //Create a new task
    private static void createNewTask() {
        System.out.println("\n--- Create New Task ---");
        String customerId = getStringInput("Customer ID: ");
        
        // Verify customer exists
        Customer customer = crm.getCustomerById(customerId);
        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }
        
        System.out.printf("Customer: %s (%s)\n", customer.getName(), customer.getCompany());
        
        String title = getStringInput("Task Title: ");
        String description = getStringInput("Description: ");
        String dueDate = getStringInput("Due Date (YYYY-MM-DD): ");
        String assignedTo = getStringInput("Assigned To: ");
        
        crm.createTask(customerId, title, description, dueDate, assignedTo);
        System.out.println("Task created successfully!");
    }
    
    //View tasks for a specific customer
    private static void viewTasksByCustomer() {
        System.out.println("\n--- View Tasks by Customer ---");
        String customerId = getStringInput("Customer ID: ");
        
        Customer customer = crm.getCustomerById(customerId);
        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }
        
        List<Task> tasks = crm.getTasksByCustomer(customerId);
        
        System.out.printf("\nTasks for %s (%d total):\n", customer.getName(), tasks.size());
        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
        } else {
            for (Task task : tasks) {
                System.out.printf("\nID: %s\n", task.getId());
                System.out.printf("Title: %s\n", task.getTitle());
                System.out.printf("Description: %s\n", task.getDescription());
                System.out.printf("Status: %s\n", task.getStatus());
                System.out.printf("Due Date: %s\n", task.getDueDate());
                System.out.printf("Assigned To: %s\n", task.getAssignedTo());
                System.out.printf("Created: %s\n", task.getCreatedDate());
                System.out.println("---");
            }
        }
    }
    
    //View all pending tasks
    private static void viewAllPendingTasks() {
        System.out.println("\n--- All Pending Tasks ---");
        List<Task> pendingTasks = crm.getPendingTasks();
        
        if (pendingTasks.isEmpty()) {
            System.out.println("No pending tasks found.");
        } else {
            System.out.printf("Pending Tasks (%d total):\n", pendingTasks.size());
            for (Task task : pendingTasks) {
                Customer customer = crm.getCustomerById(task.getCustomerId());
                String customerName = customer != null ? customer.getName() : "Unknown";
                
                System.out.printf("\nTask ID: %s\n", task.getId());
                System.out.printf("Customer: %s\n", customerName);
                System.out.printf("Title: %s\n", task.getTitle());
                System.out.printf("Due Date: %s\n", task.getDueDate());
                System.out.printf("Assigned To: %s\n", task.getAssignedTo());
                System.out.println("---");
            }
        }
    }
    
    //Update task status
    private static void updateTaskStatus() {
        System.out.println("\n--- Update Task Status ---");
        String taskId = getStringInput("Task ID: ");
        
        Task task = crm.getTaskById(taskId);
        if (task == null) {
            System.out.println("Task not found.");
            return;
        }
        
        System.out.printf("Current Task: %s (Status: %s)\n", task.getTitle(), task.getStatus());
        
        System.out.println("New Status Options:");
        System.out.println("1. PENDING");
        System.out.println("2. COMPLETED");
        System.out.println("3. OVERDUE");
        
        int statusChoice = getIntInput("Select new status: ");
        TaskStatus newStatus;
        switch (statusChoice) {
            case 1: newStatus = TaskStatus.PENDING; break;
            case 2: newStatus = TaskStatus.COMPLETED; break;
            case 3: newStatus = TaskStatus.OVERDUE; break;
            default:
                System.out.println("Invalid choice.");
                return;
        }
        
        crm.updateTaskStatus(taskId, newStatus);
        System.out.println("Task status updated successfully!");
    }
    
    //Reporting submenu
    private static void reportingMenu() {
        while (true) {
            System.out.println("\n=== REPORTING ===");
            System.out.println("1. Customer Activity Report");
            System.out.println("2. Task Completion Report");
            System.out.println("3. Communication Frequency Report");
            System.out.println("4. Back to Main Menu");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    crm.generateCustomerActivityReport();
                    break;
                case 2:
                    crm.generateTaskCompletionReport();
                    break;
                case 3:
                    generateCommunicationFrequencyReport();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    //Generate communication frequency report
    private static void generateCommunicationFrequencyReport() {
        System.out.println("\n=== COMMUNICATION FREQUENCY REPORT ===");
        
        Map<CommunicationType, Integer> typeCount = new HashMap<>();
        for (CommunicationType type : CommunicationType.values()) {
            typeCount.put(type, 0);
        }
        
        List<Customer> customers = crm.getAllCustomers();
        int totalComms = 0;
        
        for (Customer customer : customers) {
            List<Communication> comms = crm.getCommunicationsByCustomer(customer.getId());
            totalComms += comms.size();
            
            for (Communication comm : comms) {
                typeCount.put(comm.getType(), typeCount.get(comm.getType()) + 1);
            }
        }
        
        System.out.printf("Total Communications: %d\n", totalComms);
        System.out.println("\nBy Type:");
        for (CommunicationType type : CommunicationType.values()) {
            int count = typeCount.get(type);
            double percentage = totalComms > 0 ? (double) count / totalComms * 100 : 0;
            System.out.printf("  %s: %d (%.1f%%)\n", type, count, percentage);
        }
        
        if (customers.size() > 0) {
            double avgCommsPerCustomer = (double) totalComms / customers.size();
            System.out.printf("\nAverage Communications per Customer: %.1f\n", avgCommsPerCustomer);
        }
    }
    
    // Utility Methods
    
    //Get string input from user
    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        System.out.flush();
        String input = scanner.nextLine().trim();
        System.out.println("[You entered: " + input + "]");
        return input;
    }
    
    //Get integer input from user with validation
    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                System.out.flush();
                String input = scanner.nextLine().trim();
                System.out.println("[You entered: " + input + "]");
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}