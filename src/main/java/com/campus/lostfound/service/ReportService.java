package com.campus.lostfound.service;
import com.campus.lostfound.model.ClaimStatus;
import com.campus.lostfound.model.Item;
import com.campus.lostfound.model.ItemStatus;
import java.util.Map;
import java.util.TreeMap;
public class ReportService {
    private DataStore database;
    public ReportService(DataStore database) {
        this.database = database;
    }
    // Create a summary of the system
    public String summary() {
        long lost = 0;
        long found = 0;
        long returned = 0;
        long closed = 0;
        for (Item item : database.getItems().values()) {
            if (item.getType().equals("LOST")) {
                lost++;
            } else if (item.getType().equals("FOUND")) {
                found++;
            }
            if (item.getStatus().equals(ItemStatus.RETURNED)) {
                returned++;
            } else if (item.getStatus().equals(ItemStatus.CLOSED)) {
                closed++;
            }
        }
        // Count pending claims
        long pending = 0;
        for (var claim : database.getClaims().values()) {
            if (claim.getStatus().equals(ClaimStatus.PENDING)) {
                pending++;
            }
        }
        // Calculate recovery rate
        double recoveryRate = 0;
        if (found > 0) {
            recoveryRate = (returned + closed) * 100.0 / found;
        }
        return "LOST reports: " + lost
                + "\nFOUND reports: " + found
                + "\nCLOSED claims: " + closed
                + "\nPENDING claims: " + pending
                + String.format(
                        "\nRecovery rate: %.2f%%",
                        recoveryRate
                );
    }
    // Count items according to their category
    public Map<String, Long> countByCategory() {
        Map<String, Long> categoryCount = new TreeMap<>();
        for (Item item : database.getItems().values()) {
            String category = item.getCategory();
            if (categoryCount.containsKey(category)) {
                categoryCount.put(
                        category,
                        categoryCount.get(category) + 1
                );
            } else {
                categoryCount.put(category, 1L);
            }
        }
        return categoryCount;
    }
    // Count items according to their location
    public Map<String, Long> countByLocation() {
        Map<String, Long> locationCount = new TreeMap<>();
        for (Item item : database.getItems().values()) {
            String location = item.getLocation();
            if (locationCount.containsKey(location)) {
                locationCount.put(
                        location,
                        locationCount.get(location) + 1
                );
            } else {
                locationCount.put(location, 1L);
            }
        }
        return locationCount;
    }
}