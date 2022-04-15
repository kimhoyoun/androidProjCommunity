package com.example.andprojcommunity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.andprojcommunity.model.FeedDTO;
import com.example.andprojcommunity.model.UserAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    public static UserAccount userInfo;

    FirebaseDatabase database;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnEdit = (Button) findViewById(R.id.btnEdit);
        Button btnExercise = (Button) findViewById(R.id.btnExercise);
        Button btnFood = (Button) findViewById(R.id.btnFood);
        Button btnCommunity = (Button) findViewById(R.id.btnCommunity);


        database = FirebaseDatabase.getInstance();
        ref = database.getReference().child("DB");

        // key값으로 수정예정
        // idToken이 아닌 email이 들어갈듯
        ref.child("UserAccount").child("4Ugwi9DyqvWIT62prxEMrw8sJVS2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userInfo = dataSnapshot.getValue(UserAccount.class);

                System.out.println(userInfo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        btnCommunity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CommunityActivity.class);
                startActivity(intent);
            }
        });

    }

    public static UserAccount getUserInstance(){
        return userInfo;
    }

}