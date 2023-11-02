package com.noteappusingjava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    EditText emailEditText, passEditText;
    Button btnLogin;
    TextView textNoAcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailEditText = findViewById(R.id.etEmailLog);
        passEditText = findViewById(R.id.etPasssLog);
        btnLogin = findViewById(R.id.btnLog);
        textNoAcc = findViewById(R.id.txtNoAcc);

        textNoAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    private void loginUser() {
        String email = emailEditText.getText().toString();
        String pass = passEditText.getText().toString();

        boolean inValidates = validateData(email, pass);
        if (!inValidates){
            return;
        }

        loginAccountFirebase(email, pass);
    }

    void loginAccountFirebase(String email, String pass){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    if (firebaseAuth.getCurrentUser().isEmailVerified()){
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        finish();
                    }
                }else {
                    Toast.makeText(RegisterActivity.this, "Email not verify, please verity email", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    boolean validateData(String email, String password){

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Email is invalid");
            return false;
        }
        if (password.length()<6){
            passEditText.setError("Password length is invalid");
            return false;
        }
        return true;
    }
}