package com.example.andprojcommunity.model;

import android.content.Context;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.andprojcommunity.R;

public class CommentItemLayout extends LinearLayout {

    CommentDTO dto;
    FeedDTO cdto;

    ImageView profile;
    TextView comment_userID;
    LinearLayout profileLayout;

    TextView comment_text;

    LinearLayout btnLayout;
    Button btnCommentUpdate;
    Button btnCommentDelete;

    LayoutParams profileLayoutParams = new LayoutParams(
            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

    LayoutParams btnLayoutParams = new LayoutParams(
            LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);


    public CommentItemLayout(Context context, CommentDTO dto, FeedDTO cdto ){
        super(context);
        this.dto = dto;
        this.cdto = cdto;

        profile = new ImageView(context);
        comment_userID = new TextView(context);
        profileLayout = new LinearLayout(context);
        comment_text = new TextView(context);
        btnCommentUpdate = new Button(context);
        btnCommentDelete = new Button(context);
        btnLayout = new LinearLayout(context);


        setLayout();
    }


    public void setLayout(){
        profile.setImageResource(R.drawable.profile);
        comment_userID.setText(dto.getComment_user());

        comment_text.setText(dto.getComment_text());

        this.setOrientation(LinearLayout.VERTICAL);
        profileLayout.addView(profile,new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        profileLayout.addView(comment_userID, new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        profileLayout.setGravity(Gravity.CENTER);

        this.addView(profileLayout, new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        this.addView(comment_text, new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        comment_text.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        android.util.Log.i("결과", "feed_user : "+ dto.getComment_user());
        android.util.Log.i("결과", "userID : "+cdto.getUserID());


        // 로그인한 userid와 댓글을 작성한 userID가 일치하면 수정&삭제 버튼 보이기
        // cdto.getUserID -> 로그인 userID로 수정 예정
       if(dto.getComment_user().equals(cdto.getUserID())){
           btnCommentUpdate.setText("수정");
           btnCommentDelete.setText("삭제");
           btnLayout.addView(btnCommentUpdate, new LayoutParams(
                   LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
           btnLayout.addView(btnCommentDelete, new LayoutParams(
                   LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

           btnLayout.setGravity(Gravity.RIGHT);

           this.addView(btnLayout, new LayoutParams(
                   LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
       }

    }

    public CommentDTO getDto(){
        return dto;
    }
}
