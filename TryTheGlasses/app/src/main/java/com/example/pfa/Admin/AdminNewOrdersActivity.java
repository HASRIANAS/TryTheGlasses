package com.example.pfa.Admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.pfa.Model.AdminOrders;
import com.example.pfa.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminNewOrdersActivity extends AppCompatActivity {

    private RecyclerView ordersList;
    private DatabaseReference orderRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);

        orderRef = FirebaseDatabase.getInstance().getReference().child("Orders");

        ordersList = findViewById(R.id.orders_list);

        ordersList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AdminOrders> options = new FirebaseRecyclerOptions.Builder<AdminOrders>().setQuery(orderRef, AdminOrders.class).build();
        FirebaseRecyclerAdapter<AdminOrders, AdminOrderViewHolder> adapter = new FirebaseRecyclerAdapter<AdminOrders, AdminOrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminOrderViewHolder holder, final int position, @NonNull final AdminOrders model) {

                holder.userName.setText("Name :" + model.getName());
                holder.userPhoneNumber.setText("Phone :" + model.getPhone());
                holder.userTotalPrix.setText("Total Amount :" + model.getTotalAmount());
                holder.userDateTime.setText("Date :" + model.getDate()+ "  " + model.getTime());
                holder.userShippingAddress.setText("Adresse shipping :" + model.getAddress()+ ", " + model.getCity());

                holder.ShowOrderBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String uID=getRef(position).getKey();
                        Intent intent=new Intent(AdminNewOrdersActivity.this, AdminUserProductsActivity.class);
                        intent.putExtra("uid",uID);
                        startActivity(intent);
                    }
                });

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CharSequence options[]= new CharSequence[]{
                                "yes",
                                "no"
                        };
                        AlertDialog.Builder builder= new AlertDialog.Builder(AdminNewOrdersActivity.this);
                        builder.setTitle("Have you shipped the product");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(i==0){

                                    String uID=getRef(position).getKey();
                                    RemoveOrder(uID);

                                }else {
                                    finish();
                                }

                            }
                        });
                        builder.show();
                    }
                });

            }

            @NonNull
            @Override
            public AdminOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout, parent, false);
                return  new AdminOrderViewHolder(view);
            }
        };

        ordersList.setAdapter(adapter);
        adapter.startListening();
    }

    public static  class AdminOrderViewHolder extends RecyclerView.ViewHolder {

        public TextView userName, userPhoneNumber, userTotalPrix, userDateTime, userShippingAddress;
        public Button ShowOrderBtn;

        public AdminOrderViewHolder(View itemView){
            super(itemView);
            userName = itemView.findViewById(R.id.order_user_name);
            userPhoneNumber = itemView.findViewById(R.id.order_phone_number);
            userTotalPrix = itemView.findViewById(R.id.order_total_price);
            userDateTime = itemView.findViewById(R.id.order_date_time);
            userShippingAddress = itemView.findViewById(R.id.order_address_city);
            ShowOrderBtn = itemView.findViewById(R.id.show_all_products_btn);

        }
    }


    private void RemoveOrder(String uID){


        orderRef.child(uID).removeValue();
    }

}
