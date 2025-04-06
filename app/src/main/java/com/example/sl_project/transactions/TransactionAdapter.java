package com.example.sl_project.transactions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private Context context;
    private List<Transaction> transactionList;
    private OnTransactionClickListener listener;

    public TransactionAdapter(Context context, List<Transaction> transactionList, OnTransactionClickListener listener) {
        this.context = context;
        this.transactionList = transactionList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.transaction_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);

        // Format amount with currency symbol
        String amountText = String.format("₹%.2f", transaction.getAmount());

        // Format timestamp to readable date
        String dateText = formatDate(transaction.getTimestamp());

        // Set text colors based on transaction type
        if (transaction.getType().equals("Income")) {
            holder.tvAmount.setTextColor(context.getResources().getColor(R.color.green));
            holder.tvAmount.setText("+" + amountText);
        } else {
            holder.tvAmount.setTextColor(context.getResources().getColor(R.color.red));
            holder.tvAmount.setText("-" + amountText);
        }

        holder.tvCategory.setText(transaction.getCategory());
        holder.tvPaymentMethod.setText(transaction.getPaymentMethod());
        holder.tvDate.setText(dateText);

        if (!transaction.getDescription().isEmpty()) {
            holder.tvDescription.setVisibility(View.VISIBLE);
            holder.tvDescription.setText(transaction.getDescription());
        } else {
            holder.tvDescription.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    private String formatDate(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());
        return sdf.format(date);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvAmount, tvCategory, tvPaymentMethod, tvDescription, tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvPaymentMethod = itemView.findViewById(R.id.tvPaymentMethod);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvDate = itemView.findViewById(R.id.tvDate);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION && listener != null) {
                listener.onTransactionClick(transactionList.get(position));
            }
        }
    }

    public interface OnTransactionClickListener {
        void onTransactionClick(Transaction transaction);
    }
}