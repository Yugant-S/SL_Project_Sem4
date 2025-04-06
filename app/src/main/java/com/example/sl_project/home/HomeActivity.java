package com.example.sl_project.home;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.R;
import com.example.sl_project.database.DatabaseHelper;
import com.example.sl_project.profile.ProfileActivity;
import com.example.sl_project.stats.StatisticsActivity;
import com.example.sl_project.transactions.AddTransactions;
import com.example.sl_project.transactions.Transaction;
import com.example.sl_project.transactions.TransactionListActivity;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private HomeTransactionAdapter adapter;
    private PieChart pieChart;
    private TextView analyticsShowMore, transactionsShowMore;
    private TextView incomeAmount;
    private TextView budgetExceeded;
    private TextView expenseAmount;
    private DatabaseHelper dbHelper;

    private EditText budgetAmount;
    private ProgressBar budgetProgressBar;
    private TextView budgetPercentage;
    private BottomNavigationView bottomNav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        // Initialize UI elements
        recyclerView = findViewById(R.id.recentTransactionsRecycler);
        pieChart = findViewById(R.id.expensePieChart);
        analyticsShowMore = findViewById(R.id.analyticsShowMore);
        transactionsShowMore = findViewById(R.id.transactionsShowMore);
        bottomNav = findViewById(R.id.bottomNav);
        incomeAmount = findViewById(R.id.incomeAmount);
        expenseAmount = findViewById(R.id.expenseAmount);
        budgetAmount = findViewById(R.id.budgetAmount);
        budgetProgressBar = findViewById(R.id.budgetProgressBar);
        budgetPercentage = findViewById(R.id.budgetPercentage);
        budgetExceeded = findViewById(R.id.budgetExceeded);
        dbHelper = new DatabaseHelper(this);
        updateIncomeAndExpense();

        // Set up RecyclerView
        setupRecyclerView();

        // Set up Pie Chart
        setupPieChart();
        updateProgressBar();

        // Handle "Show More" click events
        analyticsShowMore.setOnClickListener(view -> showAnalyticsDetails());
        transactionsShowMore.setOnClickListener(view -> showTransactionsList());

        // Set up Bottom Navigation
        setupBottomNavigation();

        budgetAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Clear any previous error when the user starts typing
                budgetAmount.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                String budgetText = s.toString();
                double budget = 0;

                if (budgetText.isEmpty()) {
                    // Handle empty input (e.g., set budget to 0 or show an error)
                    budgetProgressBar.setProgress(0);
                    budgetPercentage.setText("0%");
                    budgetAmount.setError("Budget cannot be empty");
                    return; // Exit the method early
                }

                try {
                    budget = Double.parseDouble(budgetText);
                } catch (NumberFormatException e) {
                    // Handle invalid number format (e.g., show an error)
                    budgetProgressBar.setProgress(0);
                    budgetPercentage.setText("0%");
                    budgetAmount.setError("Invalid budget amount");
                    return; // Exit the method early
                }

                if (budget <= 0) {
                    // Handle non-positive budget (e.g., show an error)
                    budgetProgressBar.setProgress(0);
                    budgetPercentage.setText("0%");
                    budgetAmount.setError("Budget must be greater than 0");
                    return; // Exit the method early
                }

                // If we reach here, the budget is valid
                updateProgressBar();
            }
        });
    }
    private void updateIncomeAndExpense() {
        double totalIncome = dbHelper.getTotalIncome();
        double totalExpenses = dbHelper.getTotalExpenses();

        incomeAmount.setText(String.format("₹%.2f", totalIncome));
        expenseAmount.setText(String.format("₹%.2f", totalExpenses));
    }

    private List<HomeTransaction> fetchAllTransactions() {
        List<HomeTransaction> homeTransactionList = new ArrayList<>();
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM transactions ORDER BY timestamp DESC", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                double amount = cursor.getDouble(cursor.getColumnIndexOrThrow("amount"));
                String category = cursor.getString(cursor.getColumnIndexOrThrow("category"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                String paymentMethod = cursor.getString(cursor.getColumnIndexOrThrow("payment_method"));
                long timestamp = cursor.getLong(cursor.getColumnIndexOrThrow("timestamp"));
                String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));

                homeTransactionList.add(new HomeTransaction(id, amount, category, description, paymentMethod, timestamp, type));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return homeTransactionList;
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<HomeTransaction> homeTransactionList = fetchAllTransactions();

        // Optional: Show only the 3 most recent
        int maxItems = 3;
        if (homeTransactionList.size() > maxItems) {
            homeTransactionList = homeTransactionList.subList(0, maxItems);
        }

        adapter = new HomeTransactionAdapter(this, homeTransactionList);
        recyclerView.setAdapter(adapter);
    }

    private void setupPieChart() {
        List<PieEntry> entries = getCategorySumsForPieChart();

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setDrawValues(false);
        dataSet.setColors(new int[]{R.color.red, R.color.blue, R.color.green, R.color.black, R.color.grey}, this);

        PieData pieData = new PieData(dataSet);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setUsePercentValues(false);
        pieChart.animateY(1000);
        pieChart.setDrawEntryLabels(false);

        Legend legend = pieChart.getLegend();
        legend.setEnabled(true);
    }

    private List<PieEntry> getCategorySumsForPieChart() {
        List<PieEntry> entries = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT category, SUM(CAST(REPLACE(amount, '₹', '') AS REAL)) FROM transactions GROUP BY category", null);

        if (cursor.moveToFirst()) {
            do {
                String category = cursor.getString(0);
                float total = cursor.getFloat(1);
                entries.add(new PieEntry(total, category));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return entries;
    }

    private void setupBottomNavigation() {
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Intent allTransactions = new Intent(this, TransactionListActivity.class);
            Intent addTransaction = new Intent(this, AddTransactions.class);
            Intent stats = new Intent(this, StatisticsActivity.class);
            Intent profile = new Intent(this, ProfileActivity.class);
            // Declare intent here

            if (itemId == R.id.nav_home) {
                // Already on Home, do nothing or refresh
                return true;
            }else if (itemId == R.id.nav_add) {
                startActivity(addTransaction);
                return true;
            } else if (itemId == R.id.nav_transactions) {
                startActivity(allTransactions);
                return true;
            } else if (itemId == R.id.nav_statistics) {
                startActivity(stats);
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(profile);
                return true;
            } else {
                return false; // Unknown item
            }

            //startActivity(intent); // Start the activity
            //return true;
        });
    }
    private void showAnalyticsDetails() {
        //Intent intent = new Intent(this, AnalyticsActivity.class);
        //startActivity(intent);
    }

    private void showTransactionsList() {
            //Intent intent = new Intent(this, TransactionsActivity.class);
            //startActivity(intent);
    }

    private void updateProgressBar() {
        String budgetText = budgetAmount.getText().toString();
        double budget = 0;
        if (!budgetText.isEmpty()) {
            budget = Double.parseDouble(budgetText);
        }

        double totalExpenses = dbHelper.getTotalExpenses();

        if (budget > 0) {
            int progress = (int) ((totalExpenses / budget) * 100);
            budgetProgressBar.setProgress(progress);
            budgetPercentage.setText(String.format("%d%%", progress));

            // Check if budget is exceeded
            if (progress > 100) {
                budgetExceeded.setVisibility(View.VISIBLE);
                budgetAmount.setTextColor(Color.RED);
            } else {
                budgetExceeded.setVisibility(View.GONE);
                budgetAmount.setTextColor(Color.WHITE);
            }
        } else {
            budgetProgressBar.setProgress(0);
            budgetPercentage.setText("0%");
            budgetExceeded.setVisibility(View.GONE);
            budgetAmount.setTextColor(Color.WHITE);
        }
    }
}
