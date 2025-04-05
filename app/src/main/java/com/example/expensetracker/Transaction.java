package com.example.expensetracker;

public class Transaction {
    private final String category;
    private final String name;
    private final String amount;
    private final int iconResId;

    public Transaction(String category, String name, String amount, int iconResId) {
        this.category = category;
        this.name = name;
        this.amount = amount;
        this.iconResId = iconResId;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getAmount() {
        return amount;
    }

    public int getIconResId() {
        return iconResId;
    }
}
