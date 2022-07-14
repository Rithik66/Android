package com.example.admincollegeapp.faculty;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admincollegeapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FacultyAdapter extends RecyclerView.Adapter<FacultyAdapter.FacultyViewAdapter> {
    private List<FacultyData> facultyDataList;
    private Context context;

    public FacultyAdapter(List<FacultyData> facultyDataList, Context context) {
        this.facultyDataList = facultyDataList;
        this.context = context;
    }

    @NonNull
    @Override
    public FacultyViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.faculty_item_layout,parent,false);
        return new FacultyViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FacultyViewAdapter holder, int position) {
        FacultyData item = facultyDataList.get(position);
        holder.name.setText(item.getName().toUpperCase());
        holder.post.setText(item.getPost().toUpperCase());
        holder.email.setText(item.getEmail());
        try {
            Picasso.get().load(item.getImage()).into(holder.avatarProfile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateTeacher.class);
                intent.putExtra("profile",item.getImage());
                intent.putExtra("name",item.getName());
                intent.putExtra("email",item.getEmail());
                intent.putExtra("post",item.getPost());
                intent.putExtra("specialization",item.getSpecialization());
                intent.putExtra("key",item.getKey());
                intent.putExtra("selectedDepartment",item.getDepartment());

                context.startActivity(intent);
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialAlertDialogBuilder(context)
                        .setTitle("Confirm Delete")
                        .setMessage("Are you sure about deleting this field ?")
                        .setPositiveButton("Confirm",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Faculty");
                                databaseReference = databaseReference.child(item.getDepartment());
                                databaseReference.child(item.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(context, "Deleted SuccessFully", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Cancel",new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialog, int which) {}
                        })
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return facultyDataList.size();
    }

    public static void confirmDelete(){

    }

    class FacultyViewAdapter extends RecyclerView.ViewHolder {

        private TextView name,post,email;
        private Button editButton,deleteButton;
        private CircleImageView avatarProfile;

        public FacultyViewAdapter(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            post = itemView.findViewById(R.id.post);
            email = itemView.findViewById(R.id.email);

            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            avatarProfile = itemView.findViewById(R.id.avatarProfile);
        }
    }
}
