package com.example.admincollegeapp.faculty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.admincollegeapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UpdateFacultyActivity extends AppCompatActivity {


    FloatingActionButton fab;
    RecyclerView cse,ece,eee,civil,mech;
    LinearLayout cseNodata,eceNodata,eeeNodata,civilNodata,mechNodata;
    List<FacultyData> cseList,eceList,eeeList,civilList,mechList;

    DatabaseReference reference,dbRef;
    FacultyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_faculty);

        cse = findViewById(R.id.cse);
        ece = findViewById(R.id.ece);
        eee = findViewById(R.id.eee);
        civil = findViewById(R.id.civil);
        mech = findViewById(R.id.mech);

        cseNodata = findViewById(R.id.cseNodata);
        eceNodata = findViewById(R.id.eceNodata);
        eeeNodata = findViewById(R.id.eeeNodata);
        civilNodata = findViewById(R.id.civilNodata);
        mechNodata = findViewById(R.id.mechNodata);

        reference = FirebaseDatabase.getInstance().getReference().child("Faculty");
        cseDep();
        eceDep();
        eeeDep();
        civilDep();
        mechDep();

        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdateFacultyActivity.this, AddFacultyActivity.class));
            }
        });
    }

    private void cseDep() {
        dbRef = reference.child("CSE");
        dbRef.addValueEventListener(new ValueEventListener() {         //get
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {   //as snapshot
                cseList = new ArrayList<>();
                if(!snapshot.exists()){                                  //if no snapshots
                    cseNodata.setVisibility(View.VISIBLE);
                    cse.setVisibility(View.GONE);
                }
                else{                                                    //if snapshot exists
                    cseNodata.setVisibility(View.GONE);
                    cse.setVisibility(View.VISIBLE);
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()) {     //iterate each snapshots
                        FacultyData data = dataSnapshot.getValue(FacultyData.class);      //change them to a class
                        cseList.add(data);                                                //add it in list
                    }
                    cse.setHasFixedSize(true);
                    cse.setLayoutManager(new LinearLayoutManager(UpdateFacultyActivity.this));      //set the layout fot the contents to be linear
                    adapter = new FacultyAdapter(cseList,UpdateFacultyActivity.this);       //list to adapter
                    cse.setAdapter(adapter);    //set adapter
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFacultyActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void eceDep() {
        dbRef = reference.child("ECE");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eceList = new ArrayList<>();
                if(!snapshot.exists()){
                    eceNodata.setVisibility(View.VISIBLE);
                    ece.setVisibility(View.GONE);
                }
                else{
                    eceNodata.setVisibility(View.GONE);
                    ece.setVisibility(View.VISIBLE);
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        FacultyData data = dataSnapshot.getValue(FacultyData.class);
                        eceList.add(data);
                    }
                    ece.setHasFixedSize(true);
                    ece.setLayoutManager(new LinearLayoutManager(UpdateFacultyActivity.this));
                    adapter = new FacultyAdapter(eceList,UpdateFacultyActivity.this);
                    ece.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFacultyActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void eeeDep() {
        dbRef = reference.child("EEE");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eeeList = new ArrayList<>();
                if(!snapshot.exists()){
                    eeeNodata.setVisibility(View.VISIBLE);
                    eee.setVisibility(View.GONE);
                }
                else{
                    eeeNodata.setVisibility(View.GONE);
                    eee.setVisibility(View.VISIBLE);
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        FacultyData data = dataSnapshot.getValue(FacultyData.class);
                        eeeList.add(data);
                    }
                    eee.setHasFixedSize(true);
                    eee.setLayoutManager(new LinearLayoutManager(UpdateFacultyActivity.this));
                    adapter = new FacultyAdapter(eeeList,UpdateFacultyActivity.this);
                    eee.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFacultyActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void civilDep() {
        dbRef = reference.child("CIVIL");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                civilList = new ArrayList<>();
                if(!snapshot.exists()){
                    civilNodata.setVisibility(View.VISIBLE);
                    civil.setVisibility(View.GONE);
                }
                else{
                    civilNodata.setVisibility(View.GONE);
                    civil.setVisibility(View.VISIBLE);
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        FacultyData data = dataSnapshot.getValue(FacultyData.class);
                        civilList.add(data);
                    }
                    civil.setHasFixedSize(true);
                    civil.setLayoutManager(new LinearLayoutManager(UpdateFacultyActivity.this));
                    adapter = new FacultyAdapter(civilList,UpdateFacultyActivity.this);
                    civil.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFacultyActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void mechDep() {
        dbRef = reference.child("MECHANICAL");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mechList = new ArrayList<>();
                if(!snapshot.exists()){
                    mechNodata.setVisibility(View.VISIBLE);
                    mech.setVisibility(View.GONE);
                }
                else{
                    mechNodata.setVisibility(View.GONE);
                    mech.setVisibility(View.VISIBLE);
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        FacultyData data = dataSnapshot.getValue(FacultyData.class);
                        mechList.add(data);
                    }
                    mech.setHasFixedSize(true);
                    mech.setLayoutManager(new LinearLayoutManager(UpdateFacultyActivity.this));
                    adapter = new FacultyAdapter(mechList,UpdateFacultyActivity.this);
                    mech.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFacultyActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}