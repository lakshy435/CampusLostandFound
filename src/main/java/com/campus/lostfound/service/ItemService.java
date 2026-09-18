package com.campus.lostfound.service;
import com.campus.lostfound.model.FoundItem;
import com.campus.lostfound.model.Item;
import com.campus.lostfound.model.ItemStatus;
import com.campus.lostfound.model.LostItem;
import com.campus.lostfound.util.AppLogger;
import com.campus.lostfound.util.Validate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
public class ItemService {
    private DataStore database;
    public ItemService(DataStore database) {
        this.database = database;
    }
    // Add a lost item
    public LostItem reportLost(
            String reporterId,
            String name,
            String category,
            String location,
            LocalDate date,
            String description) {
        Validate.requiredtext(reporterId);
        Validate.requiredtext(name);
        Validate.requiredtext(category);
        Validate.requiredtext(location);
        Validate.requiredtext(description);
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be empty.");
        }
        if (!database.getUsers().containsKey(reporterId)) {
            throw new IllegalArgumentException("User not found.");
        }
        String itemId = createItemId("L");
        LostItem item = new LostItem(
                itemId,
                reporterId,
                name.trim(),
                category.trim(),
                location.trim(),
                date,
                description.trim()
        );
        database.getItems().put(itemId, item);
        database.saveItems();
        AppLogger.info("Lost item reported: " + itemId);
        return item;
    }
    // Add a found item
    public FoundItem reportFound(
            String reporterId,
            String name,
            String category,
            String location,
            LocalDate date,
            String description) {
        Validate.requiredtext(reporterId);
        Validate.requiredtext(name);
        Validate.requiredtext(category);
        Validate.requiredtext(location);
        Validate.requiredtext(description);
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be empty.");
        }
        if (!database.getUsers().containsKey(reporterId)) {
            throw new IllegalArgumentException("User not found.");
        }
        String itemId = createItemId("F");
        FoundItem item = new FoundItem(
                itemId,
                reporterId,
                name.trim(),
                category.trim(),
                location.trim(),
                date,
                description.trim()
        );
        database.getItems().put(itemId, item);
        database.saveItems();
        AppLogger.info("Found item reported: " + itemId);
        return item;
    }
    // Find an item using its ID
    public Item getbyId(String itemId) {
        Validate.requiredtext(itemId);
        return database.getItems().get(itemId.trim());
    }
    // Search items by name, category, location or description
    public List<Item> search(String keyword) {
        Validate.requiredtext(keyword);
        keyword = keyword.trim().toLowerCase();
        List<Item> result = new ArrayList<>();
        for (Item item : database.getItems().values()) {
            String name = item.getName().toLowerCase();
            String category = item.getCategory().toLowerCase();
            String location = item.getLocation().toLowerCase();
            String description = item.getDescription().toLowerCase();
            if (name.contains(keyword)
                    || category.contains(keyword)
                    || location.contains(keyword)
                    || description.contains(keyword)) {
                result.add(item);
            }
        }
        return result;
    }
    // Get all items
    public List<Item> getAll() {
        return new ArrayList<>(
                database.getItems().values()
        );
    }
    // Change the status of an item
    public void updateStatus(
            String itemId,
            ItemStatus status) {
        Item item = getbyId(itemId);
        if (item == null) {
            throw new IllegalArgumentException("Item not found.");
        }
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be empty.");
        }
        item.setStatus(status);
        database.saveItems();
        AppLogger.info("Item status updated: " + itemId);
    }
    // Delete an item
    public void delete(String itemId) {
        Validate.requiredtext(itemId);
        Item removedItem =
                database.getItems().remove(itemId.trim());
        if (removedItem == null) {
            throw new IllegalArgumentException(
                    "Item does not exist."
            );
        }
        database.saveItems();
        AppLogger.info("Item deleted: " + itemId);
    }
    // Generate a unique item ID
    private String createItemId(String type) {
        int number = 1;
        String itemId;
        do {
            itemId = type + String.format("%04d", number);
            number++;
        } while (database.getItems().containsKey(itemId));
        return itemId;
    }
}