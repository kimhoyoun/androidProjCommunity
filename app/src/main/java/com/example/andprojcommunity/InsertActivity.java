package com.example.andprojcommunity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.example.andprojcommunity.model.CommentDTO;
import com.example.andprojcommunity.model.FeedDTO;
import com.example.andprojcommunity.model.UserAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class InsertActivity extends AppCompatActivity {
    private final int GALLERY_CODE = 10;
    private final int FROM_CAMERA = 0;
    private final int FROM_ALBUM = 2;

    final private static String Tag = "태그명";
    final static int TAKE_PICTURE = 1;

    String getmCurrentPhotoPath;
    final static int REQUEST_TAKE_PHOTO = 1;


    int flag;

    String mCurrentPhotoPath;
    Uri imgUri, photoURI, albumURI;

    FirebaseStorage storage = FirebaseStorage.getInstance();

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


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                System.out.println("권한 설정 완료");
            }
            else{
                System.out.println("권한 설정 요청");
                ActivityCompat.requestPermissions(InsertActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},1 );
            }
        }




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
                            flag = 0;
                            takePhoto();

                        }else if(item.getItemId() == R.id.menu_Album){
                            flag = 1;
                            selectAlbum();
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
                               final String cu = "testUserID";

                               String filename = cu + "_"+System.currentTimeMillis();
                               StorageReference storageRef = storage.getReferenceFromUrl("gs://androidproj-ab6fe.appspot.com").child("photo/" + filename);

                               UploadTask uploadTask;

                               Uri file = null;
                               if(flag == 0){
                                   file = Uri.fromFile(new File(mCurrentPhotoPath));
                               }else if(flag == 1){
                                   file = photoURI;
                               }

                               uploadTask = storageRef.putFile(file);
//                               final ProcessDialog processDialog = new

                               uploadTask.addOnFailureListener(new OnFailureListener() {
                                   @Override
                                   public void onFailure(@NonNull Exception e) {
                                       System.out.println("업로드 실패");
                                       e.printStackTrace();
                                   }
                               }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                   @Override
                                   public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                       System.out.println("업로드 성공");
                                   }
                               });


                               long seq = (Long)task.getResult().getValue();

                               RadioButton rb = (RadioButton)findViewById(radioGroup.getCheckedRadioButtonId());
                               int feedType = ((rb.getText().toString()).equals("Exercise") ? 2 : 1);

                               UserAccount user = MainActivity.getUserInstance();

                               FeedDTO feed = new FeedDTO((int)seq, user.getEmailId(), user.getName(), newTitle.getText().toString(), newMainText.getText().toString()
                                       , feedType, currentTime(), filename);

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

    // 권한 요청


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        System.out.println("onRequestPermissionResult");
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
            System.out.println("Permission : "+ permissions[0] + "was "+ grantResults[0]);
        }
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
                    Uri providerURI = FileProvider.getUriForFile(this, "com.example.andprojcommunity.fileprovider",photoFile);
                    imgUri = providerURI;
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, providerURI);
                    startActivityForResult(intent, REQUEST_TAKE_PHOTO);
                }
            }
        }else{
            System.out.println("저장공간 접근 불가능");

        }
    }



    // 이미지 생성
    public File createImageFile() throws IOException{
        String imgFileName = System.currentTimeMillis()+"";
        File imageFile = null;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if(!storageDir.exists()){
            // 스토리지 없으면 만듦
            System.out.println("storage 존재하지 않음 : "+storageDir.toString());
            storageDir.mkdirs();
        }

        System.out.println("storage 존재함 : "+ storageDir.toString());
        imageFile = File.createTempFile(imgFileName, ".jpg",storageDir);
        mCurrentPhotoPath = imageFile.getAbsolutePath();

        return imageFile;

    }

    // 찍은 사진 갤러리 저장
    public void galleryAddPic(){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
        Toast.makeText(InsertActivity.this, "사진 저장", Toast.LENGTH_SHORT).show();
    }


    // 앨범선택 클릭
    public void selectAlbum(){
        //앨범에서 이미지 가져옴

        //앨범 열기
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);

        intent.setType("image/*");

        startActivityForResult(intent, FROM_ALBUM);
    }

    // 선택 후 처리 (FROM_ALBUM, FROM_CAMERA)


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK){
            return;
        }


        switch (requestCode){

            case FROM_ALBUM:{
                // 앨범 가져오기
                if(data.getData()!= null){
                    try{
                        photoURI = data.getData();
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),photoURI);
                        img1.setImageBitmap(bitmap);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                break;
            }

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

            case REQUEST_TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    galleryAddPic();
                    File file = new File(mCurrentPhotoPath);
                    Bitmap bitmap;
                    if(Build.VERSION.SDK_INT >= 29){
                        ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), Uri.fromFile(file));
                        try {
                            bitmap = ImageDecoder.decodeBitmap(source);
                            if(bitmap != null){
                                img1.setImageBitmap(bitmap);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(file));
                            if (bitmap != null) {
                                img1.setImageBitmap(bitmap);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
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
