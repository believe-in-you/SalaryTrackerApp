package com.example.myapplication.salarytracker.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.salarytracker.R;
import com.example.myapplication.salarytracker.UserDetails.History;

import java.util.List;

import static com.example.myapplication.salarytracker.Adapters.DueDatesAdapter.MONTH_MAPPING;

public class PaymentHistoryDataAdapter extends RecyclerView.Adapter<PaymentHistoryDataAdapter.PaymentHistoryViewHolder>{

    private Context context;
    private List<History> history;

    public PaymentHistoryDataAdapter(Context context, List<History> history) {
        this.context = context;
        this.history = history;
    }


    @NonNull
    @Override
    public PaymentHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflator = LayoutInflater.from(context);
        View view = inflator.inflate(R.layout.payment_history_cardview, parent, false);
        return new PaymentHistoryDataAdapter.PaymentHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentHistoryViewHolder holder, int position) {
        History curr_history = history.get(position);
        holder.month_year.setText(MONTH_MAPPING.get(Integer.parseInt(curr_history.getMonth())) + " " + curr_history.getYear());

        holder.leaves.setText("Leaves taken: " + curr_history.getLeaves());

        holder.total_amount.setText("Amount: Rs " + curr_history.getAmount());
    }

    @Override
    public int getItemCount() {
        return history.size();
    }

    public static class PaymentHistoryViewHolder extends RecyclerView.ViewHolder {
        public TextView month_year, leaves, total_amount;

        public PaymentHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            month_year = itemView.findViewById(R.id.month_year);
            leaves = itemView.findViewById(R.id.leaves);
            total_amount = itemView.findViewById(R.id.total_amount);
        }

    }


}
