package com.iconlab.turkgram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class feed_activity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menuforupload,menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.upload){
            Intent intent = new Intent(feed_activity.this,Uploadactivity.class);
            startActivity(intent);

        }else if(item.getItemId() == R.id.Cikis){
            firebaseAuth.signOut();
            Intent intent = new Intent(feed_activity.this,Signupactivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_activity);
        Toast.makeText(this, "Hos geldiniz", Toast.LENGTH_SHORT).show();
        firebaseAuth = FirebaseAuth.getInstance();


    }
    public void uploadbutton(View view){
        Intent intent = new Intent(feed_activity.this,Uploadactivity.class);
        startActivity(intent);
    }

}
