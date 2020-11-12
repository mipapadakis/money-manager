package com.example.moneymanager;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class FragmentOne extends Fragment {
    private final Context context;

    public FragmentOne(Context mContext) {
        context = mContext;
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one, container, false);

        List<Payment> paymentList = List.of(new Payment(1, "product 1", "JAN 1"),
                new  Payment(2, "product 2", "FEB 1"),
                new  Payment(3, "product 3", "MAR 1"),
                new  Payment(4, "product 4", "APR 1"),
                new  Payment(5, "product 5", "MAY 1"));

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        PaymentAdapter adapter = new PaymentAdapter(paymentList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
