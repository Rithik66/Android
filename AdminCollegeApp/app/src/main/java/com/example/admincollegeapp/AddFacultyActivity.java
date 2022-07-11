package com.example.admincollegeapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;


public class AddFacultyActivity extends AppCompatActivity {

    Spinner dropdown;
    TextView facultyName,facultyEmail,facultyPost,facultySpecial;
    CircleImageView profilePic;
    String selectedDepartment,name,email,post,specialization;
    Button submit;
    ImageView removeImage;
    Uri imageUri,uri;
    String key;

    StorageReference storageReference;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_faculty);

        dropdown = findViewById(R.id.dropdown);
        facultyName = findViewById(R.id.facultyName);
        facultyPost = findViewById(R.id.facultyPost);
        facultyEmail = findViewById(R.id.facultyEmail);
        facultySpecial = findViewById(R.id.facultySpecial);
        profilePic = findViewById(R.id.profilePic);
        submit = findViewById(R.id.submit);
        removeImage = findViewById(R.id.removeImage);

        storageReference = FirebaseStorage.getInstance().getReference().child("Faculty");

        uri = Uri.parse("android.resource://com.example.admincollegeapp/drawable/profilepic_5");
        try {
            InputStream stream = getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        imageUri=uri;
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        String departments[] = {"Select Department","CSE","ECE","EEE","CIVIL","MECHANICAL"};
        dropdown.setAdapter(new ArrayAdapter<String>(AddFacultyActivity.this,android.R.layout.simple_spinner_dropdown_item,departments));

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDepartment=dropdown.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(AddFacultyActivity.this, "Please select a Department", Toast.LENGTH_SHORT).show();
                selectedDepartment=null;
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name=facultyName.getText().toString();
                email=facultyEmail.getText().toString();
                post=facultyPost.getText().toString();
                specialization=facultySpecial.getText().toString();

                if(imageUri.equals(uri))
                    Toast.makeText(AddFacultyActivity.this, "Please select an Image", Toast.LENGTH_SHORT).show();
                else if(name.isEmpty()){
                    facultyName.setError("Empty");
                    facultyName.requestFocus();
                }
                else if(email.isEmpty()){
                    facultyEmail.setError("Empty");
                    facultyEmail.requestFocus();
                }
                else if(post.isEmpty()){
                    facultyPost.setError("Empty");
                    facultyPost.requestFocus();
                }
                else if(specialization.isEmpty()) {
                    facultySpecial.setError("Empty");
                    facultySpecial.requestFocus();
                }
                else if(selectedDepartment.equals("Select Department"))
                    Toast.makeText(AddFacultyActivity.this, "Please select a Department", Toast.LENGTH_SHORT).show();
                else
                    uploadImage();
            }
        });

        removeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageUri=uri;
                Toast.makeText(AddFacultyActivity.this, "Image removed", Toast.LENGTH_SHORT).show();
                profilePic.setImageURI(uri);
                removeImage.setVisibility(View.GONE);
            }
        });
    }

    private void uploadImage() {
        pd = new ProgressDialog(this);
        pd.setTitle("Uploading");
        pd.show();

        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-hh:mm:ss");
        String fileName = formatter.format(now);

        StorageReference reference = storageReference.child(selectedDepartment).child(fileName);
        reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Faculty");
                        databaseReference = databaseReference.child(selectedDepartment);
                        key=databaseReference.push().getKey();
                        FacultyData facultyData = new FacultyData(name,email,post,specialization,uri.toString(),selectedDepartment,key);
                        databaseReference.child(key).setValue(facultyData).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(AddFacultyActivity.this, "Data Saved", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddFacultyActivity.this, "Data Save Failed", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddFacultyActivity.this, "Data Save Failed", Toast.LENGTH_SHORT).show();
                    }
                });
                imageUri=uri;
                profilePic.setImageURI(uri);
                removeImage.setVisibility(View.GONE);
                facultyName.setText("");
                facultyEmail.setText("");
                facultyPost.setText("");
                facultySpecial.setText("");
                pd.dismiss();
                Toast.makeText(AddFacultyActivity.this,"Uploaded Successfully",Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                pd.setMessage("Uploaded:"+(int)progress+"%");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddFacultyActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                pd.dismiss();
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
            profilePic.setImageURI(imageUri);
            if(!imageUri.equals(R.drawable.profilepic_5));
                removeImage.setVisibility(View.VISIBLE);
        }
    }
}