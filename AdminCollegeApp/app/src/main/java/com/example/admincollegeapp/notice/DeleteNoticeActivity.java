package com.example.admincollegeapp.notice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.admincollegeapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DeleteNoticeActivity extends AppCompatActivity {

    ProgressBar progressBar;
    RecyclerView deleteNoticeRecycler;
    ArrayList<NoticeData> list;
    NoticeAdapter adapter;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_notice);

        progressBar = findViewById(R.id.progressBar);
        deleteNoticeRecycler = findViewById(R.id.deleteNoticeRecycler);

        deleteNoticeRecycler.setLayoutManager(new LinearLayoutManager(DeleteNoticeActivity.this));
        deleteNoticeRecycler.setHasFixedSize(true);

        reference = FirebaseDatabase.getInstance().getReference().child("Notice");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    NoticeData data = dataSnapshot.getValue(NoticeData.class);
                    list.add(data);
                }
                adapter = new NoticeAdapter(DeleteNoticeActivity.this,list);

                progressBar.setVisibility(View.GONE);

                deleteNoticeRecycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);

                Toast.makeText(DeleteNoticeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}