package com.example.admincollegeapp.gallery;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admincollegeapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UploadImageActivity extends AppCompatActivity {

    Spinner dropdown;
    Button uploadImageBtn,addEventBtn;
    CardView addImage;
    ImageView uploadedImage;
    EditText eventName;
    TextView sampleTxt;

    String category;
    Uri imageUri;
    String fileName;
    ArrayList<String> events;
    ArrayAdapter<String> adapter;

    private DatabaseReference reference,mapRef;
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
        eventName = findViewById(R.id.eventName);
        sampleTxt = findViewById(R.id.sampleTxt);
        addEventBtn = findViewById(R.id.addEventBtn);
        mapRef = FirebaseDatabase.getInstance().getReference("Event");

        events = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,events);

        addEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = eventName.getText().toString();
                String key = mapRef.push().getKey();
                if(value.equals("")) {
                    Toast.makeText(UploadImageActivity.this, "Event name is Empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    mapRef.child(key).setValue(value);
                    eventName.setText("");
                    events.clear();
                    adapter.notifyDataSetChanged();

                    Toast.makeText(UploadImageActivity.this, "Event added", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dropdown.setAdapter(adapter);
        showData();

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

    private void showData() {
        mapRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    events.add(dataSnapshot.getValue().toString());
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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