package com.example.sl_project.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Info
    private static final String DATABASE_NAME = "FinanceTracker.db";
    private static final int DATABASE_VERSION =3;

    // Table Names
    public static final String TABLE_TRANSACTIONS = "transactions";

    // Transaction Table Columns
    public static final String KEY_TRANSACTION_ID = "id";
    public static final String KEY_TRANSACTION_AMOUNT = "amount";
    public static final String KEY_TRANSACTION_CATEGORY = "category";
    public static final String KEY_TRANSACTION_PAYMENT_METHOD = "payment_method";
    public static final String KEY_TRANSACTION_DESCRIPTION = "description";
    public static final String KEY_TRANSACTION_TIMESTAMP = "timestamp";
    public static final String KEY_TRANSACTION_TYPE = "type";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TRANSACTIONS_TABLE = "CREATE TABLE " + TABLE_TRANSACTIONS +
                "(" +
                KEY_TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_TRANSACTION_AMOUNT + " REAL NOT NULL," +
                KEY_TRANSACTION_CATEGORY + " TEXT," +
                KEY_TRANSACTION_PAYMENT_METHOD + " TEXT," +
                KEY_TRANSACTION_DESCRIPTION + " TEXT," +
                KEY_TRANSACTION_TIMESTAMP + " INTEGER," +
                KEY_TRANSACTION_TYPE + " TEXT" +
                ")";

        db.execSQL(CREATE_TRANSACTIONS_TABLE);
        addDummyData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Drop older table if existed
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);
            // Create tables again
            onCreate(db);
        }
    }

    private void addDummyData(SQLiteDatabase db) {
        // Dummy transaction 1
        ContentValues values1 = new ContentValues();
        values1.put(KEY_TRANSACTION_AMOUNT, 150.0);
        values1.put(KEY_TRANSACTION_CATEGORY, "Food");
        values1.put(KEY_TRANSACTION_PAYMENT_METHOD, "Cash");
        values1.put(KEY_TRANSACTION_DESCRIPTION, "Lunch");
        values1.put(KEY_TRANSACTION_TIMESTAMP, System.currentTimeMillis());
        values1.put(KEY_TRANSACTION_TYPE, "Expense");

        db.insert(TABLE_TRANSACTIONS, null, values1);

        // Dummy transaction 2
        ContentValues values2 = new ContentValues();
        values2.put(KEY_TRANSACTION_AMOUNT, 50.0);
        values2.put(KEY_TRANSACTION_CATEGORY, "Transport");
        values2.put(KEY_TRANSACTION_PAYMENT_METHOD, "Card");
        values2.put(KEY_TRANSACTION_DESCRIPTION, "Metro Ticket");
        values2.put(KEY_TRANSACTION_TIMESTAMP, System.currentTimeMillis());
        values2.put(KEY_TRANSACTION_TYPE, "Expense");
        db.insert(TABLE_TRANSACTIONS, null, values2);

        // Dummy transaction 3
        ContentValues values3 = new ContentValues();
        values3.put(KEY_TRANSACTION_AMOUNT, 2000.0);
        values3.put(KEY_TRANSACTION_CATEGORY, "Salary");
        values3.put(KEY_TRANSACTION_PAYMENT_METHOD, "Bank");
        values3.put(KEY_TRANSACTION_DESCRIPTION, "Monthly");
        values3.put(KEY_TRANSACTION_TIMESTAMP, System.currentTimeMillis());
        values3.put(KEY_TRANSACTION_TYPE, "Income");
        db.insert(TABLE_TRANSACTIONS, null, values3);
    }

    public double getTotalIncome() {
        SQLiteDatabase db = this.getReadableDatabase();
        double totalIncome = 0.0;
        String query = "SELECT SUM(" + KEY_TRANSACTION_AMOUNT + ") FROM " + TABLE_TRANSACTIONS +
                " WHERE " + KEY_TRANSACTION_TYPE + " = 'Income'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            totalIncome = cursor.getDouble(0);
        }
        cursor.close();
        return totalIncome;
    }

    public double getTotalExpenses() {
        SQLiteDatabase db = this.getReadableDatabase();
        double totalExpenses = 0.0;
        String query = "SELECT SUM(" + KEY_TRANSACTION_AMOUNT + ") FROM " + TABLE_TRANSACTIONS +
                " WHERE " + KEY_TRANSACTION_TYPE + " = 'Expense'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            totalExpenses = cursor.getDouble(0);
        }
        cursor.close();
        return totalExpenses;
    }
}

