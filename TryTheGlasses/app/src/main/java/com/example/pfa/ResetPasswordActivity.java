package com.example.pfa;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pfa.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ResetPasswordActivity extends AppCompatActivity {

    private String check="";
    private TextView pageTitle,titleQuestion;
    private EditText phoneNumber,question1,question2;
    private Button verify;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        pageTitle=(TextView) findViewById(R.id.reset_password_tv);
        titleQuestion=(TextView) findViewById(R.id.title_question);
        phoneNumber=(EditText) findViewById(R.id.find_phone_number);
        question1=(EditText) findViewById(R.id.question_1);
        question2=(EditText) findViewById(R.id.question_2);
        verify=(Button) findViewById(R.id.verify);



        check=getIntent().getStringExtra("check");
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(check.equals("settings")){
            pageTitle.setText("Set Question");
            phoneNumber.setVisibility(View.GONE);
            titleQuestion.setText("Please set an answer to the following questions");
            verify.setText("Set");
            displayPreviousAnsers();

            verify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setAnswers();
                }
            });

        }else if(check.equals("login")){
            phoneNumber.setVisibility(View.VISIBLE);

            verify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    verifyUser();
                }
            });


        }
    }

    private void verifyUser() {
        final String phone=phoneNumber.getText().toString();
        final String answer1=question1.getText().toString().toLowerCase();
        final String answer2=question2.getText().toString().toLowerCase();
        if(!phone.equals("")&& !answer1.equals("") && !answer2.equals("")){

            final DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Users")
                    .child(phone);

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        String mphone=dataSnapshot.child("phone").getValue().toString();

                        if(dataSnapshot.hasChild("Security Question")){
                            String ans1=dataSnapshot.child("Security Question").child("answer1").getValue().toString();
                            String ans2=dataSnapshot.child("Security Question").child("answer2").getValue().toString();
                            if(!ans1.equals(answer1)){
                                Toast.makeText(ResetPasswordActivity.this,"Your first answer is incorrect",Toast.LENGTH_SHORT).show();
                            }else if(!ans2.equals(answer2)){
                                Toast.makeText(ResetPasswordActivity.this,"Your second answer is incorrect",Toast.LENGTH_SHORT).show();
                            }else
                            {
                                AlertDialog.Builder builder=new AlertDialog.Builder(ResetPasswordActivity.this);
                                builder.setTitle("New Password");
                                final EditText newPassword=new EditText(ResetPasswordActivity.this);
                                newPassword.setHint("Write new Password..");
                                builder.setView(newPassword);

                                builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if(!newPassword.getText().toString().equals("")){
                                            ref.child("password").setValue(newPassword.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Toast.makeText(ResetPasswordActivity.this,"Password Changed Succesfully",Toast.LENGTH_SHORT).show();
                                                            Intent intent=new Intent(ResetPasswordActivity.this,LoginActivity.class);
                                                            startActivity(intent);
                                                        }
                                                    });
                                        }

                                    }
                                });

                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                                builder.show();
                            }
                        }
                        else{
                            Toast.makeText(ResetPasswordActivity.this,"You didnt set the security questions",Toast.LENGTH_SHORT).show();
                        }
                    }else
                    {
                        Toast.makeText(ResetPasswordActivity.this,"Phone number doesnt exist",Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        else
        {
            Toast.makeText(ResetPasswordActivity.this,"Please fill fields above ",Toast.LENGTH_SHORT).show();
        }




    }

    public void setAnswers(){
        String answer1=question1.getText().toString().toLowerCase();
        String answer2=question2.getText().toString().toLowerCase();

        if(answer1.equals("")&& answer2.equals("")){
            Toast.makeText(ResetPasswordActivity.this,"Please answer the First Question",Toast.LENGTH_SHORT).show();
        } else
        {
            DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Users")
                    .child(Prevalent.currentOnlineUser.getPhone());

            HashMap<String, Object> userdataMap = new HashMap<>();
            userdataMap.put("answer1", answer1);
            userdataMap.put("answer2", answer2);

            ref.child("Security Question").updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(ResetPasswordActivity.this,"Security Questions Set",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(ResetPasswordActivity.this,HomeActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }
    }


    public void displayPreviousAnsers(){
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Users")
                .child(Prevalent.currentOnlineUser.getPhone());

        ref.child("Security Question").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String ans1=dataSnapshot.child("answer1").getValue().toString();
                    String ans2=dataSnapshot.child("answer2").getValue().toString();

                    question1.setText(ans1);
                    question2.setText(ans2);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
