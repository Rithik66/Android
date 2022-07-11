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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class UploadEbookActivity extends AppCompatActivity {

    CardView selectPdf,preview;
    Button uploadPdf;
    EditText pdfTitle;
    Uri pdf;
    TextView previewText;
    ImageView closePreview;

    String fileName,key,imageLink;

    StorageReference storageReference;
    DatabaseReference reference;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_ebook);

        selectPdf = findViewById(R.id.addPdf);
        uploadPdf = findViewById(R.id.uploadPdf);
        pdfTitle = findViewById(R.id.pdfTitle);
        previewText = findViewById(R.id.previewText);
        preview = findViewById(R.id.preview);
        closePreview = findViewById(R.id.closePreview);

        storageReference = FirebaseStorage.getInstance().getReference("PDF/");
        reference = FirebaseDatabase.getInstance().getReference().child("PDF");

        selectPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pdfTitle.getText().toString().isEmpty()){
                    pdfTitle.setError("Empty");
                    pdfTitle.requestFocus();
                }
                else{
                    openDocuments();
                }
            }
        });

        uploadPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pdfTitle.getText().toString().isEmpty()){
                    pdfTitle.setError("Empty");
                    pdfTitle.requestFocus();
                }
                else if(pdf==null){
                    Toast.makeText(UploadEbookActivity.this,"Please select a PDF",Toast.LENGTH_SHORT).show();
                }
                else {
                    uploadPdf();
                }
            }
        });

        closePreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previewText.setText(null);
                preview.setVisibility(View.GONE);
                pdf=null;
                pdfTitle.setText("");
            }
        });
    }

    private void openDocuments() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select pdf files."),1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && data!=null && data.getData()!=null && resultCode == RESULT_OK){
            pdf=data.getData();
            if(pdf!=null) {
                preview.setVisibility(View.VISIBLE);
                previewText.setText(pdfTitle.getText().toString());
            }
        }
    }

    private void uploadPdf(){

        pd = new ProgressDialog(UploadEbookActivity.this);
        pd.setTitle("Uploading");
        pd.show();

        fileName = pdfTitle.getText().toString();
        storageReference = storageReference.child(fileName);
        storageReference.putFile(pdf).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Date now = new Date();
                        SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy");
                        String date = sdfDate.format(now);
                        SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm:ss a");
                        String time = sdfTime.format(now);

                        key = reference.push().getKey();
                        PdfData pdfData = new PdfData(uri.toString(),date,time,fileName,key);
                        reference.child(key).setValue(pdfData).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(UploadEbookActivity.this,"Data saved",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UploadEbookActivity.this,"Data save failed",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                Toast.makeText(UploadEbookActivity.this,"Pdf uploaded successfully",Toast.LENGTH_SHORT).show();
                pd.dismiss();
                previewText.setText(null);
                preview.setVisibility(View.GONE);
                pdf=null;
                pdfTitle.setText("");
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
                Toast.makeText(UploadEbookActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
            }
        });
    }
}