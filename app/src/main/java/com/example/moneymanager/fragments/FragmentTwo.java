package com.example.moneymanager.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneymanager.database.model.DatabaseHelper;
import com.example.moneymanager.database.model.Payment;
import com.example.moneymanager.database.model.PaymentAdapter;
import com.example.moneymanager.R;
import com.example.moneymanager.ui.main.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentTwo extends Fragment {
    private static final String type = DatabaseHelper.TYPE_TWO; //TODO •
    private final List<Payment> paymentsList = new ArrayList<>();
    private PaymentAdapter adapter;
    private final Context context;
    private DatabaseHelper db;

    public FragmentTwo(Context mContext) {
        context = mContext;
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentos, container, false);

        db = new DatabaseHelper(context);
        if(paymentsList.isEmpty()) paymentsList.addAll(db.getAllPayments(type));

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        adapter = new PaymentAdapter(paymentsList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(context, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                showPaymentDialog(true, paymentsList.get(position), position);
            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position);
            }
        }));

        return view;
    }



    /**
     * Inserting new payment in db
     * and refreshing the list
     */
    private void createPayment(String name, String price, String details) {
        // inserting note in db and getting
        // newly inserted note id
        long id = db.insertPayment(name, price, details, type);

        // get the newly inserted note from db
        Payment p = db.getPayment(id);

        if (p != null) {
            // adding new note to array list at 0 position
            paymentsList.add(0, p);

            // refreshing the list
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * Updating payment in db and updating
     * item in the list by its position
     */
    private void updatePayment(String name, String price, String details, int position) {
        Payment p = paymentsList.get(position);
        // updating note text
        p.setName(name);
        p.setPrice(price);
        p.setDetails(details);

        // updating note in db
        db.updatePayment(p);

        // refreshing the list
        paymentsList.set(position, p);
        adapter.notifyItemChanged(position);
    }

    /**
     * Deleting payment from SQLite and removing the
     * item from the list by its position
     */
    private void deletePayment(int position) {
        // deleting the note from db
        db.deletePayment(paymentsList.get(position));

        // removing the note from the list
        paymentsList.remove(position);
        adapter.notifyItemRemoved(position);
    }

    /**
     * Opens dialog with Edit - Delete options
     */
    private void showActionsDialog(final int position) {
        CharSequence[] colors = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose option");
        builder.setItems(colors, (dialog, which) -> {
            if (which == 0) {
                showPaymentDialog(true, paymentsList.get(position), position);
            } else {
                deletePayment(position);
            }
        });
        builder.show();
    }


    /**
     * Shows alert dialog with EditText options to enter / edit a payment.
     * when shouldUpdate=true, it automatically displays old payment and changes the
     * button text to UPDATE
     */
    private void showPaymentDialog(final boolean shouldUpdate, final Payment payment, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
        View view = layoutInflaterAndroid.inflate(R.layout.dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);
        alertDialogBuilderUserInput.setView(view);

        final EditText inputName = view.findViewById(R.id.dialog_name_edit_text);
        final EditText inputPrice = view.findViewById(R.id.dialog_price_edit_text);
        final EditText inputDetails = view.findViewById(R.id.dialog_details_edit_text);
        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText(!shouldUpdate ? getString(R.string.fragment_two_new_payment) : getString(R.string.update_payment)); //TODO •

        if (shouldUpdate && payment != null) {
            inputName.setText(payment.getName());
            inputPrice.setText(payment.getPrice());
            inputDetails.setText(payment.getDetails());
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "update" : "save", (dialogBox, id) -> {})
                .setNegativeButton("cancel",
                        (dialogBox, id) -> dialogBox.cancel());

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            // Show toast message when no text is entered
            if (TextUtils.isEmpty(inputName.getText().toString())) {
                Toast.makeText(context, "Please enter a name!", Toast.LENGTH_SHORT).show(); //TODO •
                return;
            } else {
                alertDialog.dismiss();
            }

            // check if user updating note
            if (shouldUpdate && payment != null) {
                // update note by it's id
                updatePayment(inputName.getText().toString(), inputPrice.getText().toString(), inputDetails.getText().toString(), position);
            } else {
                // create new note
                createPayment(inputName.getText().toString(), inputPrice.getText().toString(), inputDetails.getText().toString());
            }
        });
    }

    public void setFabClickListener(){
        showPaymentDialog(false, null, -1);
    }
}
