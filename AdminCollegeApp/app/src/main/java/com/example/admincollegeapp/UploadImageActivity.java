package com.example.admincollegeapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

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
import java.util.HashMap;

public class UploadImageActivity extends AppCompatActivity {

    Spinner dropdown;
    Button uploadImageBtn;
    CardView addImage;
    ImageView uploadedImage;

    String category;
    Uri imageUri;
    String fileName;

    private DatabaseReference reference;
    private StorageReference storageReference;
    ProgressDialog pd;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);

        dropdown = findViewById(R.id.dropdown);
        uploadedImage = findViewById(R.id.uploadedImage);
        addImage = findViewById(R.id.addImage);
        uploadImageBtn = findViewById(R.id.uploadImageBtn);

        String items[] = new String[]{"Select a Category","IGNITRON","PONGAL","ONAM"};
        dropdown.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,items));

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = dropdown.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { /* Nothing Done */ }
        });

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        uploadImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageUri== null){
                    Toast.makeText(UploadImageActivity.this,"Please select a Image",Toast.LENGTH_SHORT).show();
                }
                else if(category.equals("Select a Category")){
                    Toast.makeText(UploadImageActivity.this,"Please select a Category",Toast.LENGTH_SHORT).show();
                }
                else{
                    uploadImage();
                }
            }
        });
    }

    private void uploadImage() {

        pd = new ProgressDialog(this);
        pd.setTitle("Uploading");
        pd.show();

        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd-hhmmss");
        Date now = new Date();
        fileName = formater.format(now);
        storageReference = FirebaseStorage.getInstance().getReference("Gallery/"+category+"/").child(fileName);

        storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        reference = FirebaseDatabase.getInstance().getReference().child("Gallery").child(category);

                        Date now = new Date();
                        SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy");
                        String date = sdfDate.format(now);
                        SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm:ss a");
                        String time = sdfTime.format(now);
                        key = reference.push().getKey();
                        GalleryData galleryData = new GalleryData(fileName,date,time,uri.toString(),category,key);

                        reference.child(key).setValue(galleryData).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(UploadImageActivity.this,"Data Saved",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UploadImageActivity.this,"Data save failed",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                uploadedImage.setImageURI(null);
                imageUri=null;
                Toast.makeText(UploadImageActivity.this,"Image Uploaded SuccessFully",Toast.LENGTH_SHORT).show();
                if(pd.isShowing()) pd.dismiss();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadImageActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
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
            uploadedImage.setImageURI(imageUri);
        }
    }
}