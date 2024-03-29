package com.example.admincollegeapp.faculty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateTeacher extends AppCompatActivity {

    CircleImageView avatarProfile;
    EditText facultyName,facultyEmail,facultyPost,facultySpecial;
    TextView sample;
    Button submit;
    String len;

    String selectedDepartment;
    Uri imageUri,uri;
    ProgressDialog pd;

    String name,email,post,specialization,profile;
    StorageReference storageReference;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_teacher);

        imageUri = Uri.parse("android.resource://com.example.admincollegeapp/drawable/profilepic_5");
        uri = Uri.parse("android.resource://com.example.admincollegeapp/drawable/profilepic_5");

        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        post = getIntent().getStringExtra("post");
        specialization = getIntent().getStringExtra("specialization");
        profile = getIntent().getStringExtra("profile");
        key = getIntent().getStringExtra("key");
        selectedDepartment = getIntent().getStringExtra("selectedDepartment");

        sample = findViewById(R.id.sample);

        avatarProfile = findViewById(R.id.profilePic);
        facultyName = findViewById(R.id.facultyName);
        facultyEmail = findViewById(R.id.facultyEmail);
        facultyPost = findViewById(R.id.facultyPost);
        facultySpecial = findViewById(R.id.facultySpecial);
        submit = findViewById(R.id.submit);

        storageReference = FirebaseStorage.getInstance().getReference().child("Faculty");
        try {
            Picasso.get().load(profile).into(avatarProfile);
        }catch(Exception e){e.printStackTrace();}

        facultyName.setText(name);
        facultyEmail.setText(email);
        facultyPost.setText(post);
        facultySpecial.setText(specialization);

        avatarProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                name = facultyName.getText().toString();
                email = facultyEmail.getText().toString();
                post = facultyPost.getText().toString();
                specialization = facultySpecial.getText().toString();

                if(imageUri.equals(uri)) {
                    Toast.makeText(UpdateTeacher.this, "Please select an Image", Toast.LENGTH_SHORT).show();
                }
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
                    Toast.makeText(UpdateTeacher.this, "Please select a Department", Toast.LENGTH_SHORT).show();
                else
                    uploadImage();
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
        Bitmap bmp = null;
        try {
            bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 10, baos);
        byte[] data = baos.toByteArray();
        StorageReference reference = storageReference.child(selectedDepartment).child(fileName);
        reference.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Faculty");
                        databaseReference = databaseReference.child(selectedDepartment);

                        Map facultyData = new HashMap();
                        facultyData.put("name", name);
                        facultyData.put("email", email);
                        facultyData.put("post", post);
                        facultyData.put("specialization", specialization);
                        facultyData.put("image", uri.toString());
                        facultyData.put("department", selectedDepartment);
                        facultyData.put("key", key);

                        databaseReference.child(key).updateChildren(facultyData).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(UpdateTeacher.this, "Data Updated", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(UpdateTeacher.this, UpdateFacultyActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UpdateTeacher.this, "Data Update Failed", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateTeacher.this, "Data Save Failed", Toast.LENGTH_SHORT).show();
                    }
                });
                imageUri = uri;
                avatarProfile.setImageURI(uri);
                facultyName.setText("");
                facultyEmail.setText("");
                facultyPost.setText("");
                facultySpecial.setText("");
                pd.dismiss();
                Toast.makeText(UpdateTeacher.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                pd.setMessage("Uploaded:" + (int) progress + "%");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateTeacher.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        });
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && data!=null && data.getData()!=null){
            imageUri = data.getData();
            try {
                InputStream imageStream = getContentResolver().openInputStream(imageUri);
                Bitmap image = BitmapFactory.decodeStream(imageStream);
                File imagePath = new File(imageUri.getPath().replace("raw/",""));
                String a = Formatter.formatShortFileSize(UpdateTeacher.this,imagePath.length());
                sample.setText(a);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}