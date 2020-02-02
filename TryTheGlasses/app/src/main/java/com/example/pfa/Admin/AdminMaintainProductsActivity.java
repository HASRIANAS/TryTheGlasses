package com.example.pfa.Admin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pfa.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainProductsActivity extends AppCompatActivity {
    private Button applyChangesBtn,deleteBtn;
    private EditText name,price,description;
    private ImageView imageView;
    private String productId="";
    private DatabaseReference productsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_products);

        productId=getIntent().getStringExtra("pid");
        productsRef= FirebaseDatabase.getInstance().getReference().child("Products").child(productId);
        applyChangesBtn=(Button) findViewById(R.id.apply_changes_btn);
        name=(EditText) findViewById(R.id.product_name_maintain);
        price=(EditText) findViewById(R.id.product_price_maintain);
        description=(EditText) findViewById(R.id.product_description_maintain);
        imageView=(ImageView) findViewById(R.id.product_image_maintain);
        deleteBtn=(Button) findViewById(R.id.delete_product_btn);

        displaySpecificProductInfo();

        applyChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyChanges();
            }
        });


        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteThis();
            }
        });


    }



    private void deleteThis() {

        productsRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(AdminMaintainProductsActivity.this,"This product have been deleted succesfully",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AdminMaintainProductsActivity.this, AdmineCategoryActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }



    private void applyChanges() {
        String pname=name.getText().toString();
        String pPrice=price.getText().toString();

        String pdescription=description.getText().toString();


        if(pname.equals("")){
            Toast.makeText(AdminMaintainProductsActivity.this,"Write down product name",Toast.LENGTH_SHORT).show();

        }else  if(pPrice.equals("")){
            Toast.makeText(AdminMaintainProductsActivity.this,"Write down product price",Toast.LENGTH_SHORT).show();

        }else  if(pdescription.equals("")){
            Toast.makeText(AdminMaintainProductsActivity.this,"Write down product description",Toast.LENGTH_SHORT).show();

        }else {

            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put("pid", productId);

            productMap.put("description", pdescription);

            productMap.put("price", pPrice);
            productMap.put("pname", pname);

            productsRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(AdminMaintainProductsActivity.this,"Changes applied to product succesfully",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AdminMaintainProductsActivity.this, AdmineCategoryActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }
            });
        }


    }

    private void displaySpecificProductInfo() {

        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String pName=dataSnapshot.child("pname").getValue().toString();
                    String pPrice=dataSnapshot.child("price").getValue().toString();
                    String pDescription=dataSnapshot.child("description").getValue().toString();
                    String pImage=dataSnapshot.child("image").getValue().toString();

                    name.setText(pName);
                    price.setText(pPrice);
                    description.setText(pDescription);
                    Picasso.get().load(pImage).into(imageView);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
