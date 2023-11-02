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

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPass, etConPass;
    Button btnCreateAcc;
    TextView txtAlreadyAcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPasss);
        etConPass = findViewById(R.id.etConPass);
        btnCreateAcc = findViewById(R.id.btnCreateAcc);
        txtAlreadyAcc = findViewById(R.id.txtAlreadyAcc);

        btnCreateAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });

        txtAlreadyAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void createAccount() {
        String email = etEmail.getText().toString();
        String pass = etPass.getText().toString();
        String conPass = etConPass.getText().toString();

        boolean inValidates = validateData(email, pass, conPass);
        if (!inValidates){
            return;
        }

        createAccountFirebase(email, pass);
    }

    private void createAccountFirebase(String email, String pass) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(LoginActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Account create successful,Check email", Toast.LENGTH_SHORT).show();
                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            firebaseAuth.signOut();
                            finish();
                        }else {
                            Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    boolean validateData(String email, String password, String conPass){

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Email is invalid");
            return false;
        }
        if (password.length()<6){
            etPass.setError("Password length is invalid");
            return false;
        }
        if (!password.equals(conPass)){
            etConPass.setError("Password not match");
            return false;
        }
        return true;
    }
}