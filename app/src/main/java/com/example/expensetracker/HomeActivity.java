package com.example.expensetracker;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TransactionAdapter adapter;
    private PieChart pieChart;
    private TextView analyticsShowMore, transactionsShowMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);  // Ensure this matches your layout filename

        // Initialize UI elements
        recyclerView = findViewById(R.id.recentTransactionsRecycler);
        pieChart = findViewById(R.id.expensePieChart);
        analyticsShowMore = findViewById(R.id.analyticsShowMore);
        transactionsShowMore = findViewById(R.id.transactionsShowMore);
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

        // Set up RecyclerView
        setupRecyclerView();

        // Set up Pie Chart
        setupPieChart();

        // Handle "Show More" click events
        analyticsShowMore.setOnClickListener(view -> showAnalyticsDetails());
        transactionsShowMore.setOnClickListener(view -> showTransactionsList());

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                return true;
            } else if (itemId == R.id.nav_transactions) {
                return true;
            } else if (itemId == R.id.nav_statistics) {
                return true;
            } else return itemId == R.id.nav_profile;
        });
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(new Transaction("Food", "Lunch", "-₹150", R.drawable.ic_add));
        transactionList.add(new Transaction("Transport", "Metro Ticket", "-₹50", R.drawable.ic_add));

        adapter = new TransactionAdapter(transactionList);
        recyclerView.setAdapter(adapter);
    }

    private void setupPieChart() {
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(30f, "Food"));
        entries.add(new PieEntry(20f, "Transport"));
        entries.add(new PieEntry(50f, "Shopping"));

        PieDataSet dataSet = new PieDataSet(entries, "Expenses");
        dataSet.setColors(new int[]{R.color.red, R.color.blue, R.color.green}, this);
        PieData pieData = new PieData(dataSet);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setUsePercentValues(true);
        pieChart.animateY(1000);

        Legend legend = pieChart.getLegend();
        legend.setEnabled(true);
    }

    private void showAnalyticsDetails() {
        // Navigate to Analytics screen
    }

    private void showTransactionsList() {
        // Navigate to Transactions screen
    }
}
