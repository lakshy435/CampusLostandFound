package com.campus.lostfound;
import com.campus.lostfound.dsa.ItemSearch;
import com.campus.lostfound.dsa.ItemSorter;
import com.campus.lostfound.dsa.MatchEngine;
import com.campus.lostfound.model.*;
import com.campus.lostfound.service.*;
import com.campus.lostfound.util.Validate;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
public class Main {
    private Scanner scanner = new Scanner(System.in);
    private DataStore database = new DataStore();
    private UserService userService = new UserService(database);
    private ItemService itemService = new ItemService(database);
    private ClaimService claimService = new ClaimService(database);
    private ReportService reportService = new ReportService(database);
    private ItemSearch itemSearch = new ItemSearch();
    private MatchEngine matchEngine = new MatchEngine();
    private User currentUser;
    public static void main(String[] args) {
        Main app = new Main();
        app.createDefaultAdmin();
        app.start();
    }
    // Create a default admin account when the program starts
    private void createDefaultAdmin() {
        if (!database.getUsers().containsKey("admin")) {
            userService.register(
                    "admin",
                    "System Admin",
                    "admin123",
                    Role.ADMIN
            );
            System.out.println("\nDefault admin created.");
            System.out.println("User ID: admin");
            System.out.println("Password: admin123");
        }
    }
    // Main program loop
    private void start() {
        boolean running = true;
        while (running) {
            if (currentUser == null) {
                running = loginMenu();
            } else {
                dashboard();
            }
        }
        scanner.close();
        System.out.println("Thank you for using the system.");
    }
    // Login and registration menu
    private boolean loginMenu() {
        System.out.println("\n================================");
        System.out.println("      CAMPUS LOST AND FOUND");
        System.out.println("================================");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("0. Exit");
        System.out.print("Enter choice: ");
        String choice = scanner.nextLine();
        try {
            switch (choice) {
                case "1":
                    login();
                    break;
                case "2":
                    register();
                    break;
                case "0":
                    return false;
                default:
                    System.out.println("Invalid choice.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return true;
    }
    // Register a new student or staff member
    private void register() {
        System.out.println("\n----- REGISTER -----");
        System.out.print("Enter user ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.println("1. Student");
        System.out.println("2. Staff");
        System.out.print("Choose role: ");
        String choice = scanner.nextLine();
        Role role;
        if (choice.equals("1")) {
            role = Role.STUDENT;
        } else if (choice.equals("2")) {
            role = Role.STAFF;
        } else {
            throw new IllegalArgumentException("Invalid role.");
        }
        userService.register(id, name, password, role);
        System.out.println("Registration successful.");
    }
    // Login user
    private void login() {
        System.out.println("\n----- LOGIN -----");
        System.out.print("Enter user ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        currentUser = userService.Login(id, password);
        System.out.println("Welcome, " + currentUser.getName());
    }
    // Display dashboard according to the user's role
    private void dashboard() {
        System.out.println("\n---------- DASHBOARD ----------");
        System.out.println("User: " + currentUser.getName());
        System.out.println("Role: " + currentUser.getRole());
        System.out.println("\n1. Report Lost Item");
        System.out.println("2. Report Found Item");
        System.out.println("3. Search Items");
        System.out.println("4. Sort Items");
        System.out.println("5. Find Possible Matches");
        System.out.println("6. Submit Claim");
        System.out.println("7. View Claims");
        System.out.println("8. Reports");
        // Admin-only options
        if (currentUser.getRole() == Role.ADMIN) {
            System.out.println("9. Decide Claim");
            System.out.println("10. Close Returned Item");
            System.out.println("11. View All Items");
            System.out.println("12. Delete Item");
        }
        System.out.println("0. Logout");
        System.out.print("Enter choice: ");
        String choice = scanner.nextLine();
        try {
            switch (choice) {
                case "1":
                    reportLost();
                    break;
                case "2":
                    reportFound();
                    break;
                case "3":
                    searchItems();
                    break;
                case "4":
                    sortItems();
                    break;
                case "5":
                    findMatches();
                    break;
                case "6":
                    submitClaim();
                    break;
                case "7":
                    viewClaims();
                    break;
                case "8":
                    showReports();
                    break;
                case "9":
                    if (currentUser.getRole() == Role.ADMIN) {
                        decideClaim();
                    } else {
                        System.out.println("Admin access required.");
                    }
                    break;
                case "10":
                    if (currentUser.getRole() == Role.ADMIN) {
                        closeItem();
                    } else {
                        System.out.println("Admin access required.");
                    }
                    break;
                case "11":
                    if (currentUser.getRole() == Role.ADMIN) {
                        viewAllItems();
                    } else {
                        System.out.println("Admin access required.");
                    }
                    break;
                case "12":
                    if (currentUser.getRole() == Role.ADMIN) {
                        deleteItem();
                    } else {
                        System.out.println("Admin access required.");
                    }
                    break;
                case "0":
                    currentUser = null;
                    System.out.println("Logged out.");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    // Report a lost item
    private void reportLost() {
        System.out.println("\n----- REPORT LOST ITEM -----");
        System.out.print("Item name: ");
        String name = scanner.nextLine();
        System.out.print("Category: ");
        String category = scanner.nextLine();
        System.out.print("Location: ");
        String location = scanner.nextLine();
        System.out.print("Date lost (YYYY-MM-DD): ");
        LocalDate date = Validate.parseDate(scanner.nextLine());
        System.out.print("Description: ");
        String description = scanner.nextLine();
        LostItem item = itemService.reportLost(
                currentUser.getUserId(),
                name,
                category,
                location,
                date,
                description
        );
        System.out.println("Lost item added successfully.");
        System.out.println("Item ID: " + item.getItemId());
    }
    // Report a found item
    private void reportFound() {
        System.out.println("\n----- REPORT FOUND ITEM -----");
        System.out.print("Item name: ");
        String name = scanner.nextLine();
        System.out.print("Category: ");
        String category = scanner.nextLine();
        System.out.print("Location: ");
        String location = scanner.nextLine();
        System.out.print("Date found (YYYY-MM-DD): ");
        LocalDate date = Validate.parseDate(scanner.nextLine());
        System.out.print("Description: ");
        String description = scanner.nextLine();
        FoundItem item = itemService.reportFound(
                currentUser.getUserId(),
                name,
                category,
                location,
                date,
                description
        );
        System.out.println("Found item added successfully.");
        System.out.println("Item ID: " + item.getItemId());
    }
    // Search items using a keyword
    private void searchItems() {
        System.out.println("\n----- SEARCH ITEMS -----");
        System.out.print("Enter keyword: ");
        String keyword = scanner.nextLine();
        List<Item> items = itemService.getAll();
        itemSearch.buildIndex(items);
        List<Item> results = itemSearch.searchbyKeyword(
                items,
                keyword
        );
        if (results.isEmpty()) {
            System.out.println("No items found.");
            return;
        }
        for (Item item : results) {
            System.out.println(item);
        }
    }
    // Sort items according to user's choice
    private void sortItems() {
        System.out.println("\n----- SORT ITEMS -----");
        System.out.println("1. Date");
        System.out.println("2. Name");
        System.out.println("3. Category");
        System.out.println("4. Location");
        System.out.print("Choose option: ");
        String choice = scanner.nextLine();
        ItemSorter.SortType sortType;
        if (choice.equals("1")) {
            sortType = ItemSorter.SortType.DATE;
        } else if (choice.equals("2")) {
            sortType = ItemSorter.SortType.NAME;
        } else if (choice.equals("3")) {
            sortType = ItemSorter.SortType.CATEGORY;
        } else if (choice.equals("4")) {
            sortType = ItemSorter.SortType.LOCATION;
        } else {
            throw new IllegalArgumentException("Invalid sorting option.");
        }
        List<Item> items = itemService.getAll();
        List<Item> sortedItems = ItemSorter.sort(items, sortType);
        for (Item item : sortedItems) {
            System.out.println(item);
        }
    }
    // Find possible matches for a lost item
    private void findMatches() {
        System.out.println("\n----- POSSIBLE MATCHES -----");
        System.out.print("Enter lost item ID: ");
        String itemId = scanner.nextLine();
        Item item = itemService.getbyId(itemId);
        if (item == null) {
            throw new IllegalArgumentException("Item not found.");
        }
        if (!(item instanceof LostItem)) {
            throw new IllegalArgumentException("This is not a lost item.");
        }
        LostItem lostItem = (LostItem) item;
        List<Item> allItems = itemService.getAll();
        List<MatchEngine.MatchResult> matches =
                matchEngine.findMatches(lostItem, allItems);
        if (matches.isEmpty()) {
            System.out.println("No possible matches found.");
            return;
        }
        for (MatchEngine.MatchResult match : matches) {
            System.out.println(
                    match.getItem()
                            + " | Match Score: "
                            + match.getScore()
                            + "%"
            );
        }
    }
    // Submit a claim for a found item
    private void submitClaim() {
        System.out.println("\n----- SUBMIT CLAIM -----");
        System.out.print("Found item ID: ");
        String itemId = scanner.nextLine();
        System.out.print("Enter ownership proof/details: ");
        String proof = scanner.nextLine();
        Claim claim = claimService.submitClaim(
                itemId,
                currentUser.getUserId(),
                proof
        );
        System.out.println("Claim submitted successfully.");
        System.out.println("Claim ID: " + claim.getClaimId());
    }
    // Display claims
    private void viewClaims() {
        System.out.println("\n----- CLAIMS -----");
        if (currentUser.getRole() == Role.ADMIN) {
            List<Claim> pendingClaims = claimService.getPending();
            if (pendingClaims.isEmpty()) {
                System.out.println("No pending claims.");
            } else {
                for (Claim claim : pendingClaims) {
                    System.out.println(claim);
                }
            }
            return;
        }
        List<Claim> claims =
                claimService.getClaimsByUser(currentUser.getUserId());
        if (claims.isEmpty()) {
            System.out.println("No claims found.");
            return;
        }
        for (Claim claim : claims) {
            System.out.println(claim);
        }
    }
    // Admin approves or rejects a claim
    private void decideClaim() {
        List<Claim> pendingClaims = claimService.getPending();
        if (pendingClaims.isEmpty()) {
            System.out.println("No pending claims.");
            return;
        }
        System.out.println("----- PENDING CLAIMS -----");
        for (Claim claim : pendingClaims) {
            System.out.println(claim);
        }
        System.out.print("Enter claim ID: ");
        String claimId = scanner.nextLine();
        System.out.print("Approve claim? (Y/N): ");
        String answer = scanner.nextLine();
        boolean approve;
        if (answer.equalsIgnoreCase("Y")) {
            approve = true;
        } else if (answer.equalsIgnoreCase("N")) {
            approve = false;
        } else {
            throw new IllegalArgumentException("Please enter Y or N.");
        }
        claimService.decideClaim(claimId, approve);
        System.out.println("Claim decision saved.");
    }
    // Close a returned item
    private void closeItem() {
        System.out.print("Enter returned item ID: ");
        String itemId = scanner.nextLine();
        claimService.closeItem(itemId);
        System.out.println("Item case closed.");
    }
    // Display all items
    private void viewAllItems() {
        List<Item> items = itemService.getAll();
        if (items.isEmpty()) {
            System.out.println("No items available.");
            return;
        }
        for (Item item : items) {
            System.out.println(item);
        }
    }
    // Delete an item
    private void deleteItem() {
        System.out.print("Enter item ID: ");
        String itemId = scanner.nextLine();
        itemService.delete(itemId);
        System.out.println("Item deleted successfully.");
    }
    // Display system reports
    private void showReports() {
        System.out.println("\n----- SYSTEM REPORT -----");
        System.out.println(reportService.summary());
        System.out.println("\nItems by category:");
        Map<String, Long> categories =
                reportService.countByCategory();
        for (String category : categories.keySet()) {
            System.out.println(
                    category + " : " + categories.get(category)
            );
        }
        System.out.println("\nItems by location:");
        Map<String, Long> locations =
                reportService.countByLocation();
        for (String location : locations.keySet()) {
            System.out.println(
                    location + " : " + locations.get(location)
            );
        }
    }
}