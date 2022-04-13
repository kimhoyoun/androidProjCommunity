package com.example.andprojcommunity.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.andprojcommunity.R;
import com.example.andprojcommunity.adapter.CommentAdapter;
import com.example.andprojcommunity.adapter.FeedAdapter;
import com.example.andprojcommunity.model.CommentDTO;
import com.example.andprojcommunity.model.FeedDTO;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyPageFragment extends Fragment{
    Context context;

    TextView profileText;
    Button btnProfileFeed;
    Button btnProfileComment;
    TextView profileTabName;

    ArrayList<CommentDTO> commentList;

    ArrayList<FeedDTO> dtoList;

    RecyclerView recyclerView;
    FeedAdapter feedAdapter;
    CommentAdapter commentAdapter;

    FirebaseDatabase database;
    DatabaseReference databaseReference;

    public MyPageFragment(Context context){
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_page, container, false);

        profileText = (TextView) view.findViewById(R.id.profileIdText);
        btnProfileFeed = (Button) view.findViewById(R.id.btnProfileFeed);
        btnProfileComment = (Button) view.findViewById(R.id.btnProfileComment);
        profileTabName = (TextView) view.findViewById(R.id.profileTabName);

        database = FirebaseDatabase.getInstance("https://androidproj-ab6fe-default-rtdb.firebaseio.com/");
        databaseReference = database.getReference().child("Table");

        recyclerView = view.findViewById(R.id.mypage_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        dtoList = new ArrayList<>();
        commentList = new ArrayList<>();

        btnProfileFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileTabName.setText("내가 쓴 게시글");


                Query myQuery = databaseReference.child("Feeds").orderByChild("userID").equalTo("user1");

                myQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dtoList.clear();
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            FeedDTO feed = snapshot.getValue(FeedDTO.class);
                            dtoList.add(feed);
                        }
                        feedAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



                feedAdapter = new FeedAdapter(dtoList);
                recyclerView.setAdapter(feedAdapter);

            }
        });

        btnProfileComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileTabName.setText("내가 쓴 댓글");

                Query myQuery = databaseReference.child("Comments").orderByChild("comment_user").equalTo("user1");

                myQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        commentList.clear();
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            CommentDTO comment = snapshot.getValue(CommentDTO.class);
                            commentList.add(comment);
                        }
                        commentAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                commentAdapter = new CommentAdapter(commentList);
                recyclerView.setAdapter(commentAdapter);

            }
        });

        return view;
    }


    public ArrayList<FeedDTO> searchMyFeed(){

        ArrayList<FeedDTO> list = new ArrayList<>();


        // myFeed search
//        try{
//            if(sqlDB != null){
//                String query = getString(R.string.myselectAllQuery);
//                Cursor cursor = sqlDB.rawQuery(query,null);
//                int count = cursor.getCount();
//
//                for(int i =0; i<count; i++){
//                    cursor.moveToNext();
//                    int no = cursor.getInt(0);
//                    String userID = cursor.getString(1);
//                    String title = cursor.getString(2);
//                    String mainText = cursor.getString(3);
//                    int likeNum = cursor.getInt(4);
//                    int cName = cursor.getInt(5);
//                    String date = cursor.getString(6);
//
//                    CommunityItemDTO dto = new CommunityItemDTO(no, userID, title, mainText, likeNum, cName, date);
//
//                    list.add(dto);
//
//                }
//            } else{
//                android.util.Log.i("결과", "실패");
//            }
//        } catch(Exception e){
//            e.printStackTrace();
//        }

        return list;
    }


    public ArrayList<CommentDTO> setComment(){
        ArrayList<CommentDTO> list = new ArrayList<>();

        // comment search
//        sqlDB = myHelper.getReadableDatabase();
//
//        try{
//            if(sqlDB != null){
//                Cursor cursor = sqlDB.rawQuery("select * from comment where comment_user = 'user6'",null);
//                int count = cursor.getCount();
//
//                android.util.Log.i("결과", count+"");
//                for(int i =0; i<count; i++){
//                    cursor.moveToNext();
//                    int no = cursor.getInt(0);
//                    String comment_user = cursor.getString(1);
//                    String comment_text = cursor.getString(2);
//                    String feed_user = cursor.getString(3);
//                    int feed_no = cursor.getInt(4);
//                    String comment_time = cursor.getString(5);
//                    CommentDTO dto = new CommentDTO(no, comment_user, comment_text, feed_user, feed_no, comment_time);
//
//                    list.add(dto);
//
//                }
//            } else{
//                android.util.Log.i("결과", "실패");
//            }
//        } catch(Exception e){
//            e.printStackTrace();
//        }

        return list;
    }

}