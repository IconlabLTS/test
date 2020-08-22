package com.iconlab.turkgram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Signupactivity extends AppCompatActivity {

    EditText editText;
    EditText password;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        editText = findViewById(R.id.email);
        password = findViewById(R.id.password);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            Intent intent2 = new Intent(Signupactivity.this, feed_activity.class);
            startActivity(intent2);
            finish();
        }


    }

    public void giris(View view) {
        String giris = editText.getText().toString();
        String password1 = password.getText().toString();
        if (giris.length() > 0){
            if(password1.length() > 0){

        firebaseAuth.signInWithEmailAndPassword(giris, password1).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                Intent intent = new Intent(Signupactivity.this, feed_activity.class);
                startActivity(intent);
                finish();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Signupactivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        }else{
            Toast.makeText(this, "Lütfen Sifre ve Mail adres girin", Toast.LENGTH_SHORT).show();
        }}


    }

    public void kayit(View view) {
        String giris = editText.getText().toString();
        String password1 = password.getText().toString();
        if (giris.length() > 0) {
            if (password1.length() > 0) {

                firebaseAuth.createUserWithEmailAndPassword(giris, password1).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(Signupactivity.this, "Kayit oldunuz", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Signupactivity.this, feed_activity.class);
                        startActivity(intent);
                        finish();


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Signupactivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                    }
                });


            }else{
                Toast.makeText(this, "Lütfen Sifre ve Mail adres girin", Toast.LENGTH_SHORT).show();
            }

        }
    }
}