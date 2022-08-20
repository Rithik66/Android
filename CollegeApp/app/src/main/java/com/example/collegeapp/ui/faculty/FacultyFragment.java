package com.example.collegeapp.ui.faculty;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collegeapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class FacultyFragment extends Fragment {


    RecyclerView cse,ece,eee,civil,mech;
    LinearLayout cseNodata,eceNodata,eeeNodata,civilNodata,mechNodata;
    List<FacultyData> cseList,eceList,eeeList,civilList,mechList;

    DatabaseReference reference,dbRef;
    FacultyAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_faculty, container, false);
        cse = view.findViewById(R.id.cse);
        ece = view.findViewById(R.id.ece);
        eee = view.findViewById(R.id.eee);
        civil = view.findViewById(R.id.civil);
        mech = view.findViewById(R.id.mech);

        cseNodata = view.findViewById(R.id.cseNodata);
        eceNodata = view.findViewById(R.id.eceNodata);
        eeeNodata = view.findViewById(R.id.eeeNodata);
        civilNodata = view.findViewById(R.id.civilNodata);
        mechNodata = view.findViewById(R.id.mechNodata);

        reference = FirebaseDatabase.getInstance().getReference().child("Faculty");
        cseDep();
        eceDep();
        eeeDep();
        civilDep();
        mechDep();
        return view;
    }

    private void cseDep() {
        dbRef = reference.child("CSE");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {   //as snapshot
                cseList = new ArrayList<>();
                if(!snapshot.exists()){
                    //if no snapshots
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
                    cse.setLayoutManager(new LinearLayoutManager(getContext()));      //set the layout fot the contents to be linear
                    adapter = new FacultyAdapter(cseList,getContext());       //list to adapter
                    cse.setAdapter(adapter);    //set adapter
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                    ece.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter = new FacultyAdapter(eceList,getContext());
                    ece.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                    eee.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter = new FacultyAdapter(eeeList,getContext());
                    eee.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                    civil.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter = new FacultyAdapter(civilList,getContext());
                    civil.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                    mech.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter = new FacultyAdapter(mechList,getContext());
                    mech.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}