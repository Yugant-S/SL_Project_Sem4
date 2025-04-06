package com.example.sl_project.transactions;

public class Transaction {
    private long id;
    private double amount;
    private String category;
    private String paymentMethod;
    private String description;
    private long timestamp;
    private String type;

    public Transaction() {
    }

    public Transaction(long id, double amount, String category, String paymentMethod,
                       String description, long timestamp, String type) {
        this.id = id;
        this.amount = amount;
        this.category = category;
        this.paymentMethod = paymentMethod;
        this.description = description;
        this.timestamp = timestamp;
        this.type = type;
    }

    // Getters and setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}