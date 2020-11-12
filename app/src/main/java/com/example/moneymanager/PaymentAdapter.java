package com.example.moneymanager;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.viewHolder> {
    //private Context context;
    private final List<Payment> paymentsList;

    public PaymentAdapter(/*Context context,*/ List<Payment> paymentsList) {
        //this.context = context;
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
        holder.payment.setText(payment.getProduct());
        holder.dot.setText(Html.fromHtml("&#8226;")); // Displaying dot from HTML character code
        holder.timestamp.setText(getFormattedDate(payment.getTimestamp())); // Formatting and displaying timestamp
    }

    @Override
    public int getItemCount() { return paymentsList.size(); }

    private String getFormattedDate(String dateStr) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = fmt.parse(dateStr);
            SimpleDateFormat fmtOut = new SimpleDateFormat("MMM d", Locale.getDefault());
            return fmtOut.format(date);
        } catch (ParseException e) { Log.d("Error @PaymentAdapter", e.getMessage()); }
        return dateStr; ////////////////////////""//////////////////////////////////////////////////
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        public TextView dot;
        public TextView payment;
        public TextView timestamp;

        public viewHolder(View view) {
            super(view);
            dot = view.findViewById(R.id.dot);
            payment = view.findViewById(R.id.product);
            timestamp = view.findViewById(R.id.timestamp);
        }
    }
}
