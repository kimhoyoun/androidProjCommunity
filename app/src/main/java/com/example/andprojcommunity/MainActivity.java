package com.example.andprojcommunity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://androidproj-ab6fe-default-rtdb.firebaseio.com/");
    private DatabaseReference databaseReference = database.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnEdit = (Button) findViewById(R.id.btnEdit);
        Button btnExercise = (Button) findViewById(R.id.btnExercise);
        Button btnFood = (Button) findViewById(R.id.btnFood);
        Button btnCommunity = (Button) findViewById(R.id.btnCommunity);

        btnCommunity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CommunityActivity.class);
                startActivity(intent);
            }
        });

        databaseReference.child("user").setValue("user3");
    }
}