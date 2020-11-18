package com.example.moneymanager.database.model;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneymanager.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.viewHolder> {
    private final List<Payment> paymentsList;

    public PaymentAdapter(List<Payment> paymentsList) {
        this.paymentsList = paymentsList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_view, parent, false);
        return new viewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Payment payment = paymentsList.get(position);
        holder.price.setText(payment.getPrice());
        holder.name.setText(payment.getName());
        if(payment.getDetails().equals("")) holder.details.setVisibility(View.GONE);
        else{
            holder.details.setText(payment.getDetails());
            holder.details.setVisibility(View.VISIBLE);
        }
        holder.dot.setText(Html.fromHtml("&#8226;")); // Displaying dot from HTML character code
        holder.timestamp.setText(getFormattedDate(payment.getTimestamp())); // Formatting and displaying timestamp
    }

    @Override
    public int getItemCount() { return paymentsList.size(); }

    private String getFormattedDate(String dateStr) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = fmt.parse(dateStr);
            SimpleDateFormat fmtOut = new SimpleDateFormat("MMM d,  HH:mm", Locale.getDefault());
            if(date==null){ return ""; }
            return fmtOut.format(date);
        } catch (ParseException e) { Log.d("Error @PaymentAdapter", e.getMessage()); }
        return "";
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        public TextView dot;
        public TextView timestamp;
        public TextView name;
        public TextView price;
        public TextView details;

        public viewHolder(View view) {
            super(view);
            dot = view.findViewById(R.id.dot);
            timestamp = view.findViewById(R.id.timestamp);
            name = view.findViewById(R.id.name_text_view);
            price = view.findViewById(R.id.price);
            details = view.findViewById(R.id.details_text_view);
        }
    }
}
