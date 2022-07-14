package com.example.admincollegeapp.notice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.admincollegeapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UploadNoticeActivity extends AppCompatActivity {

    CardView addImage;
    ImageView noticeImage;
    EditText noticeTitle;
    Button uploadNotice;

    Uri imageUri;
    String title;
    String key;

    private DatabaseReference reference;
    private StorageReference storageReference;
    ProgressDialog pd;

    NoticeData noticeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_notice);

        storageReference = FirebaseStorage.getInstance().getReference();
        reference =  FirebaseDatabase.getInstance().getReference().child("Notice");

        pd = new ProgressDialog(this);

        addImage = findViewById(R.id.addImage);
        noticeImage = findViewById(R.id.noticeImage);
        noticeTitle = findViewById(R.id.noticeTitle);
        uploadNotice = findViewById(R.id.uploadNotice);

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        uploadNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                title = noticeTitle.getText().toString();
                if(noticeTitle.getText().toString().isEmpty()) {
                    noticeTitle.setError("Empty");
                    noticeTitle.requestFocus();
                } else if (imageUri==null){
                    Toast.makeText(UploadNoticeActivity.this, "Upload an Image", Toast.LENGTH_SHORT).show();
                } else {
                    uploadImage();
                }
            }
        });

    }

    private void uploadImage() {

        pd = new ProgressDialog(this);
        pd.setTitle("Uploading");
        pd.show();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-hh:mm:ss");
        Date now = new Date();
        String fileName = formatter.format(now);

        storageReference = FirebaseStorage.getInstance().getReference().child("Notice").child(fileName);
        storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(UploadNoticeActivity.this,"Image Uploaded SuccessFully",Toast.LENGTH_SHORT).show();
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Date now = new Date();
                        SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy");
                        String date = sdfDate.format(now);
                        SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm:ss a");
                        String time = sdfTime.format(now);

                        key = reference.push().getKey();
                        NoticeData noticeData = new NoticeData(title,uri.toString(),date,time,key);
                        reference.child(key).setValue(noticeData).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(UploadNoticeActivity.this,"Data saved",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UploadNoticeActivity.this,"Data save failed",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                if(pd.isShowing()) pd.dismiss();
                noticeImage.setImageURI(null);
                imageUri=null;
                noticeTitle.setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadNoticeActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                if(pd.isShowing()) pd.dismiss();

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                pd.setMessage("Uploaded:"+(int)progress+"%");
            }
        });
    }


    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && data!=null && data.getData()!=null){
            imageUri = data.getData();
            noticeImage.setImageURI(imageUri);
        }
    }
}