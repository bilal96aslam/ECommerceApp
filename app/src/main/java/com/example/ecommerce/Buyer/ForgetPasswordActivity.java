package com.example.ecommerce.Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Prevalent.Prevalent;
import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ForgetPasswordActivity extends AppCompatActivity {


    private EditText numberEditText, q1eEditText, q2EditText;
    private TextView pageTitle,titleQues;
    private Button verify;

    private String check="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        check= getIntent().getStringExtra("check");


        numberEditText = (EditText) findViewById(R.id.find_phone_number);
        q1eEditText = (EditText) findViewById(R.id.question_1);
        q2EditText = (EditText) findViewById(R.id.question_2);

        pageTitle = (TextView) findViewById(R.id.title_forget);
        titleQues = (TextView) findViewById(R.id.title_questions);

        verify=(Button) findViewById(R.id.verify_btn);



        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setAnswers();

            }
        });


    }

    private void setAnswers() {

        String q1=q1eEditText.getText().toString().toLowerCase();
        String q2=q2EditText.getText().toString().toLowerCase();


        if(q1.isEmpty() && q1.isEmpty() ){
            Toast.makeText(getApplicationContext(), "please answer both question", Toast.LENGTH_SHORT).show();
        }
        else{
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(Prevalent.currentOnlineUser.getPhone());

            HashMap<String,Object> map=new HashMap<>();
            map.put("answer1", q1);
            map.put("answer2", q2);
            ref.child("Security Questions").updateChildren(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "You have set security questions successfully!", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
                                startActivity(intent);}
                        }
                    });

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        numberEditText.setVisibility(View.GONE);


        if(check.equals("settings")){

            pageTitle.setText("Set Questions");
            titleQues.setText("Please set Answers the Following Security Questions?");
            verify.setText("Set");
            DisplayPreviousAnswer();
        }
        else if(check.equals("login")){

            numberEditText.setVisibility(View.VISIBLE);

            verify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                 verifyUser();

                }
            });
        }
    }

    private void verifyUser() {

        final String no=numberEditText.getText().toString();
        final String q1=q1eEditText.getText().toString().toLowerCase();
        final String q2=q2EditText.getText().toString().toLowerCase();

        if(!no.equals("") && !q1.equals("") && !q2.equals("")){
            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(no);

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.exists()){

                        if(snapshot.hasChild("Security Questions")){

                            String ans1=snapshot.child("Security Questions").child("answer1").getValue().toString();
                            String ans2=snapshot.child("Security Questions").child("answer2").getValue().toString();
                            if(!ans1.equals(q1)){
                                Toast.makeText(getApplicationContext(), "your 1st answer is wrong.", Toast.LENGTH_SHORT).show();
                            }
                            else if(!ans2.equals(q2)){
                                Toast.makeText(getApplicationContext(), "your 2nd answer is wrong.", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                AlertDialog.Builder builder=new AlertDialog.Builder(ForgetPasswordActivity.this);
                                builder.setTitle("Write Password Here:");
                                final EditText newPasswd=new EditText(ForgetPasswordActivity.this);
                                builder.setView(newPasswd);

                                builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(!newPasswd.getText().toString().equals("")){
                                            ref.child("password").setValue(newPasswd.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Intent intent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
                                                            startActivity(intent);
                                                            Toast.makeText(getApplicationContext(), "Password changed successfully!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                        }
                                        else {
                                            Toast.makeText(getApplicationContext(), "Write password please", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                                builder.show();
                            }


                        }
                        else{
                            Toast.makeText(getApplicationContext(), "You have not set the security questions!", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else{
                        Toast.makeText(getApplicationContext(), "This phone number not exist!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else {
            Toast.makeText(getApplicationContext(), "please complete the form", Toast.LENGTH_SHORT).show();
        }


    }

    private void DisplayPreviousAnswer(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(Prevalent.currentOnlineUser.getPhone());
        ref.child("Security Questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    String ans1=snapshot.child("answer1").getValue().toString();
                    String ans2=snapshot.child("answer2").getValue().toString();

                    q1eEditText.setText(ans1);
                    q2EditText.setText(ans2);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}