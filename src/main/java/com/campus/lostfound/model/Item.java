package com.campus.lostfound.model;
import java.io.Serializable;
import java.time.LocalDate;
public abstract class Item implements Serializable {
    private final String itemId;
    private final String reporterId;
    private String name;
    private String category;
    private String location;
    private LocalDate date;
    private String description;
    private ItemStatus status;
    // Constructor
    protected Item(
            String itemId,
            String reporterId,
            String name,
            String category,
            String location,
            LocalDate date,
            String description,
            ItemStatus status) {
        this.itemId = itemId;
        this.reporterId = reporterId;
        this.name = name;
        this.category = category;
        this.location = location;
        this.date = date;
        this.description = description;
        this.status = status;
    }
    // Getters
    public String getItemId() {
        return itemId;
    }
    public String getReporterId() {
        return reporterId;
    }
    public String getName() {
        return name;
    }
    public String getCategory() {
        return category;
    }
    public String getLocation() {
        return location;
    }
    public LocalDate getDate() {
        return date;
    }
    public String getDescription() {
        return description;
    }
    public ItemStatus getStatus() {
        return status;
    }
    // Setters
    public void setName(String name) {
        this.name = name;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setStatus(ItemStatus status) {
        this.status = status;
    }
    // This will be implemented by LostItem and FoundItem
    public abstract String getType();
    // Display item information
    @Override
    public String toString() {
        return itemId
                + " | " + getType()
                + " | " + name
                + " | " + category
                + " | " + location
                + " | " + date
                + " | " + status;
    }
}