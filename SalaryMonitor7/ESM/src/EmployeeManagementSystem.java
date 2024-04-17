import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


class Employee {
    private String id;
    private String name;
    private double salary;
    private String originalName;
    private double originalSalary;
    private boolean nameChanged;
    private boolean salaryChanged;

    public Employee(String id, String name, double salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.originalName = name;
        this.originalSalary = salary;
        this.nameChanged = false;
        this.salaryChanged = false;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        System.out.println("Are you sure you want to change the name from " + this.name + " to " + name + "? (yes/no)");
        if (confirmChange()) {
            System.out.println("Name changed from " + this.name + " to " + name);
            this.originalName = this.name;
            this.name = name;
            this.nameChanged = true;
        } else {
            System.out.println("Name change canceled.");
        }
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        System.out.println("Are you sure you want to change the salary from " + this.salary + " to " + salary + "? (yes/no)");
        if (confirmChange()) {
            System.out.println("Salary changed from " + this.salary + " to " + salary);
            this.originalSalary = this.salary;
            this.salary = salary;
            this.salaryChanged = true;
        } else {
            System.out.println("Salary change canceled.");
        }
    }

    private boolean confirmChange() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            String confirmation = reader.readLine();
            return confirmation.equalsIgnoreCase("yes");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getViewName() {
        if (nameChanged) {
            return this.name;
        } else {
            return this.originalName;
        }
    }

    public String getViewSalary() {
        if (salaryChanged) {
            return String.format("$%,.2f", this.salary);
        } else {
            return String.format("$%,.2f", this.originalSalary);
        }
    }

    @Override
    public String toString() {
        // Format the salary to display with commas and a trailing dot
        String formattedSalary = String.format("%.2f", salary);
        String[] parts = formattedSalary.split("\\.");
        String result = parts[0].replaceAll("(\\d)(?=(\\d{3})+$)", "$1,") + "." + parts[1] + "-";
        return "Employee{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", salary=" + result +
                '}';
    }
}

public class EmployeeManagementSystem {
    private ArrayList<Employee> employees;
    private BufferedReader reader;

    public EmployeeManagementSystem() {
        employees = new ArrayList<>();
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public void addEmployee() {
        try {
            System.out.println("Enter employee ID:");
            String id = reader.readLine();
            System.out.println("Enter employee name:");
            String name = reader.readLine();
            System.out.println("Enter employee salary:");
            double salary = 0;
            try {
                salary = Double.parseDouble(reader.readLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input for salary. Please enter a number.");
                return;
            }

            Employee employee = new Employee(id, name, salary);
            employees.add(employee);
            System.out.println("Employee added successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void viewEmployees() {
        if (employees.isEmpty()) {
            System.out.println("No employees found.");
            return;
        }

        // Find the maximum length for each column
        int maxIdLength = 0;
        int maxNameLength = 0;
        for (Employee employee : employees) {
            maxIdLength = Math.max(maxIdLength, employee.getId().length());
            maxNameLength = Math.max(maxNameLength, employee.getViewName().length());
        }

        // Add extra spacing between column headers
        int headerSpacing = 30;

        // Print column headers with increased spacing
        System.out.printf("%-" + (maxIdLength + headerSpacing) + "s%-"
                + (maxNameLength + headerSpacing) + "s%-15s%n",
                "Employee ID", "Employee Name", "Employee Salary");

        // Print employee details
        for (Employee employee : employees) {
            System.out.printf("%-" + (maxIdLength + headerSpacing) + "s%-"
                    + (maxNameLength + headerSpacing) + "s%s%n",
                    employee.getId(), employee.getViewName(), employee.getViewSalary());
        }
    }

    public void editEmployee() {
        try {
            System.out.println("Enter employee ID to edit:");
            String idToEdit = reader.readLine();
            for (Employee employee : employees) {
                if (employee.getId().equals(idToEdit)) {
                    System.out.println("What would you like to edit? Enter 'name' or 'salary':");
                    String editChoice = reader.readLine();
                    if (editChoice.equalsIgnoreCase("name")) {
                        System.out.println("Enter new name for " + employee.getName() + ":");
                        String newName = reader.readLine();
                        employee.setName(newName);
                        System.out.println("Employee name updated successfully.");
                    } else if (editChoice.equalsIgnoreCase("salary")) {
                        System.out.println("Enter new salary for " + employee.getName() + ":");
                        double newSalary = 0;
                        try {
                            newSalary = Double.parseDouble(reader.readLine());
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input for salary. Please enter a number.");
                            return;
                        }
                        employee.setSalary(newSalary);
                        System.out.println("Employee salary updated successfully.");
                    } else {
                        System.out.println("Invalid choice. Please enter 'name' or 'salary'.");
                    }
                    return;
                }
            }
            System.out.println("Employee not found.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteEmployee() {
        try {
            System.out.println("Enter employee ID to delete:");
            String idToDelete = reader.readLine();
            for (Employee employee : employees) {
                if (employee.getId().equals(idToDelete)) {
                    System.out.println("Are you sure you want to delete " + employee.getName() + "? (yes/no)");
                    String confirmation;
                    try {
                        confirmation = reader.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }
                    if (confirmation.equalsIgnoreCase("yes")) {
                        employees.remove(employee);
                        System.out.println("Employee deleted successfully.");
                    } else {
                        System.out.println("Deletion canceled.");
                    }
                    return;
                }
            }
            System.out.println("Employee not found.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        EmployeeManagementSystem system = new EmployeeManagementSystem();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                System.out.println("\nEmployee Management System Menu:");
                System.out.println("1. Add Employee");
                System.out.println("2. View Employees");
                System.out.println("3. Edit Employee");
                System.out.println("4. Delete Employee");
                System.out.println("5. Exit");
                System.out.println("Enter your choice:");

                int choice = 0;
                try {
                    choice = Integer.parseInt(reader.readLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                    continue;
                }

                switch (choice) {
                    case 1:
                        system.addEmployee();
                        break;
                    case 2:
                        system.viewEmployees();
                        break;
                    case 3:
                        system.editEmployee();
                        break;
                    case 4:
                        system.deleteEmployee();
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        return; // Exit the main method
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 5.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
