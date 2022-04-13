package com.example.andprojcommunity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.andprojcommunity.adapter.CommentAdapter;
import com.example.andprojcommunity.model.CommentDTO;
import com.example.andprojcommunity.model.FeedDTO;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    FeedDTO dto;

    InputMethodManager imm;

    Button btnNewComment;
    EditText newCommnetText;

    ArrayList<CommentDTO> commentList;

    RecyclerView detailCommentRecyclerView;
    CommentAdapter commentAdapter;

    FirebaseDatabase database ;
    DatabaseReference databaseReference ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailpage);


        imm = (InputMethodManager) this.getSystemService(INPUT_METHOD_SERVICE);
        Intent intent = getIntent();
        dto = (FeedDTO) intent.getSerializableExtra("dto");

        btnNewComment = (Button)findViewById(R.id.btnNewComment);
        newCommnetText = (EditText)findViewById(R.id.newCommnetText);

        detailCommentRecyclerView = findViewById(R.id.detailCommentRecyclerView);
        detailCommentRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        database = FirebaseDatabase.getInstance("https://androidproj-ab6fe-default-rtdb.firebaseio.com/");
        databaseReference = database.getReference().child("Table");

        commentList = new ArrayList<>();

        Toolbar toolbar = findViewById(R.id.detailToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Detail Page");

        TextView title = (TextView) findViewById(R.id.detalTitle);
        TextView mainText = (TextView) findViewById(R.id.detailMainText);
        TextView dateText = (TextView) findViewById(R.id.detailDateText);

        title.setText(dto.getTitle());
        mainText.setText(dto.getMainText());
        dateText.setText(dto.getDate().substring(0,10));


        Query myQuery = databaseReference.orderByChild("feed_no").equalTo(dto.getNo()+"");

        DatabaseReference commentRef = databaseReference.child("Table").child("Comments");

        myQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    CommentDTO comment = snapshot.getValue(CommentDTO.class);

                }
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("feed List Read Error : "+String.valueOf(error.toException()));
            }
        });

        commentAdapter = new CommentAdapter(commentList);
        detailCommentRecyclerView.setAdapter(commentAdapter);


        btnNewComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!newCommnetText.getText().toString().equals("")){
                    final int sequence;


                    System.out.println(databaseReference.child("Table").child("Comments").child("Sequence").get());
                    databaseReference.child("Table").child("Comments").child("Sequence").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            long seq = (Long)snapshot.getValue();
                            System.out.println(seq);
                            CommentDTO comment = new CommentDTO((int)seq, "user3",newCommnetText.getText().toString(), dto.getUserID(), dto.getNo(), "2022-04-03 16:26:10");

                            databaseReference.child("Table").child("Comments").child(seq+"").setValue(comment);
                            databaseReference.child("Table").child("Comments").child("Sequence").setValue(seq +1);

                            imm.hideSoftInputFromWindow(newCommnetText.getWindowToken(), 0);
                            newCommnetText.setText("");

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



//                    DatabaseReference ref = databaseReference.child("Table").child("Comments").child("4");
//                    CommentDTO comment = new CommentDTO(3, "user3",newCommnetText.getText().toString(), dto.getUserID(), dto.getNo(), "2022-04-03 16:26:10");
//                    ref.setValue(comment);
//
//                    // 키보드 내리기
//                    imm.hideSoftInputFromWindow(newCommnetText.getWindowToken(), 0);
//                    newCommnetText.setText("");

                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                    builder.setTitle("오류");
                    builder.setMessage("댓글을 입력해 주세요!");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    builder.show();
                }

            }
            });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detailmenu, menu);

       return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.detail_menuUpdate:
//                update(dto);
                finish();
                return true;
            case R.id.detail_menuDelete:

                AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                builder.setTitle("삭제");
                builder.setMessage("삭제하시겠습니까?");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean result;
//                        result = delete(dto);
                        Toast.makeText(DetailActivity.this, "삭제됨",Toast.LENGTH_SHORT).show();

//                        if(result){
//                            finish();
//                        }else{
//
//                        }

                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(DetailActivity.this, "취소되었습니다.",Toast.LENGTH_SHORT).show();
                    }
                });

                builder.create().show();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }


//    public void update(CommunityItemDTO item){
//        Intent intent = new Intent(DetailActivity.this, InsertActivity.class);
//        intent.putExtra("dto",item);
//        startActivity(intent);
//    }
//
//    public boolean delete(CommunityItemDTO item){
//        int no = item.getNo();
//
//        String query = "delete from communityItem where no = "+no;
//
//        sqlDB = myHelper.getWritableDatabase();
//        sqlDB.execSQL(query);
//
//        query = "delete from comment where feed_no = "+no;
//        sqlDB.execSQL(query);
//
//        sqlDB.close();
//
//        return true;
//    }

    public void setComment(){



    }
}
