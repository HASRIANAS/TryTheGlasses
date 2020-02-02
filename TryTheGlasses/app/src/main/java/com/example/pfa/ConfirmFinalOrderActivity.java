package com.example.pfa;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pfa.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity {
    private EditText nameEditText,phoneEditText,cityEditText,addressCityText,leftEyeEditText,rightEyeEditText;
    private Button orderConfirmButton;
    private String totalAmmount="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        totalAmmount=this.getIntent().getStringExtra("Total Price");
        Toast.makeText(ConfirmFinalOrderActivity.this,"Total Price :"+totalAmmount+"DH",Toast.LENGTH_SHORT).show();
        orderConfirmButton =(Button) findViewById(R.id.order_final_confirm_btn);
        nameEditText=(EditText) findViewById(R.id.order_name);
        phoneEditText=(EditText) findViewById(R.id.order_phone_number);
        cityEditText=(EditText) findViewById(R.id.order_city);
        addressCityText=(EditText) findViewById(R.id.order_adress);
        leftEyeEditText=(EditText) findViewById(R.id.left_eye_mesurment);
        rightEyeEditText=(EditText) findViewById(R.id.righ_eye_mesurment);

        orderConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Check();
            }
        });
    }

    private void Check() {
        if(TextUtils.isEmpty(nameEditText.getText().toString())){
            Toast.makeText(ConfirmFinalOrderActivity.this,"Please provide your name",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phoneEditText.getText().toString())){
            Toast.makeText(ConfirmFinalOrderActivity.this,"Please provide your phone number",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(cityEditText.getText().toString())){
            Toast.makeText(ConfirmFinalOrderActivity.this,"Please provide your city",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(addressCityText.getText().toString())){
            Toast.makeText(ConfirmFinalOrderActivity.this,"Please provide your addrress",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(addressCityText.getText().toString())){
            Toast.makeText(ConfirmFinalOrderActivity.this,"Please provide your addrress",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(addressCityText.getText().toString())){
            Toast.makeText(ConfirmFinalOrderActivity.this,"Please provide your addrress",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(leftEyeEditText.getText().toString())){
            Toast.makeText(ConfirmFinalOrderActivity.this,"Please provide your left eye mesurement",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(rightEyeEditText.getText().toString())){
            Toast.makeText(ConfirmFinalOrderActivity.this,"Please provide your right eye mesurment",Toast.LENGTH_SHORT).show();
        }
        else{
            ConfirmOrder();
        }
    }

    private void ConfirmOrder() {

        final String saveCurrenttime,saveCurrentDate;
        Calendar calfordate=Calendar.getInstance();

        SimpleDateFormat currentDate=new SimpleDateFormat("MMM,dd,yyyy");
        saveCurrentDate=currentDate.format(calfordate.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrenttime=currentTime.format(calfordate.getTime());


        final DatabaseReference orderRef= FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());

        HashMap<String,Object> ordersMap=new HashMap<>();
        ordersMap.put("totalAmount",totalAmmount);
        ordersMap.put("name",nameEditText.getText().toString());
        ordersMap.put("phone",phoneEditText.getText().toString());
        ordersMap.put("date",saveCurrentDate);
        ordersMap.put("time",saveCurrenttime);
        ordersMap.put("address",addressCityText.getText().toString());
        ordersMap.put("city",cityEditText.getText().toString());
        ordersMap.put("rightEye",rightEyeEditText.getText().toString());
        ordersMap.put("leftEye",leftEyeEditText.getText().toString());
        ordersMap.put("Status","not Shipped");
        orderRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference().child("Cart List")
                            .child("User View").child(Prevalent.currentOnlineUser.getPhone()).removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(ConfirmFinalOrderActivity.this,"Your order has been placed succesfully..",Toast.LENGTH_SHORT).show();

                                        Intent intent=new Intent(ConfirmFinalOrderActivity.this,HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }

            }
        });
    }
}
