package com.example.ecommerce.Seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SellerLoginActivity extends AppCompatActivity {

    private Button sellerLogin;
    private EditText InputEmail,InputPassword;
    private ProgressDialog loadingBar;
    private TextView signUp;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);

        sellerLogin = (Button) findViewById(R.id.seller_login_btn);
        InputEmail = (EditText) findViewById(R.id.seller_login_email);
        InputPassword = (EditText) findViewById(R.id.seller_login_password);
        signUp = (TextView) findViewById(R.id.already_acc_link);
        loadingBar = new ProgressDialog(this);

        firebaseAuth=FirebaseAuth.getInstance();


        sellerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             LoginSeller();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SellerRegistrationActivity.class);
                startActivity(intent);
            }
        });
    }

    private void LoginSeller() {



        final String password = InputPassword.getText().toString();
        final String email = InputEmail.getText().toString();

        if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please write your email...", Toast.LENGTH_SHORT).show();
        }
        else{
            loadingBar.setTitle("Seller Account Login");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
               if(task.isSuccessful()){
                   loadingBar.dismiss();
                   Intent intent = new Intent(getApplicationContext(), SellerHomeActivity.class);
                   intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                   startActivity(intent);
                   finish();
               }
                }
            });


        }
    }
}