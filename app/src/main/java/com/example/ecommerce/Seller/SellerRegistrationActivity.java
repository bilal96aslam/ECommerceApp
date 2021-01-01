package com.example.ecommerce.Seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Buyer.LoginActivity;
import com.example.ecommerce.Buyer.MainActivity;
import com.example.ecommerce.Buyer.RegisterActivity;
import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SellerRegistrationActivity extends AppCompatActivity {

    private Button sellerReg;
    private EditText InputName, InputPhoneNumber, InputPassword,InputEmail,InputAddress;
    private ProgressDialog loadingBar;
    private TextView alreadyAccount;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registration);

        sellerReg = (Button) findViewById(R.id.seller_reg_btn);
        alreadyAccount=(TextView) findViewById(R.id.already_acc_link);

        InputName = (EditText) findViewById(R.id.seller_name);
        InputPhoneNumber = (EditText) findViewById(R.id.seller_number);
        InputPassword = (EditText) findViewById(R.id.seller_password);
        InputEmail = (EditText) findViewById(R.id.seller_email);
        InputAddress = (EditText) findViewById(R.id.seller_address);
        loadingBar = new ProgressDialog(this);

        firebaseAuth=FirebaseAuth.getInstance();

        alreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerRegistrationActivity.this, SellerLoginActivity.class);
                startActivity(intent);
            }
        });
        sellerReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               registerSeller();
            }
        });


    }

    private void registerSeller() {

        final String name = InputName.getText().toString();
        final String phone = InputPhoneNumber.getText().toString();
        final String password = InputPassword.getText().toString();
        final String email = InputEmail.getText().toString();
        final String address = InputAddress.getText().toString();

        if (TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "Please write your name...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Please write your phone number...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password) && password.length()< 6)
        {
            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please write your email...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(address))
        {
            Toast.makeText(this, "Please write your address...", Toast.LENGTH_SHORT).show();
        }

        else
        {
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            firebaseAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(SellerRegistrationActivity.this,new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){
                        final DatabaseReference rootRef;
                        rootRef= FirebaseDatabase.getInstance().getReference().child("Sellers");

                        String uId=firebaseAuth.getCurrentUser().getUid();

                        HashMap<String,Object> userDataMap=new HashMap<>();
                        userDataMap.put("sid", uId);
                        userDataMap.put("name", name);
                        userDataMap.put("phone", phone);
                        userDataMap.put("email", email);
                        userDataMap.put("password", password);
                        userDataMap.put("address", address);

                        rootRef.child(uId).updateChildren(userDataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                loadingBar.dismiss();
                                Toast.makeText(getApplicationContext(), "Congratulations, your account has been created.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), SellerHomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        });

                    }else
                        Log.e("Error", String.valueOf(task.getException()));
                }
            });
        }
    }
}