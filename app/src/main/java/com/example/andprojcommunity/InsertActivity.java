package com.example.andprojcommunity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.example.andprojcommunity.model.CommentDTO;
import com.example.andprojcommunity.model.FeedDTO;
import com.example.andprojcommunity.model.UserAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.gun0912.tedpermission.PermissionListener;
//import com.gun0912.tedpermission.normal.TedPermission;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class InsertActivity extends AppCompatActivity {
    private final int GALLERY_CODE = 10;
    private final int FROM_CAMERA = 10;

    String mCurrentPhotoPath;
    Uri imgUri;

    ImageView img1;

    FeedDTO dto;

    EditText newTitle;
    EditText newMainText;
    ImageButton btnImgAdd;

    FirebaseDatabase database;
    DatabaseReference databaseReference;

    RadioGroup radioGroup;

    GridView gridView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);


        // TedPermission 라이브러리 -> 카메라 권한 획득
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(InsertActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(InsertActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

//        new TedPermission()




        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("DB");

        newTitle = findViewById(R.id.newTitle);
        newMainText = findViewById(R.id.newMainText);
        btnImgAdd = findViewById(R.id.btnImgAdd);
        radioGroup = findViewById(R.id.radioGroup);
        gridView = findViewById(R.id.photoGridView);
        img1 = findViewById(R.id.testImg);

        Toolbar toolbar = findViewById(R.id.insertToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if(intent.getSerializableExtra("dto")!=null){
            dto = (FeedDTO) intent.getSerializableExtra("dto");
            System.out.println(dto);
            getSupportActionBar().setTitle("Update Page");
            newTitle.setText(dto.getTitle());
            newMainText.setText(dto.getMainText());
        }else{
            getSupportActionBar().setTitle("Insert Page");
        }




        btnImgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupMenu popupMenu = new PopupMenu(InsertActivity.this, view);
                getMenuInflater().inflate(R.menu.camera_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId() == R.id.menu_takePhoto){

                            takePhoto();

                        }else if(item.getItemId() == R.id.menu_Album){

                        }else{

                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.insertmenu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.btnInsertOK:{
                if(dto != null){
                    noticeMessage("수정");
                }else{
                    noticeMessage("입력");
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void noticeMessage(String type){
        AlertDialog.Builder builder = new AlertDialog.Builder(InsertActivity.this);
        builder.setTitle(type);
        builder.setMessage(type+"하시겠습니까?");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

               if(type.equals("수정")){

                   dto.setTitle(newTitle.getText().toString());
                   dto.setMainText(newMainText.getText().toString());

                   databaseReference.child("Feeds").child(dto.getNo()+"").setValue(dto).addOnSuccessListener(new OnSuccessListener<Void>() {
                       @Override
                       public void onSuccess(Void unused) {
                           Toast.makeText(InsertActivity.this, "수정됨",Toast.LENGTH_SHORT).show();
                           Intent intent = new Intent(InsertActivity.this, DetailActivity.class);
                           intent.putExtra("dto", dto);
                           startActivity(intent);
                           finish();
                       }
                   });

               }else if(type.equals("입력")){
                   databaseReference.child("Feeds").child("Sequence").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                       @Override
                       public void onComplete(@NonNull Task<DataSnapshot> task) {
                           if(!task.isSuccessful()){
                               System.out.println("실패");
                           }else{
                               long seq = (Long)task.getResult().getValue();

                               RadioButton rb = (RadioButton)findViewById(radioGroup.getCheckedRadioButtonId());
                               int feedType = ((rb.getText().toString()).equals("Exercise") ? 2 : 1);

                               UserAccount user = MainActivity.getUserInstance();

                               FeedDTO feed = new FeedDTO((int)seq, user.getEmailId(), user.getName(), newTitle.getText().toString(), newMainText.getText().toString()
                                       , feedType, currentTime());

                               databaseReference.child("Feeds").child("Sequence").setValue(seq +1);
                               databaseReference.child("Feeds").child(seq+"").setValue(feed).addOnSuccessListener(new OnSuccessListener<Void>() {
                                   @Override
                                   public void onSuccess(Void unused) {
                                       Toast.makeText(InsertActivity.this, "입력됨",Toast.LENGTH_SHORT).show();
                                       finish();
                                   }
                               });

                           }
                       }
                   });
               }
            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(InsertActivity.this, "취소되었습니다.",Toast.LENGTH_SHORT).show();
            }
        });

        builder.create().show();
    }

    public String currentTime(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String getTime = sdf.format(date);
        return getTime;
    }


    public void takePhoto(){
        // 사진촬영
        System.out.println("사진 촬영");

        String state = Environment.getExternalStorageState();

        if(Environment.MEDIA_MOUNTED.equals(state)){
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(intent.resolveActivity(getPackageManager())!=null){
                File photoFile = null;

                try {
                    // 이미지 생성 함수
                    photoFile = createImageFile();
                }catch (IOException e){
                    e.printStackTrace();
                }

                if(photoFile != null){
                    Uri providerURI = FileProvider.getUriForFile(this, getPackageName(),photoFile);
                    imgUri = providerURI;
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, providerURI);
                    startActivityForResult(intent, FROM_CAMERA);
                }
            }
        }else{
            System.out.println("저장공간 접근 불가능");

        }
    }



    // 이미지 생성
    public File createImageFile() throws IOException{
        String imgFileName = System.currentTimeMillis()+".jpg";
        File imageFile = null;
        File storageDir = new File(Environment.getExternalStorageDirectory()+"/Pictures","ireh");

        if(!storageDir.exists()){
            // 스토리지 없으면 만듦
            System.out.println("storage 존재하지 않음 : "+storageDir.toString());
            storageDir.mkdirs();
        }

        System.out.println("storage 존재함 : "+ storageDir.toString());
        imageFile = new File(storageDir,imgFileName);
        mCurrentPhotoPath = imageFile.getAbsolutePath();

        return imageFile;

    }

    // 찍은 사진 갤러리 저장
    public void galleryAddPic(){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        sendBroadcast(mediaScanIntent);
        Toast.makeText(this, "사진 저장", Toast.LENGTH_SHORT).show();
    }


    // 앨범선택 클릭
    public void selectAlbum(){
        //앨범에서 이미지 가져옴

        //앨범 열기
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);

        intent.setType("image/*");

//        startActivityForResult(intent, FROM_ALBUM);
    }

    // 선택 후 처리 (FROM_ALBUM, FROM_CAMERA)


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK){
            return;
        }

        switch (requestCode){

//            case FROM_ALBUM:{
//                // 앨범 가져오기
//                if(data.getData()!= null){
//                    try{
//                        photoURI = data.getData();
//                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),photoURI);
//                        img1.setImageBitmap(bitmap);
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//                }
//                break;
//            }

            case FROM_CAMERA:{
               // 카메라 촬영
                try{
                    System.out.println("FROM_CAMERA");
                    galleryAddPic();
                    img1.setImageURI(imgUri);
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    public class PhothGridView extends BaseAdapter{
        Context context;

        public PhothGridView(Context context){
            context = context;
        }


        @Override
        public int getCount() {

            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }
}
