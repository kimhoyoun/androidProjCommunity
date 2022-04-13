package com.example.andprojcommunity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.andprojcommunity.fragment.FeedFragment;
import com.example.andprojcommunity.fragment.MyPageFragment;
import com.example.andprojcommunity.fragment.SearchFragment;
import com.example.andprojcommunity.model.FeedDTO;
import com.google.android.material.tabs.TabLayout;

public class CommunityActivity extends AppCompatActivity{

    Fragment fragment0, fragment1, fragment2;
    TabLayout tabs;

    Fragment selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community);

        fragment0 = new FeedFragment(CommunityActivity.this);
        selected = fragment0;
        getSupportFragmentManager().beginTransaction().add(R.id.frame, fragment0).commit();

        tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setTabGravity(tabs.GRAVITY_FILL);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();


                if(position == 0){

                    fragment0 = new FeedFragment(CommunityActivity.this);

                    selected = fragment0;
                }else if(position == 1){

                        fragment1 = new SearchFragment(CommunityActivity.this);

                    selected = fragment1;
                }else if(position == 2){

                        fragment2 = new MyPageFragment(CommunityActivity.this);

                    selected = fragment2;
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

    @Override
    protected void onResume() {
        super.onResume();
        if(selected == fragment0){

            fragment0 = new FeedFragment(CommunityActivity.this);

            selected = fragment0;
        }
//        else if(selected == fragment1){
//
//            fragment1 = new SearchFragment(CommunityActivity.this);
//
//            selected = fragment1;
//        }
//        else if(selected == fragment2){
//
//            fragment2 = new MyPageFragment(CommunityActivity.this);
//
//            selected = fragment2;
//        }

        getSupportFragmentManager().beginTransaction().replace(R.id.frame, selected).commit();

    }

    public void detailPage(FeedDTO dto){
        FeedDTO tempDTO = new FeedDTO(dto.getNo(), dto.getUserID(), dto.getTitle(), dto.getMainText(),  dto.getcName(), dto.getDate());

//        Intent intent = new Intent(CommunityActivity.this, DetailActivity.class);
//        intent.putExtra("dto",tempDTO);
//        startActivity(intent);

    }
}


