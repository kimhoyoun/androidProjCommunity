package com.example.andprojcommunity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.andprojcommunity.fragment.FeedFragment;
import com.example.andprojcommunity.fragment.MyPageFragment;
import com.example.andprojcommunity.fragment.SearchFragment;
import com.example.andprojcommunity.model.FeedDTO;
import com.example.andprojcommunity.model.UserAccount;
import com.google.android.material.tabs.TabLayout;

public class CommunityActivity extends AppCompatActivity{

    Fragment fragment0, fragment1, fragment2;
    TabLayout tabs;

    Fragment selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community);

        Toolbar toolbar = findViewById(R.id.communityToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Community Page - Feed List");

        fragment0 = new FeedFragment(CommunityActivity.this);
        fragment1 = new SearchFragment(CommunityActivity.this);
        fragment2 = new MyPageFragment(CommunityActivity.this);

        selected = fragment0;
        getSupportFragmentManager().beginTransaction().add(R.id.frame, fragment0).commit();

        tabs = findViewById(R.id.tabs);
        tabs.setTabGravity(tabs.GRAVITY_FILL);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();


                if(position == 0){
                    selected = fragment0;
                    getSupportActionBar().setTitle("Community - Feed List Page");
                }else if(position == 1){
                    selected = fragment1;
                    getSupportActionBar().setTitle("Community - Search Page");
                }else if(position == 2){
                    selected = fragment2;
                    getSupportActionBar().setTitle("Community - My Profile Page");
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.frame, selected).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}


