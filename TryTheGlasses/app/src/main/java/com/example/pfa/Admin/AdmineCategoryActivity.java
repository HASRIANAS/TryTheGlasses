package com.example.pfa.Admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.pfa.HomeActivity;
import com.example.pfa.MainActivity;
import com.example.pfa.R;

public class AdmineCategoryActivity extends AppCompatActivity {


    private ImageView carre, rectangulaire, aviateur, sportive,papillon,monoverre;

    private Button LogoutBtn, CheckOrderBtn,maintainBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admine_category);


        LogoutBtn = (Button) findViewById(R.id.admin_logout_btn);
        CheckOrderBtn = (Button) findViewById(R.id.check_orders_btn);
        maintainBtn=(Button) findViewById(R.id.maintain_btn);




        maintainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdmineCategoryActivity.this, HomeActivity.class);
                intent.putExtra("admin","admin");
                startActivity(intent);


            }
        });

        LogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdmineCategoryActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        CheckOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdmineCategoryActivity.this, AdminNewOrdersActivity.class);
                startActivity(intent);
            }
        });

        carre = (ImageView) findViewById(R.id.carre);
        rectangulaire = (ImageView) findViewById(R.id.rectangulaire);
        aviateur = (ImageView) findViewById(R.id.aviateur);
        sportive = (ImageView) findViewById(R.id.sportive);
        papillon = (ImageView) findViewById(R.id.papillon);
        monoverre = (ImageView) findViewById(R.id.monoverre);



        carre.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdmineCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "Carre");
                startActivity(intent);
            }
        });


        rectangulaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdmineCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "Rectangulaire");
                startActivity(intent);
            }
        });


        aviateur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdmineCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "Aviateur");
                startActivity(intent);
            }
        });


        sportive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdmineCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "Sportive");
                startActivity(intent);
            }
        });


        papillon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdmineCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "Papillon");
                startActivity(intent);
            }
        });


        monoverre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdmineCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "Monoverre");
                startActivity(intent);
            }
        });

    }
}
