package com.example.moneymanager.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneymanager.database.DatabaseHelper;
import com.example.moneymanager.database.model.Payment;
import com.example.moneymanager.database.model.PaymentAdapter;
import com.example.moneymanager.R;
import com.example.moneymanager.ui.RecyclerTouchListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FragmentTwo extends Fragment {
    private static final float LOW_ALPHA = 0.06F;
    private static final float FULL_ALPHA = 1.0F;
    private static final int type = DatabaseHelper.TYPE_TWO; //TODO •
    private static final List<Payment> paymentsList = new ArrayList<>();
    private final FloatingActionButton addFab, searchFab;
    private static TextView fragmentDescriptionTV;
    private static String fragmentDescriptionString;
    private static PaymentAdapter adapter;
    private final Context context;
    private static DatabaseHelper db;

    //Search_bar views
    private EditText searchEditText;
    private ImageButton searchCloseIBtn;
    private boolean inSearchView = false;

    public FragmentTwo(Context mContext, FloatingActionButton addFab, FloatingActionButton searchFab) {
        context = mContext;
        this.addFab = addFab;
        this.searchFab = searchFab;
    } //TODO •

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentos, container, false);
        fragmentDescriptionTV = view.findViewById(R.id.fragment_description);
        searchEditText = view.findViewById(R.id.search_edit_text);
        searchCloseIBtn = view.findViewById(R.id.search_close_btn);
        fragmentDescriptionString = getResources().getText(R.string.fragment_two_description).toString(); // TODO •

        db = new DatabaseHelper(context, type);
        db.getWritableDatabase().close();
        if(paymentsList.isEmpty()) paymentsList.addAll(db.getAllPayments());
        updateFragmentDescription();

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

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    addFab.animate().alpha(LOW_ALPHA);
                    addFab.setAlpha(LOW_ALPHA);
                    searchFab.animate().alpha(LOW_ALPHA);
                    searchFab.setAlpha(LOW_ALPHA);
                } else { // if (newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING)
                    addFab.animate().alpha(FULL_ALPHA);
                    searchFab.animate().alpha(FULL_ALPHA);
                }
            }
        });

        return view;
    }

    private static void updateFragmentDescription(){ //TODO •
        double total = 0;
        for(int i=0; i<paymentsList.size(); i++){
            total += Double.parseDouble(paymentsList.get(i).getPrice());
        }
        fragmentDescriptionTV.setText(String.format(fragmentDescriptionString,total));
    }


    /**
     * Inserting new payment in db
     * and refreshing the list
     */
    private void createPayment(String name, String price, String details) {
        // inserting payment in db and getting newly inserted payment id
        long id = db.insertPayment(name, price, details);

        // get the newly inserted payment from db
        Payment p = db.getPayment(id);

        if (p != null) {
            // adding new payment to array list at 0 position
            paymentsList.add(0, p);

            // refreshing the list
            adapter.notifyDataSetChanged();
            updateFragmentDescription();
        }
    }

    /**
     * Updating payment in db and updating
     * item in the list by its position
     */
    static void updatePayment(String name, String price, String details, int position) {
        if(position<0) return;
        Payment p = paymentsList.get(position);
        // updating payment text
        p.setName(name);
        p.setPrice(price);
        p.setDetails(details);

        // updating payment in db
        db.updatePayment(p);

        // refreshing the list
        paymentsList.set(position, p);
        adapter.notifyItemChanged(position);
        updateFragmentDescription();
    }

    /**
     * Deleting payment from SQLite and removing the
     * item from the list by its position
     */
    static void deletePayment(int position) {
        if(position<0) return;
        // deleting the payment from db
        db.deletePayment(paymentsList.get(position));

        // removing the payment from the list
        paymentsList.remove(position);
        adapter.notifyItemRemoved(position);
        updateFragmentDescription();
    }

    static int getPosition(Payment payment){
        for(int i=0; i<paymentsList.size(); i++){
            if(paymentsList.get(i).getId() == payment.getId())
                return i;
        }
        return -1;
    }

    /**
     * Opens dialog with Edit - Delete options
     */
    private void showActionsDialog(final int position) {
        CharSequence[] colors = new CharSequence[]{"Edit", "Merge", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose option");
        builder.setItems(colors, (dialog, which) -> {
            if (which == 0) {
                showPaymentDialog(true, paymentsList.get(position), position);
            }
            else if (which == 1) {
                if(!createMergePaymentDialog(paymentsList.get(position)))
                    Toast.makeText(context, "You've run out of friends! :(", Toast.LENGTH_SHORT).show();
            }
            else {
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
                .setCancelable(true)
                .setPositiveButton(shouldUpdate ? "update" : "save", (dialogBox, id) -> {})
                .setNegativeButton("cancel",
                        (dialogBox, id) -> dialogBox.cancel());

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);

        inputName.requestFocus();
        toggleKeyboard(inputName, true);

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            // Show toast message when no name or price is entered
            if(TextUtils.isEmpty(inputName.getText().toString()) && TextUtils.isEmpty(inputPrice.getText().toString())){
                Toast.makeText(context, "Please enter a name and price!", Toast.LENGTH_SHORT).show(); //TODO •
                inputName.requestFocus();
                return;
            }
            if(TextUtils.isEmpty(inputPrice.getText().toString())){
                Toast.makeText(context, "Please enter a price!", Toast.LENGTH_SHORT).show();
                inputPrice.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(inputName.getText().toString())) {
                Toast.makeText(context, "Please enter a name!", Toast.LENGTH_SHORT).show(); //TODO •
                inputName.requestFocus();
                return;
            } else {
                alertDialog.dismiss();
            }

            Payment p;
            // check if user updating payment
            if (shouldUpdate && payment != null) {
                // update payment by it's id
                updatePayment(inputName.getText().toString(), inputPrice.getText().toString(), inputDetails.getText().toString(), position);
                p = paymentsList.get(position);
            } else {
                // create new payment
                createPayment(inputName.getText().toString(), inputPrice.getText().toString(), inputDetails.getText().toString());
                p = paymentsList.get(0);
            }
            enableSearchView(false);
            createMergePaymentDialog(p);
        });
    }

    private boolean createMergePaymentDialog(Payment paymentToBeMerged){
        DatabaseHelper dbTypeThree = new DatabaseHelper(context, DatabaseHelper.TYPE_THREE);
        List<Payment> list = new ArrayList<>(dbTypeThree.searchPaymentByName(paymentToBeMerged.getName()));
        if(list.isEmpty()) return false;

        CharSequence[] paymentNames = new CharSequence[list.size()];
        for(int i=0; i<paymentNames.length; i++){
            paymentNames[i] = list.get(i).getName()+" ("+list.get(i).getPrice()+"€)";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("The following people also owe you money. Merge?");
        builder.setItems(paymentNames, (dialog, which) -> {
            mergePayment(paymentToBeMerged, list.get(which));
        });
        builder.setNegativeButton("Nope", (dialogInterface, i) -> {
                    dialogInterface.cancel();
                }).show();

        return true;
    }

    private void mergePayment(Payment paymentFromTwo, Payment paymentFromThree){
        double p2 = Double.parseDouble(paymentFromTwo.getPrice()), p3 = Double.parseDouble(paymentFromThree.getPrice());
        double newPrice;

        if(p2>p3){
            newPrice = p2-p3;
            // • Delete payment from 3
            // • Update payment price from 2
            FragmentThree.deletePayment(FragmentThree.getPosition(paymentFromThree));
            updatePayment(paymentFromTwo.getName(),
                    Double.toString(newPrice),
                    paymentFromTwo.getDetails(),
                    getPosition(paymentFromThree));
        }
        else if(p2<p3){
            newPrice = p3-p2;
            // • Delete payment from 2
            // • Update payment price from 3
            deletePayment(getPosition(paymentFromTwo));
            FragmentThree.updatePayment(paymentFromThree.getName(),
                    Double.toString(newPrice),
                    paymentFromThree.getDetails(),
                    FragmentThree.getPosition(paymentFromThree));
        }
        else{
            Toast.makeText(context, "Same amount -> deleted both.", Toast.LENGTH_SHORT).show();
            deletePayment(getPosition(paymentFromTwo));
            FragmentThree.deletePayment(FragmentThree.getPosition(paymentFromThree));
        }
    }

    public void setAddFabClickListener(){ showPaymentDialog(false, null, -1); }


    public void setSearchFabClickListener(){
        if(!inSearchView) { //Open Search View
            enableSearchView(true);
            setSearchCloseIBtnListener();
            searchEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence cs, int start, int before, int count) {
                    ArrayList<Payment> sublist = new ArrayList<Payment>(db.searchPaymentByName(cs.toString()));
                    paymentsList.clear();
                    paymentsList.addAll(sublist);
                    adapter.notifyDataSetChanged();
                    if(paymentsList.isEmpty())
                        Toast.makeText(context, "List is empty my friend!", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void afterTextChanged(Editable s) {}
            });
        }
    }

    private void setSearchCloseIBtnListener(){
        searchCloseIBtn.setOnClickListener(view -> {
            if(inSearchView){
                paymentsList.clear();
                paymentsList.addAll(db.getAllPayments());
                adapter.notifyDataSetChanged();
                enableSearchView(false);
            }
        });
    }

    private void enableSearchView(boolean show){
        inSearchView = show;
        if(show){
            searchEditText.setVisibility(View.VISIBLE);
            searchFab.setVisibility(View.GONE);
            searchCloseIBtn.setVisibility(View.VISIBLE);
            searchCloseIBtn.bringToFront();
            fragmentDescriptionTV.setVisibility(View.INVISIBLE);
            toggleKeyboard(searchEditText, true);
            searchEditText.requestFocus();
        }
        else{
            searchEditText.setText("");
            searchEditText.setVisibility(View.GONE);
            searchCloseIBtn.setVisibility(View.GONE);
            fragmentDescriptionTV.setVisibility(View.VISIBLE);
            toggleKeyboard(searchEditText, false);
            searchFab.setAlpha(0F);
            searchFab.setVisibility(View.VISIBLE);
            searchFab.animate().alpha(FULL_ALPHA);
            updateFragmentDescription();
        }
    }

    private void toggleKeyboard(View view, boolean show){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(show) imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
        else imm.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
}