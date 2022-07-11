package com.example.admincollegeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    CardView uploadNotice,uploadImage,uploadpdf,updateFaculty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uploadNotice = findViewById(R.id.addNotice);
        uploadNotice.setOnClickListener(this);
        uploadImage = findViewById(R.id.addGallery);
        uploadImage.setOnClickListener(this);
        uploadpdf = findViewById(R.id.addEbook);
        uploadpdf.setOnClickListener(this);
        updateFaculty = findViewById(R.id.updateFaculty);
        updateFaculty.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.addNotice:
                intent = new Intent(MainActivity.this,UploadNoticeActivity.class);
                startActivity(intent);
                break;
            case R.id.addGallery:
                intent = new Intent(MainActivity.this,UploadImageActivity.class);
                startActivity(intent);
                break;
            case R.id.addEbook:
                intent = new Intent(MainActivity.this,UploadEbookActivity.class);
                startActivity(intent);
                break;
            case R.id.updateFaculty:
                intent = new Intent(MainActivity.this,UpdateFacultyActivity.class);
                startActivity(intent);
                break;
        }
    }
}